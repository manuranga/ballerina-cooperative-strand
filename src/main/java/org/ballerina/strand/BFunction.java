package org.ballerina.strand;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.lang.reflect.Field;

public abstract class BFunction {
    BFunction caller;
    protected BFunction waitingOn;
    protected BFunction nextBFunction;
    protected boolean nextBFunctionAsync;
    protected int state;
    protected boolean done;

    public BFunction(BFunction caller) {
        this.caller = caller;

    }

    public abstract void execute();

    @Override
    public String toString() {
        ReflectionToStringBuilder builder = new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE) {
            @Override
            protected boolean accept(Field field) {
                String name = field.getName();
                return name.startsWith("local_") || name.startsWith("param_");
            }
        };
        return  builder.toString() + "#" + state;
    }

    protected void error() {
        throw new IllegalStateException("Excessive scheduling " + toString());
    }
}
