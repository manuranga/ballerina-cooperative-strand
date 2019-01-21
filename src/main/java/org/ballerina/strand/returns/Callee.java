package org.ballerina.strand.returns;

import org.ballerina.strand.BFunction;

public class Callee extends BFunction {
    private final int i;
    int returns;

    Callee(BFunction caller, int i) {
        super(caller);
        this.i = i;
    }

    @Override
    public void execute() {
        switch (state) {
            case 0:
                returns = 42 + i;
                done = true;
                state++;
                return;
            default:
                error();
        }
    }
}
