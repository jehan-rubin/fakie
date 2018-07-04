package com.fakie.io.input.codesmell;

import com.fakie.io.input.FakieInputException;
import com.fakie.model.processor.CodeSmell;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class JsonCodeSmellParserTest {
    @Test
    public void name() throws FakieInputException, URISyntaxException {
        URL resource = getClass().getClassLoader().getResource("codesmell/codesmells.json");
        assert resource != null : "Could not locate resource folder";
        File file = new File(resource.toURI());
        List<CodeSmell> parse = new JsonCodeSmellParser().parse(file);
        assertEquals(1, parse.size());
        CodeSmell expected = new CodeSmell(
                Collections.singletonList("Class"),
                Collections.singletonMap("name", "Main"),
                "God Class");
        assertEquals(expected, parse.get(0));
    }
}