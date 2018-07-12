package com.fakie.cli.learning;

import com.fakie.utils.exceptions.FakieException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

@CommandLine.Command(name = "fpgrowth", aliases = {"fpg"}, description = "Use the FPGrowth algorithm on the dataset")
public class FPGrowthCommand extends FakieLearningCommand {
    private static final Logger logger = LogManager.getFormatterLogger();

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
