package org.ballerina.strand;

import org.ballerina.strand.fib.Fib;
import org.ballerina.strand.fib.FibMain;

import static org.ballerina.strand.Scheduler.run;

public class Tester {
    public static void main(String[] args) {
        String cmd = args[0];
        int n = Integer.parseInt(args[1]);
        switch (cmd) {
            case "native":
                System.out.println(nativeFib(n));
                return;
            case "native-time":
                System.out.println(measureNativeTime(n));
                return;
            case "strand-time":
                System.out.println(measureCoopTime(n));
                return;
            case "strand":
                run(new FibMain(null, n));
                return;
            case "ratio":
                System.out.println(jvmVsCoopRuntimeRatio(n));
                return;
            default:
                System.out.println("Usage: <native|stand|ratio|native-time|stand-time> <n>");
        }
    }

    private static double jvmVsCoopRuntimeRatio(int n) {
        double accumulated = 0;
        int count = 0;

        for (int i = 0; i < 200; i++) {
            long nativeTime = measureNativeTime(n);
            long coopTime = measureCoopTime(n);
            if (i > 100) {
                // ignore first 100 times for jvm to warm up
                accumulated += coopTime / ((double) nativeTime);
                count++;
            }

        }
        return accumulated / count;
    }

    private static long measureCoopTime(int n) {
        long startTime;
        long endTime;
        startTime = System.nanoTime();
        run(new Fib(null, n));
        endTime = System.nanoTime();
        return endTime - startTime;
    }

    private static long measureNativeTime(int n) {
        long startTime;
        long endTime;
        startTime = System.nanoTime();
        nativeFib(n);
        endTime = System.nanoTime();
        return endTime - startTime;
    }


    private static long nativeFib(int n) {
        long a = 1;
        if (n > 2) {
            a = nativeFib(n - 1) + nativeFib(n - 2);
        }
        return a;
    }
}
