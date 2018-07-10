package com.fakie.cli.paprika;

import picocli.CommandLine;

import java.nio.file.Path;

@CommandLine.Command(name = "query",
        description = "Run Paprika query on the given db")
public class PaprikaQuery extends PaprikaCommand {
    @CommandLine.Option(names = {"-db", "--database"}, description = "Path to the info Paprika db")
    private Path db = null;

    @CommandLine.Option(names = {"-s", "--suffix"}, description = "Suffix for the csv filename")
    private String suffix = null;

    @Override
    protected void runPaprika() {
        fakie().runPaprikaQuery(db, suffix);
    }
}
