package com.fakie.cli.learning;

import com.fakie.cli.FakieSubCommand;
import com.fakie.cli.query.CypherExporter;
import com.fakie.io.input.FakieInputException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.nio.file.Path;

@CommandLine.Command(subcommands = {CypherExporter.class})
public abstract class FakieLearningCommand extends FakieSubCommand {
    private static final Logger logger = LogManager.getFormatterLogger();

    @CommandLine.Option(names = {"-n", "--nb-rules"}, description = "Number of rules to find",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    protected int n = 10000;

    @CommandLine.Option(names = {"-s", "--min-support"}, description = "Minimum support bound",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    protected double support = 0.1;

    @CommandLine.Option(names = {"-f", "--file"},
            description = "Path to the file containing the code smells in the database")
    private Path codesmell = null;

    @Override
    protected void process() {
        try {
            fakie().readCodeSmellFile(codesmell);
            applyAlgorithm();
        }
        catch (FakieInputException e) {
            logger.error(e);
        }
    }

    protected abstract void applyAlgorithm();
}
