package com.fakie.cli.macro;

import com.fakie.cli.FakieSubCommand;
import com.fakie.utils.exceptions.FakieException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.nio.file.Path;

@CommandLine.Command(name = "generate",
        description = "Macro command to load a Neo4j database and then use " +
                "the FPGrowth algorithm to generate Cypher queries")
public class Generate extends FakieSubCommand {
    private static final Logger logger = LogManager.getFormatterLogger();

    @CommandLine.Option(names = {"-db", "--database"}, description = "Path to the Neo4j database")
    private Path db = null;

    @CommandLine.Option(names = {"-f", "--file"},
            description = "Path to the file containing the code smells in the database")
    private Path codesmell = null;

    @CommandLine.Option(names = {"-n", "--nb-rules"}, description = "Number of rules to find",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private int n = 10000;

    @CommandLine.Option(names = {"-s", "--min-support"}, description = "Minimum support bound",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private double support = 0.1;

    @CommandLine.Option(names = {"-o", "--output"}, description = "Destination folder for the generated queries")
    private Path output = null;

    @Override
    public void run() {
        process();
    }

    @Override
    protected void process() {
        try {
            fakie().loadGraphFromNeo4jDatabase(db);
            fakie().readCodeSmellFile(codesmell);
            fakie().fpGrowth(n, support);
            fakie().exportRulesAsCypherQueries(output);
        }
        catch (FakieException e) {
            logger.error(e);
        }
    }
}
