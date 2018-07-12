package com.fakie.cli.dataset;

import com.fakie.cli.FakieSubCommand;
import com.fakie.cli.learning.AprioriCommand;
import com.fakie.cli.learning.FPGrowthCommand;
import picocli.CommandLine;

@CommandLine.Command(subcommands = {FPGrowthCommand.class, AprioriCommand.class})
public abstract class GraphLoaderCommand extends FakieSubCommand {
    @Override
    protected void process() {
        loadGraph();
    }

    protected abstract void loadGraph();
}
