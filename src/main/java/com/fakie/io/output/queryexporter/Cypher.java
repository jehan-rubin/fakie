package com.fakie.io.output.queryexporter;

import com.fakie.io.output.FakieOutputException;
import com.fakie.learning.Rule;
import com.fakie.utils.FakieUtils;
import com.fakie.utils.expression.Expression;
import iot.jcypher.query.JcQuery;
import iot.jcypher.query.api.IClause;
import iot.jcypher.query.api.start.StartPoint;
import iot.jcypher.query.factories.clause.RETURN;
import iot.jcypher.query.factories.clause.START;
import iot.jcypher.query.values.JcNode;
import iot.jcypher.query.writer.Format;
import iot.jcypher.util.Util;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cypher implements QueryExporter {
    private static final String EXT = ".cql";

    @Override
    public void exportRulesAsQueries(Path path, List<Rule> rules) throws FakieOutputException {
        Map<String, JcQuery> queryTable = createQueryTable(rules);
        exportQueryTable(path, queryTable);
    }

    private void exportQueryTable(Path path, Map<String, JcQuery> queryTable) throws FakieOutputException {
        try {
            for (Map.Entry<String, JcQuery> entry : queryTable.entrySet()) {
                createQuery(path, entry.getKey(), entry.getValue());
            }
        }
        catch (IOException e) {
            throw new FakieOutputException(e);
        }
    }

    private void createQuery(Path path, String codeSmell, JcQuery query) throws IOException, FakieOutputException {
        File dir = path.resolve(codeSmell).toFile();
        if (!dir.exists() && !dir.mkdirs()) {
            throw new FakieOutputException("Could not create directory \'" + dir + "\'");
        }
        File file = dir.toPath().resolve(FakieUtils.uniqueName().concat(EXT)).toFile();
        if (!file.exists() && !file.createNewFile()) {
            throw new FakieOutputException("Could not create file \'" + file + "\'");
        }
        try (PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            out.println(formatQuery(query));
        }
    }

    private Map<String, JcQuery> createQueryTable(List<Rule> rules) {
        Map<String, JcQuery> queries = new HashMap<>();
        for (Rule rule : rules) {
            convertRuleToQueryAndAddItToQueries(queries, rule);
        }
        return queries;
    }

    private void convertRuleToQueryAndAddItToQueries(Map<String, JcQuery> queries, Rule rule) {
        JcQuery jcQuery = buildQuery(rule.premises());
        for (Expression expression : rule.consequences().depthFirstChildren()) {
            if (FakieUtils.isACodeSmell(expression)) {
                queries.put(expression.eval().toString(), jcQuery);
            }
        }
    }

    private JcQuery buildQuery(Expression premises) {
        JcQuery jcQuery = new JcQuery();
        JcNode n = new JcNode("n");
        List<IClause> clauses = new ArrayList<>();
        StartPoint start = START.node(n).all();
        clauses.add(start);
        clauses.add(RETURN.value(n));
        jcQuery.setClauses(clauses.toArray(new IClause[0]));
        return jcQuery;
    }

    private String formatQuery(JcQuery query) {
        return Util.toCypher(query, Format.PRETTY_3);
    }
}
