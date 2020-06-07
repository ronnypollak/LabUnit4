package de.oth.ajp;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.Proxy;
import javassist.util.proxy.ProxyFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;

public class Mocker {

    //TODO: Arraylist hinzufügen welche für jedes Objekt methodsString speichert. so hat jedes objekt sein eigenes array an bereits benutzten methoden

    static ArrayList<Method> methodsUsed = new ArrayList<Method>();
    static String[][] methodsString = new String[10][3];
    static int i = 0;

    public static <T> T mock(Class<T> mockClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        ProxyFactory factory = new ProxyFactory();
        if (mockClass.isInterface()){
            factory.setInterfaces(new Class<?>[] {mockClass});
        }else {
            factory.setSuperclass(mockClass);
        }

        factory.setFilter(new MethodFilter() {
            public boolean isHandled(Method method) {
                //System.out.println("Handled?" + method.getName());
                return !method.getName().equals("finalize");
            }
        });

        MethodHandler handler = new MethodHandler() {
            public Object invoke(Object self, Method method, Method proceed, Object[] args) throws Throwable {
                methodsString[i][0] = method.getName();
                int o = 1;
                for (Parameter p : method.getParameters()
                     ) {
                    methodsString[i][o++] = p.getName();
                }

                //methodsString[i][1] = String.valueOf(method.getParameters());
                i++;
                //methodsUsed.add(method);

                System.out.println(method.getName() + "(" + Arrays.toString(args) + ")" );
                return getReturnType(method);

            }
        };

        Class c = factory.createClass(); //proxyClass
        Proxy proxy = (Proxy)c.getDeclaredConstructor().newInstance();
        proxy.setHandler(handler);
        return (T) proxy;
    }

    public static <T> T verify(T mockObject) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        Proxy proxy = (Proxy)mockObject;
        proxy.setHandler(new MethodHandler() {
            public Object invoke(Object self, Method method, Method proceed, Object[] args) throws Throwable {
                boolean isRight = false;

                for (int u = 0; u <= i; u++){
                    if(methodsString[u][0].equals(method.getName()) ){
                        for (Parameter p : method.getParameters()) {
                            for (String s: methodsString[u]) {
                                if(p.getName().equals(s)){
                                    isRight = true;
                                    break;
                                }
                            }
                        }
                        if(isRight){
                            System.out.println(method.getName() + "() was used");
                            return getReturnType(method);
                        }
                    }
                }throw new AssertionError("Verification failure: " + method.getName() + "() false use" );

            }
        });
        return mockObject;
    }

    private static Object getReturnType(Method method) {
        Class<?> returnType = method.getReturnType();


        if(Integer.TYPE.equals(returnType) ){
            return 0;

        } else if (Boolean.TYPE.equals(returnType) ){
            return false;

        } else if (Long.TYPE.equals(returnType)){
            return 0;
        }
        else if(String.class.equals(returnType)){
            return "";
        }else return null;
    }
}
