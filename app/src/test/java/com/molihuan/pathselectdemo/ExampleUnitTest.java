package com.molihuan.pathselectdemo;

import org.junit.Test;

class A {
}

class B extends A {
}


class C extends A {
}

class D extends B {
}

public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {

        A a = new A();
        B b = new B();

        C c = new C();
        D d = new D();

//        System.out.println(b.getClass().isAssignableFrom(b.getClass()));
//
//        System.out.println(c.getClass().isAssignableFrom(b.getClass()));
//        System.out.println(d.getClass().isAssignableFrom(b.getClass()));

        System.out.println(b.getClass().isAssignableFrom(B.class));

        System.out.println(c.getClass().isAssignableFrom(B.class));
        System.out.println(d.getClass().isAssignableFrom(B.class));
        //////////////////////////////////////////////////////////////////

        System.out.println(b.getClass().isAssignableFrom(c.getClass()));
        System.out.println(b.getClass().isAssignableFrom(d.getClass()));


        System.out.println("=====================================");
        System.out.println(A.class.isAssignableFrom(a.getClass()));
        System.out.println(A.class.isAssignableFrom(b.getClass()));
        System.out.println(A.class.isAssignableFrom(c.getClass()));
        System.out.println(Object.class.isAssignableFrom(a.getClass()));
        System.out.println(Object.class.isAssignableFrom(String.class));
        System.out.println(String.class.isAssignableFrom(Object.class));


    }
}