package com.fakie.learning.filter;

import com.fakie.learning.Rule;
import com.fakie.utils.expression.Expression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ManyToOne implements Filter {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public List<Rule> filter(List<Rule> rules) {
        logger.info("Keep only many to one rules (%d)", rules.size());
        List<Rule> filtered = new ArrayList<>();
        for (Rule rule : rules) {
            addOnlyManyToOneRule(filtered, rule);
        }
        return filtered;
    }

    private void addOnlyManyToOneRule(List<Rule> filtered, Rule rule) {
        Expression consequences = rule.consequences();
        if (consequences.variables() == 2) {
            filtered.add(rule);
        }
    }
}
