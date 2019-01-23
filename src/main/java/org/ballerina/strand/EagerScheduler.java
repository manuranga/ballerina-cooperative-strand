package org.ballerina.strand;

import org.ballerina.strand.interleave.Printer;

import java.util.ArrayDeque;
import java.util.Deque;

class EagerScheduler implements Scheduler {

    public void run(BFunction entryPoint) {
        Deque<BFunction> strandPool = new ArrayDeque<>();
        strandPool.add(entryPoint);
        while (true) {
            BFunction fn = strandPool.poll();
            if (fn == null) {
                break;
            }

            while (true) {
                BFunction next;
                fn.execute();

                BFunction callee = fn.callee;
                if (callee != null) {
                    fn.callee = null;
                    if (fn.waitingOn != callee) {
                        // async function call
                        strandPool.add(callee);
                        next = fn;
                    } else {
                        // function call
                        next = callee;
                    }
                } else if (fn.done) {
                    BFunction caller = fn.caller;
                    if (caller != null) {
                        fn.caller = null;
                        if (caller.waitingOn == fn) {
                            // function returns
                            next = caller;
                            next.waitingOn = null;
                        } else {
                            // async function returns
                            break;
                        }
                    } else {
                        break;
                    }
                } else {
                    // keep exec same function
                    next = fn;
                }

                fn = next;
            }
        }
    }

}