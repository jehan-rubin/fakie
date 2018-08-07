package com.fakie.cli;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Std {
    private final PrintStream stdout;
    private final PrintStream stderr;

    public Std() {
        this(System.out, System.err);
    }

    public Std(PrintStream stdout, PrintStream stderr) {
        this.stdout = stdout;
        this.stderr = stderr;
    }

    public void enableSystemOutput() {
        System.setOut(stdout);
        System.setErr(stderr);
    }

    public void disableSystemOutput() {
        // System.setOut(new NullPrintStream());
        // System.setErr(new NullPrintStream());
    }

    public PrintStream out() {
        return stdout;
    }

    public PrintStream err() {
        return stderr;
    }

    public static class NullPrintStream extends PrintStream {
        public NullPrintStream() {
            super(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    // Do nothing
                }
            });
        }
    }
}
