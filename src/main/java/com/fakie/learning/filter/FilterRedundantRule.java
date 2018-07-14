package com.fakie.learning.filter;

import com.fakie.learning.Rule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class FilterRedundantRule implements Filter {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public List<Rule> filter(List<Rule> rules) {
        logger.info("Filter redundant rules (%d)", rules.size());
        Deque<Rule> temp = rules.stream()
                .sorted(this::compare)
                .collect(Collectors.toCollection(ArrayDeque::new));
        Set<Rule> filtered = new HashSet<>();
        while (!temp.isEmpty()) {
            Rule pop = temp.pop();
            for (Rule rule : new ArrayList<>(temp)) {
                if (isBetter(pop, rule)) {
                    temp.remove(pop);
                    pop = rule;
                } else if (isBetter(rule, pop)) {
                    temp.remove(rule);
                }
            }
            filtered.add(pop);
        }
        return new ArrayList<>(filtered);
    }

    private boolean isBetter(Rule ruleI, Rule ruleJ) {
        boolean sameConsequences = ruleI.consequences().equals(ruleJ.consequences());
        boolean sameSupport = Double.compare(ruleI.getSupport(), ruleJ.getSupport()) == 0;
        boolean sameConfidence = Double.compare(ruleI.getConfidence(), ruleJ.getConfidence()) == 0;
        boolean morePremises = ruleI.premises().size() <= ruleJ.premises().size();
        boolean sameMetricButMorePremises = sameConsequences && sameSupport && sameConfidence && morePremises;

        boolean betterSupport = Double.compare(ruleI.getSupport(), ruleJ.getSupport()) <= 0;
        boolean betterConfidence = Double.compare(ruleI.getConfidence(), ruleJ.getConfidence()) <= 0;
        boolean samePremises = ruleI.premises().equals(ruleJ.premises());
        boolean betterSupportAndConfidence = sameConsequences && betterSupport && betterConfidence && samePremises;

        return sameMetricButMorePremises || betterSupportAndConfidence;
    }

    private int compare(Rule a, Rule b) {
        return b.premises().variables() - a.premises().variables();
    }
}
