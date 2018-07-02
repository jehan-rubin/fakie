package com.fakie.cli.learning;

import com.fakie.utils.exceptions.FakieException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

@CommandLine.Command(
        name = "apriori",
        customSynopsis = "fakie GRAPH_LOADER apriori [-hV] QUERY_EXPORTER",
        description = "Use the  Apriori algorithm on the dataset")
public class AprioriAlgorithm extends FakieLearningCommand {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public void applyAlgorithm() {
        logger.debug("Using Apriori algorithm");
        try {
            fakie().apriori();
        }
        catch (FakieException e) {
            logger.error(e);
        }
    }
}
