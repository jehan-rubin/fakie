package com.fakie.cli.query;

import picocli.CommandLine;

import java.nio.file.Path;

@CommandLine.Command(name = "cypher", description = "Save the generated rules in the Cypher query language")
public class CypherLanguage extends QueryLanguageCommand {
    @CommandLine.Option(names = {"-o", "--output"}, description = "Path to the generated queries folder")
    private Path path;

    @Override
    protected void createQueries() {
        fakie().saveRulesAsCypherQueries();
    }
}
