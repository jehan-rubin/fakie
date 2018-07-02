package com.fakie.cli.learning;

import com.fakie.utils.exceptions.FakieException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

@CommandLine.Command(
        name = "fpgrowth", aliases = {"fpg"},
        customSynopsis = "fakie GRAPH_LOADER fpgrowth [-hV] QUERY_EXPORTER",
        description = "Use the  FPGrowth algorithm on the dataset")
public class FPGrowthAlgorithm extends FakieLearningCommand {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public void applyAlgorithm() {
        logger.debug("Using FPGrowth algorithm");
        try {
            fakie().fpGrowth();
        }
        catch (FakieException e) {
            logger.error(e);
        }
    }
}
