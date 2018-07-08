package com.fakie.io.input.apk;

import com.fakie.io.input.FakieInputException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class CSVInfoReaderTest {
    private String apk;
    private Path apkFolder;
    private File info;

    @Before
    public void setUp() throws Exception {
        apk = "org.torproject.android.apk";
        URL url = getClass().getClassLoader().getResource("apk");
        assert url != null : "Could not locate apk folder";
        URI uri = url.toURI();
        apkFolder = Paths.get(uri);
        info = apkFolder.resolve("info.csv").toFile();
    }

    @Test
    public void torInfo() throws FakieInputException {
        APKInfo apkInfo = new CSVInfoReader().readInfo(info);
        APKInfo.Entry entry = apkInfo.get(apk);
        assertEquals("Orbot", entry.getName());
        assertEquals("org.torproject.android", entry.getPkg());
    }
}