package com.fakie.cli.paprika;

import com.fakie.io.IOPath;
import com.fakie.io.input.FakieInputException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Path;

@CommandLine.Command(name = "analyse", description = "Run Paprika analyse on given folder")
public class PaprikaAnalyse extends PaprikaCommand {
    private static final Logger logger = LogManager.getFormatterLogger();

    @CommandLine.Option(names = {"-a", "--android"}, required = true, description = "Path to the android platform jars")
    private File androidJars;

    @CommandLine.Option(names = {"-f", "--apk-folder"}, required = true, description = "Path to the apk folder")
    private File apk;

    @CommandLine.Option(names = {"-i", "--info-apk"}, required = true, description = "Path to the info apk file")
    private File info;

    @CommandLine.Option(names = {"-d", "--database"}, description = "Path to the info Paprika db")
    private Path db = IOPath.DB.asPath();

    @Override
    protected void runPaprika() {
        try {
            fakie().runPaprikaAnalyse(androidJars, apk, info, db);
        }
        catch (FakieInputException e) {
            logger.error(e);
        }
    }
}
