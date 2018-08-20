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

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CypherQuery {
    private static final Map<Expression.Type, Mapper> mapping = new EnumMap<>(Expression.Type.class);
    private static final Map<Expression.Type, Linker> linking = new EnumMap<>(Expression.Type.class);
    private static final Map<Expression.Type, BeforePredicate> before = new EnumMap<>(Expression.Type.class);
    private static int counter = 0;

    static {
        mapping.put(Expression.Type.EQ, ((op, expression) -> op.EQUALS(expression[0].eval())));
        mapping.put(Expression.Type.NEQ, ((op, expression) -> op.NOT_EQUALS(expression[0].eval())));
        mapping.put(Expression.Type.GT, ((op, expression) -> op.GTE(expression[0].eval())));
        mapping.put(Expression.Type.LT, ((op, expression) -> op.LTE(expression[0].eval())));
        mapping.put(Expression.Type.IS_TRUE, ((op, expression) -> op.EQUALS(true)));
        mapping.put(Expression.Type.NOT, ((op, expression) -> op.EQUALS(false)));

        linking.put(Expression.Type.AND, Concatenator::AND);
        linking.put(Expression.Type.OR, Concatenator::OR);

        before.put(Expression.Type.IS_TRUE, (iBeforePredicate -> iBeforePredicate));
        before.put(Expression.Type.NOT, IBeforePredicate::NOT);
    }

    private final Rule rule;
    private final String codesmell;
    private final JcQuery jcQuery = new JcQuery();
    private final int id;

    public CypherQuery(Rule rule) throws FakieOutputException {
        this.rule = rule;
        this.codesmell = extractCodeSmell(rule);
        this.id = counter++;
        buildQuery();
    }

    public String getCodesmell() {
        return codesmell;
    }

    public int getId() {
        return id;
    }

    private void buildQuery() {
        JcNode n = new JcNode("n");
        List<IClause> clauses = new ArrayList<>();
        StartPoint start = START.node(n).all();
        clauses.add(start);
        if (!rule.premises().getType().isNone()){
            Concat concat = WHERE.BR_OPEN();
            Concatenator concatenator = concatExpression(n, rule.premises(), concat);
            clauses.add(concatenator.BR_CLOSE());
        }
        clauses.add(RETURN.value(n.property("name")));
        jcQuery.setClauses(clauses.toArray(new IClause[0]));
    }

    private static String extractCodeSmell(Rule rule) throws FakieOutputException {
        for (Expression expression : rule.consequences().depthFirstChildren()) {
            if (FakieUtils.isACodeSmell(expression)) {
                return expression.eval().toString();
            }
        }
        throw new FakieOutputException("Rule " + rule + " does not contain any codesmell");
    }

    public String export() {
        return header() + '\n' + formatQuery(jcQuery);
    }

    private String header() {
        return "// Confidence : " + rule.getConfidence() + ", Support : " + rule.getSupport();
    }

    private static String formatQuery(JcQuery query) {
        return Util.toCypher(query, Format.PRETTY_3);
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
                return concatExpression(n, exp, before.get(expression.getType()).apply(concat));
            }
        }
        return concat.has(n.property(expression.eval().toString()));
    }

    private interface Mapper {
        Concatenator map(BooleanOperation op, Expression... expression);
    }

    private interface Linker {
        Concat link(Concatenator concatenator);
    }

    private interface BeforePredicate extends Function<IBeforePredicate, IBeforePredicate> {}
}
