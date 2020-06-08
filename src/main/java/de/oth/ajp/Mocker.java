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
import javassist.util.proxy.Proxy;
import javassist.util.proxy.ProxyFactory;

import static de.oth.ajp.ExtraInfo.*;

public class Mocker {
    static Verification verification;
    static boolean verificationOn = false;

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

        MethodHandler handler = new MockerMethodHandler();

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


        MethodHandler handler = new SpyMethodHandler();

        Class c = factory.createClass(); //proxyClass
        Proxy proxy = (Proxy)c.getDeclaredConstructor().newInstance();
        proxy.setHandler(handler);
        return (T) proxy;
    }

    public static <T> T verify(T mockObject) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        verification = verification.times(1);
        verificationOn = true;

        return mockObject;
    }

    public static <T> T verify(T mockObject, Verification v) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        verificationOn = true;
        verification = v;

        return mockObject;

    }

}
