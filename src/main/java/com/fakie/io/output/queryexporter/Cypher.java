package com.fakie.io.output.queryexporter;

import com.fakie.io.output.FakieOutputException;
import com.fakie.learning.Rule;
import com.fakie.utils.FakieUtils;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

public class Cypher implements QueryExporter {
    private static final String EXT = ".cql";

    @Override
    public void exportRulesAsQueries(Path path, List<Rule> rules) throws FakieOutputException {
        for (Rule rule : rules) {
            try {
                save(path, new CypherQuery(rule));
            }
            catch (IOException e) {
                throw new FakieOutputException(e);
            }
        }
    }

    private void save(Path path, CypherQuery cypherQuery) throws FakieOutputException, IOException {
        File dir = path.resolve(cypherQuery.getCodesmell()).toFile();
        if (!dir.exists() && !dir.mkdirs()) {
            throw new FakieOutputException("Could not create directory \'" + dir + "\'");
        }
        File file = dir.toPath().resolve(FakieUtils.uniqueName().concat(" " + cypherQuery.getId()).concat(EXT))
                .toFile();
        if (!file.exists() && !file.createNewFile()) {
            throw new FakieOutputException("Could not create file \'" + file + "\'");
        }
        try (PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            out.println(cypherQuery.export());
        }
    }
}
