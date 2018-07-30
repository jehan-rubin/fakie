package com.fakie.utils.paprika;

import com.fakie.cli.Std;
import com.fakie.io.input.graphloader.Neo4j;
import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Properties;
import com.fakie.utils.exceptions.FakieException;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class PaprikaAnalyseTest {
    private File tor;
    private PaprikaAccessor paprikaAccessor;
    private String pck;
    private String apk;
    private File info;
    private Path db;
    private Path apkFolder;
    private File android;

    @Before
    public void setUp() throws Exception {
        pck = "org.torproject.android";
        apk = pck.concat(".apk");
        URL url = getClass().getClassLoader().getResource("apk");
        assert url != null : "Could not locate apk folder";
        URI uri = url.toURI();
        apkFolder = Paths.get(uri);
        info = apkFolder.resolve("info.csv").toFile();
        db = apkFolder.resolve("db");
        android = apkFolder.resolve("android").toFile();
        paprikaAccessor = new PaprikaAccessor(new Std());

        if (!android.exists()) {
            cloneAndroidJars();
        }
    }

    private void cloneAndroidJars() throws GitAPIException {
        Git git = Git.cloneRepository()
                .setURI("https://github.com/Sable/android-platforms.git")
                .setDirectory(android)
                .call();
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.deleteDirectory(db.toFile());
    }

    @Test
    public void analyseTor() throws FakieException {
        paprikaAccessor.analyse(android, apkFolder.toFile(), info, db);
        try (Neo4j neo4j = new Neo4j(db)) {
            Graph graph = neo4j.load();

            Properties properties = graph.getVertices().get(0);
            assertEquals(3, properties.getProperty("number_of_interfaces"));
            assertEquals(417, properties.getProperty("number_of_methods"));
            assertEquals(79, properties.getProperty("number_of_classes"));
            assertEquals(pck, properties.getProperty("app_key"));
        }
    }
}
