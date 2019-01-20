package org.ballerina.strand;

import org.ballerina.strand.fib.Fib;

import java.util.Deque;
import java.util.LinkedList;

class Scheduler {

    static void run(BFunction entryPoint) {
        Deque<BFunction> running = new LinkedList<>();
        running.add(entryPoint);
        while (true) {
            BFunction fn = running.poll();
            if (fn == null) {
                break;
            }

            fn.execute();
            if (fn.nextBFunction != null) {
                running.add(fn.nextBFunction);
                fn.nextBFunction = null;
                if (fn.nextBFunctionAsync) {
                    running.add(fn);
                    fn.nextBFunctionAsync = false;
                }
            } else if (!fn.done) {
                running.add(fn);
            }

            if (fn.done && fn.caller != null && fn.caller.watingOn == fn) {
                running.add(fn.caller);
                fn.watingOn = null;
            }
        }
    }

}