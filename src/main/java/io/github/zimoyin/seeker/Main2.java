package io.github.zimoyin.seeker;

import io.github.zimoyin.seeker.reference.ClassVsFactory;
import io.github.zimoyin.seeker.reference.vs.interfaces.GeneralField;
import io.github.zimoyin.seeker.reference.vs.interfaces.GeneralMethod;
import io.github.zimoyin.seeker.reference.vs.interfaces.GeneralMethodParameter;
import io.github.zimoyin.seeker.reference.vs.visitor.ClassVs;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Objects;

@Deprecated
public class Main2 {
    public static void main(String[] args) throws IOException, NoSuchFieldException, ClassNotFoundException {
        @TestAnnotation ClassVs classVS = ClassVsFactory.getClassVS(Test2.class);

        for (GeneralField field : classVS.getFields()) {
            System.out.println(Arrays.toString(field.getGenericTypes()));
        }
        for (GeneralMethod method : classVS.getMethods()) {
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

class Test2<A extends Test> {
    A a;
    public <B extends Number,C extends Integer> B a(A a, B b) {
        return null;
    }
}