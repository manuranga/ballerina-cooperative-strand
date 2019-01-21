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

    public Printer(BFunction caller) {
        super(caller);
    }

    public void execute() {
        switch (state) {
            case 0:
                nextBFunction = new PrintLetters(this);
                nextBFunctionAsync = true;
                state++;
                return;
            case 1:
                nextBFunction = new PrintInts(this);
                state++;
                waitingOn = nextBFunction;
                done = true;
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
