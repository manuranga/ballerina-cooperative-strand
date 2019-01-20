package org.ballerina.strand.fib;


import org.ballerina.strand.BFunction;

public class FibMain extends BFunction {

    private Fib f1;

    public FibMain(BFunction caller) {
        super(caller);
    }

    public void execute() {
        switch (state) {
            case 0:
                 f1 = new Fib(this, 31);
                this.nextBFunction = f1;
                watingOn = this.nextBFunction;
                state++;
                return;
            case 1:
                System.out.println(f1.returns);
                state++;
                done = true;
                return;
            default:
                error();
        }
    }
}

