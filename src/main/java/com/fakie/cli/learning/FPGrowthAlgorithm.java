package com.fakie.cli.learning;

import com.fakie.utils.exceptions.FakieException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

@CommandLine.Command(
        name = "fpgrowth", aliases = {"fpg"},
        customSynopsis = "fakie GRAPH_LOADER fpgrowth [-hV] -f=<file> [-n=<n>] [-s=<support>] QUERY_EXPORTER",
        description = "Use the  FPGrowth algorithm on the dataset")
public class FPGrowthAlgorithm extends FakieLearningCommand {
    private static final Logger logger = LogManager.getFormatterLogger();

    @CommandLine.Option(names = {"-n", "--nb-rules"}, description = "Number of rules to find",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private int n = 10000;

    @CommandLine.Option(names = {"-s", "--min-support"}, description = "Minimum support bound",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private double support = 0.1;

    @Override
    public void applyAlgorithm() {
        logger.debug("Using FPGrowth algorithm");
        try {
            fakie().fpGrowth(n, support);
        }
        catch (FakieException e) {
            logger.error(e);
        }
    }
}
