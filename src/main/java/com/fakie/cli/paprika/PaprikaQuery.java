package com.fakie.cli.paprika;

import com.fakie.io.IOPath;
import picocli.CommandLine;

import java.nio.file.Path;

@CommandLine.Command(name = "query", description = "Run Paprika query on the given db")
public class PaprikaQuery extends PaprikaCommand {
    @CommandLine.Option(names = {"-db", "--database"}, description = "Path to the info Paprika db")
    private Path db = IOPath.DB.asPath();

    @Override
    protected void runPaprika() {
        fakie().runPaprikaQuery(db);
    }
}
