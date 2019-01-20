package org.ballerina.strand.interleave;

import org.ballerina.strand.BFunction;

public class PrintLetters extends BFunction {

    PrintLetters(BFunction caller) {
        super(caller);
    }

    public void execute() {
        switch (state) {
            case 0:
                System.out.println('A');
                state++;
                return;
            case 1:
                System.out.println('B');
                state++;
                return;
            case 2:
                System.out.println('C');
                state++;
                done = true;
                return;
            default:
                error();
        }
    }
}
