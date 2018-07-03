package com.fakie.cli.query;

import com.fakie.io.IOPath;
import com.fakie.io.output.FakieOutputException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.nio.file.Path;

@CommandLine.Command(name = "cypher", description = "Export the generated rules in the Cypher query language")
public class CypherExporter extends QueryExporterCommand {
    private static final Logger logger = LogManager.getFormatterLogger();

    @CommandLine.Option(names = {"-o", "--output"}, description = "Destination folder for the generated queries")
    private Path path = IOPath.CYPHER_FOLDER.asPath();

    @Override
    protected void createQueries() {
        try {
            fakie().exportRulesAsCypherQueries(path);
        }
        catch (FakieOutputException e) {
            logger.error(e);
        }
    }
}
