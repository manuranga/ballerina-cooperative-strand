package org.ballerina.strand.fib;


import org.ballerina.strand.BFunction;

public class FibMain extends BFunction {

    private final int local_n;
    private Fib f1;

    public FibMain(BFunction caller, int n) {
        super(caller);
        local_n = n;
    }

    public void execute() {
        switch (state) {
            case 0:
                f1 = new Fib(this, local_n);
                this.nextBFunction = f1;
                waitingOn = this.nextBFunction;
                state++;
                return;
            case 1:
                System.out.println(f1.returns);
                f1 = null;
                state++;
                done = true;
                return;
            default:
                error();
        }
    }
}

