package com.fakie.cli;

import java.io.PrintStream;

public class Std {
    private final PrintStream stdout;
    private final PrintStream stderr;

    public Std(PrintStream stdout, PrintStream stderr) {
        this.stdout = stdout;
        this.stderr = stderr;
    }

    public PrintStream out() {
        return stdout;
    }

    public PrintStream err() {
        return stderr;
    }
}
