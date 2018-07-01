package com.fakie.cli.dataset;

import com.fakie.Fakie;
import com.fakie.cli.FakieCLI;
import com.fakie.cli.FakieCommand;
import com.fakie.cli.Std;
import com.fakie.cli.learning.FPGrowthAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

@CommandLine.Command(subcommands = {FPGrowthAlgorithm.class})
public abstract class GraphLoaderCommand extends FakieCommand {
    private static final Logger logger = LogManager.getFormatterLogger();

    @CommandLine.ParentCommand
    private FakieCLI parent;

    @Override
    public Std std() {
        return parent.std();
    }

    @Override
    public Fakie fakie() {
        return parent.fakie();
    }

    @Override
    public void run() {
        logger.debug("Using graph loader \'%s\'", this::name);
        if (noAlgorithmChosen()) {
            logger.warn("%s requires at least one command", this::name);
            std().err().println("Choose an algorithm among the available commands\n");
            CommandLine.usage(this, std().err());
        }
        else {
            loadGraph();
        }
    }

    protected abstract void loadGraph();

    private String name() {
        CommandLine.ParseResult subResult = parent.getSubResult();
        return subResult.commandSpec().name();
    }

    private boolean noAlgorithmChosen() {
        CommandLine.ParseResult subResult = parent.getSubResult();
        return !subResult.hasSubcommand();
    }
}
