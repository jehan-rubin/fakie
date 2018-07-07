package com.fakie.utils.paprika;

import com.fakie.io.input.FakieInputException;
import com.fakie.io.input.apk.APKInfo;
import com.fakie.io.input.apk.CSVInfoReader;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import paprika.analyzer.SootAnalyzer;
import paprika.entities.PaprikaApp;
import paprika.neo4j.ModelToGraph;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class PaprikaAccessor {
    private static final Logger logger = LogManager.getFormatterLogger();
    private static final String[] EXT = new String[]{"apk"};
    private final File androidJars;
    private final File apk;
    private final File info;

    public PaprikaAccessor(File androidJars, File apk, File info) {
        this.androidJars = androidJars;
        this.apk = apk;
        this.info = info;
    }

    public void analyse(Path db) throws FakieInputException {
        APKInfo apkInfo = new CSVInfoReader().readInfo(info);
        ModelToGraph modelToGraph = new ModelToGraph(db.toString());
        if (apk.isDirectory()) {
            analyseDirectory(apk, db, apkInfo, modelToGraph);
        }
        else {
            analyseApk(apk, db, apkInfo.get(apk.getName()), modelToGraph);
        }
    }

    private void analyseApk(File file, Path db, APKInfo.Entry entry, ModelToGraph modelToGraph) {
        try {
            logger.info("Paprika analyse %s", file);
            SootAnalyzer analyzer = new SootAnalyzer(
                    file.toPath().toString(),
                    androidJars.toPath().toString(),
                    entry.getName(), entry.getName(), entry.getPkg(),
                    date(), 1, "default-developer", "default-category",
                    "Free", 1, "0", "", "", "", "", true
            );
            analyzer.init();
            analyzer.runAnalysis();
            logger.info("Saving Paprika analysis into %s", db);
            PaprikaApp paprikaApp = analyzer.getPaprikaApp();
            logger.debug("Creating graph for %s", paprikaApp);
            modelToGraph.insertApp(paprikaApp);
            logger.info("Successfully saved Paprika db");
        }
        catch (Exception e) {
            logger.error(e);
        }
    }

    private void analyseDirectory(File dir, Path db, APKInfo apkInfo, ModelToGraph modelToGraph) {
        logger.info("Paprika analysing apk in the %s folder", dir);
        Collection<File> files = FileUtils.listFiles(dir, EXT, false);
        for (File file : files) {
            analyseApk(file, db, apkInfo.get(file.getName()), modelToGraph);
        }
        logger.info("Paprika successfully analyzed the apk folder %s", dir);
    }

    private String date() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.n");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
