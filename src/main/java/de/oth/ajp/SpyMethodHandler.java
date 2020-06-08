package de.oth.ajp;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.Proxy;

import java.lang.reflect.Method;
import java.util.Arrays;

import static de.oth.ajp.Mocker.*;

public class SpyMethodHandler implements MethodHandler {

    private String[][] methodsString;
    private int i;

    public SpyMethodHandler(){
        methodsString = new String[10][3];
    }

    @Override
    public Object invoke(Object self, Method method, Method proceed, Object[] args) throws Throwable {
        if (!verificationOn) {
            methodsString[i][0] = method.getName();
            int o = 1;

            for(Object para : args){
                methodsString[i][o++] = para.toString();
            }
            i++;
            System.out.println(method.getName() + "(" + Arrays.toString(args) + ")" );

            return proceed.invoke(self, args);

        } else {
            int times = verification.times;
            ExtraInfo type = verification.info;

            final int[] actualTimes = {0};

            switch (type) {
                case NEVER -> {
                    boolean isRight = false;
                    for (int u = 0; u < i; u++) {
                        if (methodsString[u][0].equals(method.getName())) {
                            if (args.length == 0) {
                                actualTimes[0]++;
                            }
                            for (Object para : args) {
                                for (String s : methodsString[u]) {
                                    if (para.toString().equals(s)) {
                                        actualTimes[0]++;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (actualTimes[0] == 0) {
                        System.out.println(method.getName() + "() was used as wished");
                        verificationOn = false;
                        return getReturnType(method);
                    }
                    throw new AssertionError("Verification failure: " + method.getName() + "() Expected number of calls: " + times + " but was: " + actualTimes[0]);
                }
                case EXACTLY -> {
                    boolean isRight = false;
                    for (int u = 0; u < i; u++) {
                        if (methodsString[u][0].equals(method.getName())) {
                            if (args.length == 0) {
                                actualTimes[0]++;
                            }
                            for (Object para : args) {
                                for (String s : methodsString[u]) {
                                    if (para.toString().equals(s)) {
                                        actualTimes[0]++;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (actualTimes[0] == times) {
                        System.out.println(method.getName() + "() was used as wished");
                        verificationOn = false;
                        return getReturnType(method);
                    }
                    throw new AssertionError("Verification failure: " + method.getName() + "() Expected number of calls: " + times + " but was: " + actualTimes[0]);
                }
                case ATLEAST -> {
                    boolean isRight = false;
                    for (int u = 0; u < i; u++) {
                        if (methodsString[u][0].equals(method.getName())) {
                            if (args.length == 0) {
                                actualTimes[0]++;
                            }
                            for (Object para : args) {
                                for (String s : methodsString[u]) {
                                    if (para.toString().equals(s)) {
                                        actualTimes[0]++;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (actualTimes[0] >= times) {
                        System.out.println(method.getName() + "() was used as wished");
                        verificationOn = false;
                        return getReturnType(method);
                    }
                    throw new AssertionError("Verification failure: " + method.getName() + "() Expected number of calls: " + times + " but was: " + actualTimes[0]);
                }
                case ATMOST -> {
                    boolean isRight = false;
                    for (int u = 0; u < i; u++) {
                        if (methodsString[u][0].equals(method.getName())) {
                            if (args.length == 0) {
                                actualTimes[0]++;
                            }
                            for (Object para : args) {
                                for (String s : methodsString[u]) {
                                    if (para.toString().equals(s)) {
                                        actualTimes[0]++;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (actualTimes[0] <= times) {
                        System.out.println(method.getName() + "() was used as wished");
                        verificationOn = false;
                        return getReturnType(method);
                    }
                    throw new AssertionError("Verification failure: " + method.getName() + "() Expected number of calls: " + times + " but was: " + actualTimes[0]);
                }
                default -> {
                    return null;
                }
            }
        }
    }


    private Object getReturnType(Method method) {
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
