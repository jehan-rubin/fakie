package com.fakie.utils.paprika;

import com.fakie.cli.Std;
import com.fakie.io.input.FakieInputException;
import com.fakie.io.input.codesmell.CodeSmells;
import com.fakie.io.input.codesmell.PaprikaDetectionParser;
import com.fakie.utils.Zipper;
import net.lingala.zip4j.exception.ZipException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.io.fs.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

public class PaprikaQueryTest {
    private static final String NAME = "tor";
    private File dir;

    @Before
    public void setUp() throws URISyntaxException, ZipException {
        dir = Zipper.unzip(NAME);
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.deletePathRecursively(dir.toPath());
    }

    @Test
    public void torWithOnlyBlobCodeSmells() throws FakieInputException, PaprikaException {
        new PaprikaAccessor(new Std()).query(dir.toPath(), dir.toPath().toString().concat("/"));
        CodeSmells parse = new PaprikaDetectionParser().parse(dir.toPath().resolve("_BLOB_NO_FUZZY.csv").toFile());
        assertEquals(2, parse.size());
        parse = new PaprikaDetectionParser().parse(dir);
        assertEquals(105, parse.size());
    }
}