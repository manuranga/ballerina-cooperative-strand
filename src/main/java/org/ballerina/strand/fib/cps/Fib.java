package org.ballerina.strand.fib.cps;


/**
 * function fib(int n) returns int {
 * int a = 1;
 * if (n > 2){
 * a = fib(n - 1) + fib(n - 2);
 * }
 * return a;
 * }
 */

public class Fib {
    public static class FibFrame {
        public long f1;
        public long f2;
        public long a;
        public long n;
        int state;
    }

    public static class Continuation {
        public boolean yield;
        public int resumeIndex;
        public Object[] stack;
    }

    static int i;

    public static long fib(Continuation c, long n) {
        int state;
        long a = 0;
        long f1 = 0;
        long f2 = 0;

        FibFrame frame = null;
        if (c.resumeIndex > 0) {
            frame = (FibFrame) c.stack[--c.resumeIndex];
            state = frame.state;
            a = frame.a;
            f1 = frame.f1;
            f2 = frame.f2;
            n = frame.n;
            c.yield = false;

        } else {
            state = 0;
        }

        while (true) {
            switch (state) {
                case 0:
                    a = 1;
                    if (n > 2) {
                        f1 = fib(c, n - 1);
                        if (c.yield) {
                            FibFrame fibFrame = new FibFrame();
                            fibFrame.n = n;
                            c.stack[c.resumeIndex++] = fibFrame;
                            return -1;
                        }
                    } else {
                        state = 4;
                        break;
                    }
                case 1:
                    f2 = fib(c, n - 2);
                    if (c.yield) {
                        FibFrame fibFrame = new FibFrame();
                        fibFrame.f1 = f1;
                        fibFrame.a = a;
                        fibFrame.n = n;
                        fibFrame.state = 1;
                        c.stack[c.resumeIndex++] = fibFrame;
                        return -1;
                    }
                case 2:
//                    c.yield = true;
//                    c.stack = new Object[100];
//                    FibFrame fibFrame = new FibFrame();
//                    fibFrame.f1 = f1;
//                    fibFrame.f2 = f2;
//                    fibFrame.a = a;
//                    fibFrame.n = n;
//                    fibFrame.state = 3;
//                    c.stack[c.resumeIndex++] = fibFrame;
//                    return -1;
                case 3:
                    a = f1 + f2;
                case 4:
                    return a;
                default:
                    throw new IllegalStateException("Excessive scheduling : 1");
            }
        }
    }
}
