package com.fakie.cli.query;

import picocli.CommandLine;

import java.nio.file.Path;

@CommandLine.Command(name = "cypher", description = "Export the generated rules in the Cypher query language")
public class CypherExporter extends QueryExporterCommand {
    @CommandLine.Option(names = {"-o", "--output"}, description = "Destination folder for the generated queries")
    private Path path;

    @Override
    protected void createQueries() {
        fakie().exportRulesAsCypherQueries();
    }
}
