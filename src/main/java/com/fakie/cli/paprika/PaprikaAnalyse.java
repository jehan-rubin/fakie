package com.fakie.cli.paprika;

import com.fakie.io.IOPath;
import com.fakie.utils.exceptions.FakieException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Path;

@CommandLine.Command(
        name = "analyse",
        subcommands = {PaprikaQuery.class},
        description = "Run Paprika analyse on given folder")
public class PaprikaAnalyse extends PaprikaCommand {
    private static final Logger logger = LogManager.getFormatterLogger();

    @CommandLine.Option(names = {"-a", "--android"}, showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
            description = "Path to the android platform jars")
    private File androidJars = IOPath.ANDROID.asFile();

    @CommandLine.Option(names = {"-f", "--apk-folder"}, showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
            description = "Path to the apk folder")
    private File apk = IOPath.APK.asFile();

    @CommandLine.Option(names = {"-i", "--info-apk"}, showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
            description = "Path to the info apk file")
    private File info = IOPath.INFO.asFile();

    @CommandLine.Option(names = {"-db", "--database"}, description = "Path to the info Paprika db")
    private Path db = null;

    @Override
    protected void runPaprika() {
        try {
            fakie().runPaprikaAnalyse(std(), androidJars, apk, info, db);
        }
        catch (FakieException e) {
            logger.error(e);
        }
    }
}
