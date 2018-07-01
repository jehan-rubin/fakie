package com.fakie.cli.dataset;

import com.fakie.Fakie;
import com.fakie.cli.FakieCli;
import com.fakie.cli.FakieCommand;
import com.fakie.cli.Std;
import com.fakie.cli.learning.FPGrowthAlgorithm;
import picocli.CommandLine;

@CommandLine.Command(subcommands = {FPGrowthAlgorithm.class})
public abstract class GraphLoaderCommand extends FakieCommand {
    @CommandLine.ParentCommand
    private FakieCli parent;

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
        if (noAlgorithmChosen()) {
            std().err().println("Choose an algorithm among the available commands\n");
            CommandLine.usage(this, std().err());
        }
        else {
            loadGraph();
        }
    }

    protected abstract void loadGraph();

    private boolean noAlgorithmChosen() {
        CommandLine.ParseResult subResult = parent.getSubResult();
        return !subResult.hasSubcommand();
    }
}
