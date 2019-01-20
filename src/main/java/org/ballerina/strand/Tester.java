package org.ballerina.strand;

import org.ballerina.strand.fib.Fib;

import static org.ballerina.strand.Scheduler.run;

public class Tester {
    public static void main(String[] args) {
        jvmVsCoopPerfRatio();
    }

    private static void jvmVsCoopPerfRatio() {
        long startTime;
        long endTime;
        double accumulated = 0;
        int n = 15;
        int count = 0;

        for (int i = 0; i < 1000; i++) {
            startTime = System.nanoTime();
            nativeFib(n);
            endTime = System.nanoTime();
            long nativeTime = endTime - startTime;

            startTime = System.nanoTime();
            run(new Fib(null, n));
            endTime = System.nanoTime();
            long coopTime = endTime - startTime;

            if (i > 100) {
                // ignore first 100 times for jvm to warm up
                accumulated += coopTime / ((double) nativeTime);
                count++;
            }
        }

        System.out.println(accumulated / count);
    }


    private static long nativeFib(int n) {
        long a = 1;
        if (n > 2) {
            a = nativeFib(n - 1) + nativeFib(n - 2);
        }
        return a;
    }
}
