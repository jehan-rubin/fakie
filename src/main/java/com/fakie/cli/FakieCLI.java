package com.fakie.cli;


import com.fakie.Fakie;
import com.fakie.cli.dataset.LoadNeo4jDatabase;
import com.fakie.cli.macro.Generate;
import com.fakie.cli.paprika.PaprikaAnalyse;
import com.fakie.cli.paprika.PaprikaCustomQuery;
import com.fakie.cli.paprika.PaprikaQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(
        name = "fakie",
        header = {
                    "%n ███████╗ █████╗ ██╗  ██╗██╗███████╗" +
                    "%n ██╔════╝██╔══██╗██║ ██╔╝██║██╔════╝" +
                    "%n █████╗  ███████║█████╔╝ ██║█████╗  " +
                    "%n ██╔══╝  ██╔══██║██╔═██╗ ██║██╔══╝  " +
                    "%n ██║     ██║  ██║██║  ██╗██║███████╗" +
                    "%n ╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝╚══════╝%n"},
        description = {"Mining Mobile Apps to Learn Design Patterns and Code Smells."},
        subcommands = {
                PaprikaAnalyse.class,
                PaprikaQuery.class,
                PaprikaCustomQuery.class,
                LoadNeo4jDatabase.class,
                Generate.class
        })
public class FakieCLI extends FakieCommand {
    private static final Logger logger = LogManager.getFormatterLogger();
    private final Std std;
    private final Fakie fakie;
    private final CommandLine commandLine;

    public FakieCLI() {
        this(new Std(), new Fakie());
        logger.debug("Running Fakie CLI with the standard output and the standard error output");
    }

    FakieCLI(Std std, Fakie fakie) {
        this.std = std;
        this.fakie = fakie;
        this.commandLine = new CommandLine(this);
    }

    @Override
    public void run() {
        logger.trace("Parsing commandline");
        CommandLine.ParseResult result = commandLine.getParseResult();
        if (!result.hasSubcommand() && !commandLine.isUsageHelpRequested()) {
            logger.warn("Fakie CLI requires at least one command");
            commandLine.usage(std.err());
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

    @Override
    public CommandLine.ParseResult getSubResult() {
        return commandLine.getParseResult().subcommand();
    }
}
