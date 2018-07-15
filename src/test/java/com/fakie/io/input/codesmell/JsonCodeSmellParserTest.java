package com.fakie.io.input.codesmell;

import com.fakie.io.input.FakieInputException;
import com.fakie.model.processor.CodeSmell;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class JsonCodeSmellParserTest {
    @Test
    public void parseExampleJson() throws FakieInputException, URISyntaxException {
        URL resource = getClass().getClassLoader().getResource("codesmell/codesmells.json");
        assert resource != null : "Could not locate resource folder";
        File file = new File(resource.toURI());
        CodeSmells parse = new JsonCodeSmellParser().parse(file);
        CodeSmells expected = CodeSmells.createIndex();
        expected.add(new CodeSmell(
                Collections.singletonList("Class"),
                Collections.singletonMap("name", "Main"),
                "God Class"));
        assertEquals(expected, parse);
    }
}