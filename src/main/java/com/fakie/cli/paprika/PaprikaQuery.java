package com.fakie.cli.paprika;

import com.fakie.io.IOPath;
import com.fakie.utils.paprika.PaprikaException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.nio.file.Path;

@CommandLine.Command(name = "query", description = "Run Paprika query on the given db")
public class PaprikaQuery extends PaprikaCommand {
    private static final Logger logger = LogManager.getFormatterLogger();

    @CommandLine.Option(names = {"-db", "--database"}, description = "Path to the info Paprika db")
    private Path db = null;

    @CommandLine.Option(names = {"-s", "--suffix"}, description = "Suffix for the csv filename")
    private String suffix = IOPath.CODE_SMELL.asString().concat("/");

    @Override
    protected void runPaprika() {
        try {
            fakie().runPaprikaQuery(std(), db, suffix);
        }
        catch (PaprikaException e) {
            logger.error(e);
        }
    }
}
