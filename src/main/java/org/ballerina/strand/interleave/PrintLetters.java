package org.ballerina.strand.interleave;

import org.ballerina.strand.BFunction;

/**
 *
 * function printInt(){
 *     int i = 0;
 *     while(i < 20){
 *         io:println(i);
 *         i++;
 *     }
 * }
 *
 */
public class PrintLetters extends BFunction {

    private int local_i;
    PrintLetters(BFunction caller) {
        super(caller);
    }

    public void execute() {
        switch (state) {
            case 0:
                local_i = 0;
                state++;
                return;
            case 1:
                if (local_i < 20) {
                    state++;
                } else {
                    state = 3;
                }
                return;
            case 2:
                System.out.println((char) (65 + local_i));
                local_i++;
                state = 1;
                return;
            case 3:
                state++;
                done = true;
                return;
            default:
                error();
        }
    }
}
