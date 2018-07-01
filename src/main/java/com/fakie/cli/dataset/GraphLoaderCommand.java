package com.fakie.cli.dataset;

import com.fakie.Fakie;
import com.fakie.cli.FakieCLI;
import com.fakie.cli.FakieCommand;
import com.fakie.cli.FakieSubCommand;
import com.fakie.cli.Std;
import com.fakie.cli.learning.AprioriAlgorithm;
import com.fakie.cli.learning.FPGrowthAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

@CommandLine.Command(
        commandListHeading = "@|bold %nLearning Algorithm|@:%n",
        subcommands = {FPGrowthAlgorithm.class, AprioriAlgorithm.class})
public abstract class GraphLoaderCommand extends FakieSubCommand {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    protected void process() {
        loadGraph();
    }

    protected abstract void loadGraph();
}
