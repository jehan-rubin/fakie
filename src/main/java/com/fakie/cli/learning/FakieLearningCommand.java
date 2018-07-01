package com.fakie.cli.learning;

import com.fakie.cli.FakieSubCommand;
import com.fakie.cli.query.CypherLanguage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

@CommandLine.Command(
        commandListHeading = "@|bold %nQuery Language|@:%n",
        subcommands = {CypherLanguage.class})
public abstract class FakieLearningCommand extends FakieSubCommand {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    protected void process() {
        applyAlgorithm();
    }

    protected abstract void applyAlgorithm();
}
