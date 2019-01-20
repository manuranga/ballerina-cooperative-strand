package org.ballerina.strand.interleave;

import org.ballerina.strand.BFunction;

public class PrintInts extends BFunction {

    PrintInts(BFunction caller) {
        super(caller);
    }

    public void execute() {
        switch (state) {
            case 0:
                System.out.println('1');
                state++;
                return;
            case 1:
                System.out.println('2');
                state++;
                return;
            case 2:
                System.out.println('3');
                done = true;
                state++;
                return;
            default:
                error();
        }
    }

}
