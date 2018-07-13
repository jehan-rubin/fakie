package com.fakie.io.input.codesmell;

import com.fakie.io.input.FakieInputException;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class PaprikaDetectionParserTest {
    @Test
    public void parseBlobCSV() throws URISyntaxException, FakieInputException {
        URL dir = getClass().getClassLoader().getResource("codesmell/2015_9_15_16_19_BLOB.csv");
        assert dir != null : "Directory \'codesmell\' could not be located";
        URI uri = dir.toURI();
        File resource = new File(uri);
        PaprikaDetectionParser paprikaDetectionParser = new PaprikaDetectionParser();
        CodeSmells codeSmells = paprikaDetectionParser.parse(resource);
        assertEquals(139, codeSmells.size());
    }

    @Test
    public void parseCSVFolder() throws URISyntaxException, FakieInputException {
        URL dir = getClass().getClassLoader().getResource("codesmell");
        assert dir != null : "Directory \'codesmell\' could not be located";
        URI uri = dir.toURI();
        File resource = new File(uri);
        PaprikaDetectionParser paprikaDetectionParser = new PaprikaDetectionParser();
        CodeSmells codeSmells = paprikaDetectionParser.parse(resource);
        assertEquals(139 + 4, codeSmells.size());
    }
}