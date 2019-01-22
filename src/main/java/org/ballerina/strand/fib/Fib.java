package org.ballerina.strand.fib;


import org.ballerina.strand.BFunction;

/**
 *function fib(int n) returns int {
 *    int a = 1;
 *    if (n > 2){
 *        a = fib(n - 1) + fib(n - 2);
 *    }
 *    return a;
 *}
 *
 */

public class Fib extends BFunction {

    private final long param_n;
    private long local_a;
    private Fib f1;
    long returns;
    private Fib f2;

    public Fib(BFunction caller, long n) {
        super(caller);
        this.param_n = n;
    }

    public void execute() {
        switch (state) {
            case 0:
                local_a = 1;
                if (param_n > 2) {
                    f1 = new Fib(this, param_n - 1);
                    callee = f1;
                    waitingOn = callee;
                    state++;
                } else {
                    state = 3;
                }
                return;
            case 1:
                f2 = new Fib(this, param_n - 2);
                callee = f2;
                waitingOn = callee;
                state++;
                return;
            case 2:
                local_a = f1.returns + f2.returns;
                f1 = null;
                f2 = null;
                state++;
                return;
            case 3:
                returns = local_a;
                state++;
                done = true;
                return;
            default:
                error();
        }
    }
}
