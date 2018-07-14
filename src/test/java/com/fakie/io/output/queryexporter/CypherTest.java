package com.fakie.io.output.queryexporter;

import com.fakie.io.IOPath;
import com.fakie.io.output.FakieOutputException;
import com.fakie.learning.Rule;
import com.fakie.utils.expression.Expression;
import com.fakie.utils.expression.Implication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.io.fs.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class CypherTest {
    private Path path;

    @Before
    public void setUp() throws URISyntaxException {
        Path cypherPath = IOPath.CYPHER_FOLDER.asPath();
        URL url = getClass().getClassLoader().getResource(".");
        assert url != null : "Could not locate resources folder";
        path = new File(url.toURI()).toPath().resolve(cypherPath);
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.deleteRecursively(path.toFile());
    }

    @Test
    public void createQueries() throws FakieOutputException, IOException {
        Cypher cypher = new Cypher();

        Implication implication = Expression.of("number_of_methods_greater_than_40").eq(true)
                .or(Expression.of("number_of_methods_between_than_30_and_39").eq(true))
                .imply(Expression.of("CODE_SMELL_BLOB").eq(true));

        URL db = getClass().getClassLoader().getResource(".");
        assert db != null : "Could not locate the resources directory";
        Rule expectedRule = new Rule(implication, 1, 1);
        cypher.exportRulesAsQueries(path, Collections.singletonList(expectedRule));
        assertEquals(1, FileUtils.countFilesInDirectoryPath(path.resolve("CODE_SMELL_BLOB")));
    }
}