package com.fakie.io.output.queryexporter;

import com.fakie.io.output.FakieOutputException;
import com.fakie.learning.Rule;
import com.fakie.utils.FakieUtils;
import com.fakie.utils.expression.BinaryOperator;
import com.fakie.utils.expression.Expression;
import com.fakie.utils.expression.UnaryOperator;
import iot.jcypher.query.JcQuery;
import iot.jcypher.query.api.IClause;
import iot.jcypher.query.api.predicate.BooleanOperation;
import iot.jcypher.query.api.predicate.Concat;
import iot.jcypher.query.api.predicate.Concatenator;
import iot.jcypher.query.api.predicate.IBeforePredicate;
import iot.jcypher.query.api.start.StartPoint;
import iot.jcypher.query.factories.clause.RETURN;
import iot.jcypher.query.factories.clause.START;
import iot.jcypher.query.factories.clause.WHERE;
import iot.jcypher.query.values.JcNode;
import iot.jcypher.query.writer.Format;
import iot.jcypher.util.Util;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

public class Cypher implements QueryExporter {
    private static final String EXT = ".cql";
    private static final Map<Expression.Type, Mapper> mapping = new EnumMap<>(Expression.Type.class);
    private static final Map<Expression.Type, Linker> linking = new EnumMap<>(Expression.Type.class);
    private static final Map<Expression.Type, BeforePredicate> before = new EnumMap<>(Expression.Type.class);

    static {
        mapping.put(Expression.Type.EQ, ((op, expression) -> op.EQUALS(expression[0].eval())));
        mapping.put(Expression.Type.NEQ, ((op, expression) -> op.NOT_EQUALS(expression[0].eval())));
        mapping.put(Expression.Type.GT, ((op, expression) -> op.GT(expression[0].eval())));
        mapping.put(Expression.Type.LT, ((op, expression) -> op.LT(expression[0].eval())));
        mapping.put(Expression.Type.IS_TRUE, ((op, expression) -> op.EQUALS(true)));
        mapping.put(Expression.Type.NOT, ((op, expression) -> op.EQUALS(false)));

        linking.put(Expression.Type.AND, Concatenator::AND);
        linking.put(Expression.Type.OR, Concatenator::OR);

        before.put(Expression.Type.IS_TRUE, (iBeforePredicate -> iBeforePredicate));
        before.put(Expression.Type.NOT, IBeforePredicate::NOT);
    }

    @Override
    public void exportRulesAsQueries(Path path, List<Rule> rules) throws FakieOutputException {
        Map<String, List<JcQuery>> queryTable = createQueryTable(rules);
        exportQueryTable(path, queryTable);
    }

    private void exportQueryTable(Path path, Map<String, List<JcQuery>> queryTable) throws FakieOutputException {
        try {
            for (Map.Entry<String, List<JcQuery>> entry : queryTable.entrySet()) {
                for (int i = 0; i < entry.getValue().size(); i++) {
                    saveFile(i, path, entry.getKey(), entry.getValue().get(i));
                }
            }
        }
        catch (IOException e) {
            throw new FakieOutputException(e);
        }
    }

    private void saveFile(int i, Path path, String codeSmell, JcQuery query) throws IOException, FakieOutputException {
        File dir = path.resolve(codeSmell).toFile();
        if (!dir.exists() && !dir.mkdirs()) {
            throw new FakieOutputException("Could not create directory \'" + dir + "\'");
        }
        File file = dir.toPath().resolve(FakieUtils.uniqueName().concat(" " + i).concat(EXT)).toFile();
        if (!file.exists() && !file.createNewFile()) {
            throw new FakieOutputException("Could not create file \'" + file + "\'");
        }
        try (PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            out.println(formatQuery(query));
        }
    }

    private Map<String, List<JcQuery>> createQueryTable(List<Rule> rules) {
        Map<String, List<JcQuery>> queries = new HashMap<>();
        for (Rule rule : rules) {
            convertRuleToQueryAndAddItToQueries(queries, rule);
        }
        return queries;
    }

    private void convertRuleToQueryAndAddItToQueries(Map<String, List<JcQuery>> queries, Rule rule) {
        JcQuery jcQuery = buildQuery(rule.premises());
        for (Expression expression : rule.consequences().depthFirstChildren()) {
            if (FakieUtils.isACodeSmell(expression)) {
                String key = expression.eval().toString();
                queries.putIfAbsent(key, new ArrayList<>());
                queries.get(key).add(jcQuery);
            }
        }
    }

    private JcQuery buildQuery(Expression premises) {
        JcQuery jcQuery = new JcQuery();
        JcNode n = new JcNode("n");
        List<IClause> clauses = new ArrayList<>();
        StartPoint start = START.node(n).all();
        clauses.add(start);
        Concat concat = WHERE.BR_OPEN();
        Concatenator concatenator = concatExpression(n, premises, concat);
        clauses.add(concatenator.BR_CLOSE());
        clauses.add(RETURN.value(n));
        jcQuery.setClauses(clauses.toArray(new IClause[0]));
        return jcQuery;
    }

    private Concatenator concatExpression(JcNode n, Expression expression, IBeforePredicate concat) {
        if (expression.getType().isBinaryOperator()) {
            BinaryOperator op = expression.cast(BinaryOperator.class);
            Expression left = op.getLeft();
            Expression right = op.getRight();
            if (left.getType().isVariable() && right.getType().isVariable()) {
                BooleanOperation booleanOperation = concat.valueOf(n.property(left.eval().toString()));
                return mapping.get(expression.getType()).map(booleanOperation, right);
            } else {
                Concatenator l = concatExpression(n, left, concat);
                Concat link = linking.get(expression.getType()).link(l);
                return concatExpression(n, right, link);
            }
        } else if (expression.getType().isUnaryOperator()) {
            UnaryOperator op = expression.cast(UnaryOperator.class);
            Expression exp = op.getExpression();
            if (exp.getType().isVariable()) {
                BooleanOperation booleanOperation = concat.valueOf(n.property(exp.eval().toString()));
                return mapping.get(expression.getType()).map(booleanOperation);
            } else {
                return concatExpression(n, exp, before.get(exp.getType()).apply(concat));
            }
        }
        return concat.has(n.property(expression.eval().toString()));
    }

    private String formatQuery(JcQuery query) {
        return Util.toCypher(query, Format.PRETTY_3);
    }

    private interface Mapper {
        Concatenator map(BooleanOperation op, Expression... expression);
    }

    private interface Linker {
        Concat link(Concatenator concatenator);
    }

    private interface BeforePredicate extends Function<IBeforePredicate, IBeforePredicate> {}
}
