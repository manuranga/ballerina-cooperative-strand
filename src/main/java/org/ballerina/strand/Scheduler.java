package org.ballerina.strand;

import java.util.ArrayDeque;
import java.util.Deque;

class Scheduler {

    static void run(BFunction entryPoint) {
        Deque<BFunction> running = new ArrayDeque<>();
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

            if (fn.done && fn.caller != null) {
                if (fn.caller.waitingOn == fn) {
                    running.add(fn.caller);
                    fn.waitingOn = null;
                }
                fn.caller = null;
            }
        }
    }

}