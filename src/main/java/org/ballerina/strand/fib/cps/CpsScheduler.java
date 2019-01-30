package org.ballerina.strand.fib.cps;

public class CpsScheduler {
    public static void run(int i) {
        Fib.Continuation c = new Fib.Continuation();

        long fib = Fib.fib(c, i);
        while (c.yield) {
            fib = Fib.fib(c, -1);
        }
        System.out.println(fib);
    }


}