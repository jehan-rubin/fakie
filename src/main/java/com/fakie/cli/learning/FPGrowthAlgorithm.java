package com.fakie.cli.learning;

import com.fakie.learning.Rule;
import com.fakie.utils.exceptions.FakieException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(name = "FPGrowth", aliases = {"FPG"}, description = "Use the  FPGrowth algorithm on the dataset")
public class FPGrowthAlgorithm extends FakieLearningCommand {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public void applyAlgorithm() {
        logger.debug("Using FPGrowth algorithm");
        try {
            List<Rule> rules = fakie().fpGrowth();
            logger.info("Generated rules : ");
            for (Rule rule : rules) {
                logger.info("\t %s", rule);
            }
        }
        catch (FakieException e) {
            logger.error(e);
        }
    }
}
