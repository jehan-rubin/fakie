package com.fakie.cli.dataset;

import com.fakie.io.input.FakieInputException;
import picocli.CommandLine;

import java.nio.file.Path;

@CommandLine.Command(name = "load-neo4j", description = {"Import Android applications from a Neo4j database."})
public class LoadNeo4jDatabase extends GraphLoaderCommand {
    @CommandLine.Parameters(index = "0", paramLabel = "DB_PATH", description = "Path to the Neo4j database")
    private Path db;

    @Override
    protected void loadGraph() {
        try {
            fakie().loadGraphFromNeo4jDatabase(db);
        }
        catch (FakieInputException e) {
            std().err().println(e.getMessage());
        }
    }
}
