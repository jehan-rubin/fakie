package com.fakie.cli;


import com.fakie.Fakie;
import com.fakie.cli.dataset.LoadNeo4jDatabase;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(
        name = "Fakie",
        header = {
                "%n ███████╗ █████╗ ██╗  ██╗██╗███████╗" +
                "%n ██╔════╝██╔══██╗██║ ██╔╝██║██╔════╝" +
                "%n █████╗  ███████║█████╔╝ ██║█████╗  " +
                "%n ██╔══╝  ██╔══██║██╔═██╗ ██║██╔══╝  " +
                "%n ██║     ██║  ██║██║  ██╗██║███████╗" +
                "%n ╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝╚══════╝%n"},
        description = {"Mining Mobile Apps to Learn Design Patterns and Code Smells."},
        subcommands = {LoadNeo4jDatabase.class})
public class FakieCli extends FakieCommand {
    private final Std std;
    private final Fakie fakie;
    private final CommandLine commandLine;

    public FakieCli() {
        this(new Std(System.out, System.err), new Fakie());
    }

    FakieCli(Std std, Fakie fakie) {
        this.std = std;
        this.fakie = fakie;
        this.commandLine = new CommandLine(this);
    }

    @Override
    public void run() {
        CommandLine.ParseResult result = commandLine.getParseResult();
        if (!result.hasSubcommand() && !commandLine.isUsageHelpRequested()) {
            commandLine.usage(std.out());
        }
    }

    public void parse(String... args) {
        commandLine.parseWithHandlers(
                new CommandLine.RunAll().useOut(std.out()).useErr(std.err()),
                new CommandLine.DefaultExceptionHandler<List<Object>>().useOut(std.out()).useErr(std.err()),
                args);
    }

    @Override
    public Std std() {
        return std;
    }

    @Override
    public Fakie fakie() {
        return fakie;
    }

    public CommandLine.ParseResult getSubResult() {
        return commandLine.getParseResult().subcommand();
    }
}
