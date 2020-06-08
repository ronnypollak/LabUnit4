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
import java.util.HashMap;

import static de.oth.ajp.ExtraInfo.*;

public class Mocker {

    static HashMap<Object, String[][]> objMethods = new HashMap<>();
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

                for(Object para : args){
                    methodsString[i][o++] = para.toString();
                }
                i++;
                System.out.println(method.getName() + "(" + Arrays.toString(args) + ")" );
                return getReturnType(method);

            }
        };

        Class c = factory.createClass(); //proxyClass
        Proxy proxy = (Proxy)c.getDeclaredConstructor().newInstance();
        proxy.setHandler(handler);
        return (T) proxy;
    }

    public static <T> T spy(T mockClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        ProxyFactory factory = new ProxyFactory();
        factory.setSuperclass(mockClass.getClass());

        factory.setFilter(new MethodFilter() {
            public boolean isHandled(Method method) {
                return !method.getName().equals("finalize");
            }
        });

        MethodHandler handler = new MethodHandler() {
            public Object invoke(Object self, Method method, Method proceed, Object[] args) throws Throwable {
                methodsString[i][0] = method.getName();
                int o = 1;

                for(Object para : args){
                    methodsString[i][o++] = para.toString();
                }
                i++;
                System.out.println(method.getName() + "(" + Arrays.toString(args) + ")" );

                return proceed.invoke(self, args);

            }
        };

        Class c = factory.createClass(); //proxyClass
        Proxy proxy = (Proxy)c.getDeclaredConstructor().newInstance();
        proxy.setHandler(handler);
        return (T) proxy;
    }

    public static <T> T verify(T mockObject) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        int times = 1;
        final int[] actualTimes = {0};

        Proxy proxy = (Proxy)mockObject;
        proxy.setHandler(new MethodHandler() {
            public Object invoke(Object self, Method method, Method proceed, Object[] args) throws Throwable {
                boolean isRight = false;

                for (int u = 0; u < i; u++){
                    if(methodsString[u][0].equals(method.getName()) ){
                        if (args.length == 0){
                            actualTimes[0]++;
                        }
                        for (Object para : args) {
                            for (String s: methodsString[u]) {
                                if(para.toString().equals(s)){
                                    isRight = true;
                                    break;
                                }
                            }
                        }

                    }
                }if(isRight){
                    System.out.println(method.getName() + "() was used as wished");
                    return getReturnType(method);
                }
                throw new AssertionError("Verification failure: " + method.getName() + "() Expected number of calls: " + times + " but was: " + actualTimes[0]);

            }
        });
        return mockObject;
    }

    public static <T> T verify(T mockObject, Verification v) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        int x = i;
        int times = v.times;
        final int[] actualTimes = {0};
        switch (v.info){
            case NEVER -> {
                Proxy proxy = (Proxy)mockObject;
                proxy.setHandler(new MethodHandler() {
                    public Object invoke(Object self, Method method, Method proceed, Object[] args) throws Throwable {
                        boolean isRight = false;

                        for (int u = 0; u < i; u++){
                            if(methodsString[u][0].equals(method.getName()) ){
                                if (args.length == 0){
                                    actualTimes[0]++;
                                }
                                for (Object para : args) {
                                    for (String s: methodsString[u]) {
                                        if(para.toString().equals(s)){
                                            actualTimes[0]++;
                                            break;
                                        }
                                    }
                                }
                            }
                        }if(actualTimes[0] == 0){
                            System.out.println(method.getName() + "() was used as wished");
                            return getReturnType(method);
                        }
                        throw new AssertionError("Verification failure: " + method.getName() + "() Expected number of calls: " + times + " but was: " + actualTimes[0]);

                    }
                });
                return mockObject;
            }
            case EXACTLY -> {
                Proxy proxy = (Proxy)mockObject;
                proxy.setHandler(new MethodHandler() {
                    public Object invoke(Object self, Method method, Method proceed, Object[] args) throws Throwable {
                        boolean isRight = false;

                        for (int u = 0; u < i; u++){
                            if(methodsString[u][0].equals(method.getName()) ){
                                if (args.length == 0){
                                    actualTimes[0]++;
                                }
                                for (Object para : args) {
                                    for (String s: methodsString[u]) {
                                        if(para.toString().equals(s)){
                                            actualTimes[0]++;
                                            break;
                                        }
                                    }
                                }

                            }
                        }if(actualTimes[0] == times){
                            System.out.println(method.getName() + "() was used as wished");
                            return getReturnType(method);
                        }
                        throw new AssertionError("Verification failure: " + method.getName() + "() Expected number of calls: " + times + " but was: " + actualTimes[0]);

                    }
                });
                return mockObject;
            }
            case ATLEAST -> {
                Proxy proxy = (Proxy)mockObject;
                proxy.setHandler(new MethodHandler() {
                    public Object invoke(Object self, Method method, Method proceed, Object[] args) throws Throwable {
                        boolean isRight = false;

                        for (int u = 0; u < i; u++){
                            if(methodsString[u][0].equals(method.getName()) ){
                                if (args.length == 0){
                                    actualTimes[0]++;
                                }
                                for (Object para : args) {
                                    for (String s: methodsString[u]) {
                                        if(para.toString().equals(s)){
                                            actualTimes[0]++;
                                            break;
                                        }
                                    }
                                }

                            }
                        }if(actualTimes[0] >= times){
                            System.out.println(method.getName() + "() was used as wished");
                            return getReturnType(method);
                        }
                        throw new AssertionError("Verification failure: " + method.getName() + "() Expected number of calls: " + times + " but was: " + actualTimes[0]);

                    }
                });
                return mockObject;
            }
            case ATMOST -> {
                Proxy proxy = (Proxy)mockObject;
                proxy.setHandler(new MethodHandler() {
                    public Object invoke(Object self, Method method, Method proceed, Object[] args) throws Throwable {
                        boolean isRight = false;

                        for (int u = 0; u < i; u++){
                            if(methodsString[u][0].equals(method.getName()) ){
                                if (args.length == 0){
                                    actualTimes[0]++;
                                }
                                for (Object para : args) {
                                    for (String s: methodsString[u]) {
                                        if(para.toString().equals(s)){
                                            actualTimes[0]++;
                                            break;
                                        }
                                    }
                                }

                            }
                        }if(actualTimes[0] <= times){
                            System.out.println(method.getName() + "() was used as wished");
                            return getReturnType(method);
                        }
                        throw new AssertionError("Verification failure: " + method.getName() + "() Expected number of calls: " + times + " but was: " + actualTimes[0]);

                    }
                });
                return mockObject;
            }
            default -> {
                return null;
            }
        }
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
