package com.fakie.utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Zipper {
    public static File unzip(String name) throws URISyntaxException, ZipException {
        URL db = Zipper.class.getClassLoader().getResource(Paths.get("db").toString());
        assert db != null : "Could not locate the db directory";
        Path path = new File(db.toURI()).toPath();
        ZipFile zipFile = new ZipFile(path.resolve(name + ".zip").toFile());
        File dir = path.resolve(name).toFile();
        zipFile.extractAll(path.toString());
        return dir;
    }
}
