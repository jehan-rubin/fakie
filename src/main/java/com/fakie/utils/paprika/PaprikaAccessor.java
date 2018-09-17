package com.fakie.utils.paprika;

import com.fakie.cli.Std;
import com.fakie.utils.Keyword;
import com.fakie.utils.exceptions.FakieException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import paprika.launcher.PaprikaLauncher;
import paprika.launcher.arg.PaprikaArgException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PaprikaAccessor {
    private static final Logger logger = LogManager.getFormatterLogger();
    private static final String[] EXT = new String[]{"apk"};
    private final Std std;

    public PaprikaAccessor(Std std) {
        this.std = std;
    }

    public void analyse(File androidJars, File apk, File info, Path db) throws FakieException {
        try {
            String[] arguments = {"analyse",
                    apk.toPath().toString(),
                    "-a", androidJars.toPath().toString(),
                    "-db", db.toString()
            };
            PaprikaLauncher paprikaLauncher = new PaprikaLauncher(arguments, std.out());
            paprikaLauncher.startPaprika();
        }
        catch (PaprikaArgException e) {
            throw new PaprikaException(e);
        }
    }

    public Path query(Path db, String suffix) throws PaprikaException {
        Path path = createQueryFolder(suffix);
        try {
            String[] arguments = {"query",
                    "-db", db.toString(),
                    "-r", "ALLAP",
                    "-c", suffix
            };
            PaprikaLauncher paprikaLauncher = new PaprikaLauncher(arguments, std.out());
            paprikaLauncher.startPaprika();
        }
        catch (PaprikaArgException e) {
            throw new PaprikaException(e);
        }
        return path;
    }

    public Path customQuery(Path db, String suffix, String query) throws PaprikaException{
        Path path = createQueryFolder(suffix);
        try {
            String[] arguments = {"query",
                    "-db", db.toString(),
                    "-r", query,
                    "-c", suffix
            };
            PaprikaLauncher paprikaLauncher = new PaprikaLauncher(arguments, std.out());
            paprikaLauncher.startPaprika();
        }
        catch (PaprikaArgException e) {
            throw new PaprikaException(e);
        }
        return path;
    }

    private Path createQueryFolder(String suffix) throws PaprikaException {
        Path path = Paths.get(suffix.concat(Keyword.CODE_SMELL.toString())).toAbsolutePath().getParent();
        File file = path.toFile();
        if (!file.exists() && !file.mkdirs()) {
            throw new PaprikaException("Could not create folder \'" + path + "\'");
        }
        return path;
    }
}
