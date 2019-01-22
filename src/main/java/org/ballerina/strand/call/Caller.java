package org.ballerina.strand.call;

import org.ballerina.strand.BFunction;

/**
 *
 * function caller(){
 *   var x =  callee();
 *   io:println(x);
 * }
 *
 */
public class Caller extends BFunction {


    private Callee x;

    public Caller(BFunction caller) {
        super(caller);
    }

    @Override
    public void execute() {
        switch (state) {
            case 0:
                x = new Callee(this, 10);
                this.callee = x;
                waitingOn = this.callee;
                state++;
                return;
            case 1:
                System.out.println(x.returns);
                x = null;
                state++;
                done = true;
                return;
            default:
                error();
        }
    }
}
