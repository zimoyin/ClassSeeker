package com.github.zimoyin.seeker;

import com.github.zimoyin.seeker.reference.ClassVsFactory;
import com.github.zimoyin.seeker.reference.vs.interfaces.GeneralMethod;
import com.github.zimoyin.seeker.reference.vs.interfaces.GeneralMethodParameter;
import com.github.zimoyin.seeker.reference.vs.visitor.ClassVs;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Objects;

@Deprecated
public class Main2 {
    public static void main(String[] args) throws IOException, NoSuchFieldException, ClassNotFoundException {
//        Class<?> anInt = Class.forName("int");
//        System.out.println(anInt);
        Class<Integer> aClass = int.class;

        @TestAnnotation ClassVs classVS = ClassVsFactory.getClassVS(Main2.class);
        for (GeneralMethod method : classVS.getMethods()) {
            if (!Objects.equals(method.getName(), "a")) continue;
            System.out.println("参数列表");
            for (GeneralMethodParameter parameter : method.getParameters()) {
                System.out.println(parameter.toString() + " -> " + Arrays.toString(parameter.getAnnotations()));
            }
            System.out.println("本地参数");
            for (GeneralMethodParameter generalMethodParameter : method.getLocalVariable()) {
                System.out.println(generalMethodParameter.getName() + " -> " + Arrays.toString(generalMethodParameter.getAnnotations()));
            }
            System.out.println("方法注解");
            System.out.println(method + " -> " + Arrays.toString(method.getAnnotations()));
            System.out.println("方法泛型");
            System.out.println(Arrays.toString(method.getGenericTypes()));
        }
    }

    //TODO 未能实现泛型
    @TestAnnotation(value = "aga")
    public <String> void a(String s, @TestAnnotation String a, int aaa) {
        @TestAnnotation int aa = 1;
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestAnnotation {
        String[] value() default "a";
    }
}

class Test2<A> {
    public <B extends Number,C extends Integer> void a(A a, B b) {
    }
}