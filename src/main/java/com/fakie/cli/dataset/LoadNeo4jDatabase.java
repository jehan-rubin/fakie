package com.fakie.cli.dataset;

import com.fakie.io.IOPath;
import com.fakie.io.input.FakieInputException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.nio.file.Path;

@CommandLine.Command(
        name = "load-neo4j",
        description = {"Import Android applications from a Neo4j database."})
public class LoadNeo4jDatabase extends GraphLoaderCommand {
    private static final Logger logger = LogManager.getFormatterLogger();

    @CommandLine.Parameters(index = "0", paramLabel = "DB_PATH", description = "Path to the Neo4j database")
    private Path db = IOPath.DB.asPath();

    @Override
    protected void loadGraph() {
        try {
            fakie().loadGraphFromNeo4jDatabase(db);
        }
        catch (FakieInputException e) {
            logger.error(e);
        }
    }
}
