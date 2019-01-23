package org.ballerina.strand.interleave;


import org.ballerina.strand.BFunction;

/**
 *
 * function printer(){
 *     start printLetters();
 *     printInts();
 * }
 *
 */

public class Printer extends BFunction {

    public Printer(BFunction caller, int arg) {
        super(caller);
    }

    public void execute() {
        switch (state) {
            case 0:
                callee = new PrintLetters(this);
                state++;
                return;
            case 1:
                callee = new PrintInts(this);
                state++;
                waitingOn = callee;
                return;
            case 2:
                state++;
                done = true;
                return;
            default:
                error();
        }
    }
}
