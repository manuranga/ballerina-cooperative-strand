package org.ballerina.strand;

import org.ballerina.strand.call.Caller;
import org.ballerina.strand.fib.Fib;
import org.ballerina.strand.fib.FibMain;
import org.ballerina.strand.fib.cps.CpsScheduler;
import org.ballerina.strand.interleave.Printer;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

import static org.ballerina.strand.Cli.SchedulerType.EAGER;
import static org.ballerina.strand.Cli.UseCase.FIB;
import static picocli.CommandLine.Model.CommandSpec;
import static picocli.CommandLine.Spec;

@Command(name = "bal-strand", showDefaultValues = true, subcommands = {HelpCommand.class})
public class Cli implements Callable<Void> {

    enum UseCase {
        FUNCTION, FIB, INTERLEAVE
    }

    enum SchedulerType {
        EAGER(new EagerScheduler()), FAIR(new FairScheduler());

        private final Scheduler instance;

        SchedulerType(Scheduler instance) {
            this.instance = instance;
        }
    }

    @Spec
    private CommandSpec spec;

    @Override
    public Void call() {
        spec.commandLine().usage(System.out);
        return null;
    }

    @Option(names = "-s", description = "one of: ${COMPLETION-CANDIDATES}")
    private static SchedulerType schedulerType = EAGER;

    @Option(names = "-u", description = "one of: ${COMPLETION-CANDIDATES}")
    private
    UseCase useCase = FIB;


    public static void main(String[] args) {
        CommandLine.call(new Cli(), args);
    }

    @Command(name = "strand", description = "run strand implementation")
    void runStrand(@Parameters(description = "number") int arg) throws Throwable {
        BFunction entryPoint = createEntryPoint(arg);
        long t = measureStrandTime(entryPoint);
        System.err.println("t=" + t);
    }

    @Command(name = "native", description = "run native implementation")
    void runNative(@Parameters(description = "number") int arg) {
        BFunction entryPoint = createEntryPoint(arg);
        long t = measureNativeTime(arg);
        System.err.println("t=" + t);
    }

    @Command(name = "stack", description = "run java stack growing implementation")
    void runStack(@Parameters(description = "number") int arg) {
        if (useCase != FIB) {
            throw new IllegalArgumentException("only fib is supported for this scheduler");
        }
        long startTime;
        long endTime;
        startTime = System.nanoTime();
        CpsScheduler.run(arg);
        endTime = System.nanoTime();
        System.err.println("t=" + (endTime - startTime));
    }

    private long measureNativeTime(int arg) {
        switch (useCase) {
            case FIB:
                return measureNativeFibTime(arg);
            case INTERLEAVE:
            case FUNCTION:
        }
        throw new UnsupportedOperationException();
    }

    private BFunction createEntryPoint(int arg) {
        switch (useCase) {
            case FIB:
                return new FibMain(null, arg);
            case INTERLEAVE:
                return new Printer(null, arg);
            case FUNCTION:
                return new Caller(null, arg);
        }
        return null;
    }

    private static double jvmVsCoopRuntimeRatio(int n) {
        double accumulated = 0;
        int count = 0;

        for (int i = 0; i < 200; i++) {
            long nativeTime = measureNativeFibTime(n);
            long coopTime = measureStrandTime(new Fib(null, n));
            if (i > 100) {
                // ignore first 100 times for jvm to warm up
                accumulated += coopTime / ((double) nativeTime);
                count++;
            }

        }
        return accumulated / count;
    }

    private static long measureStrandTime(BFunction entryPoint) {
        long startTime;
        long endTime;
        startTime = System.nanoTime();
        schedulerType.instance.run(entryPoint);
        endTime = System.nanoTime();
        return endTime - startTime;
    }

    private static long measureNativeFibTime(int n) {
        long startTime;
        long endTime;
        startTime = System.nanoTime();
        System.out.println(nativeFib(n));
        endTime = System.nanoTime();
        return endTime - startTime;
    }


    private static long nativeFib(int n) {
        long a = 1;
        if (n > 2) {
            a = nativeFib(n - 1) + nativeFib(n - 2);
        }
        return a;
    }
}
