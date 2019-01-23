package org.ballerina.strand;

import java.util.ArrayDeque;
import java.util.Deque;

public class FairScheduler implements Scheduler {

    public void run(BFunction entryPoint) {
        Deque<BFunction> running = new ArrayDeque<>();
        running.add(entryPoint);
        while (true) {
            BFunction fn = running.poll();
            if (fn == null) {
                break;
            }

            fn.execute();
            if (fn.callee != null) {
                running.add(fn.callee);
                if (fn.waitingOn != fn.callee) {
                    running.add(fn);
                }
                fn.callee = null;
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