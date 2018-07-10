package com.fakie.learning.filter;

import com.fakie.learning.LearningException;
import com.fakie.learning.Rule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class FilterRedundantRule implements Filter {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public List<Rule> filter(List<Rule> rules) throws LearningException {
        logger.info("Filter redundant rules");
        Deque<Rule> temp = rules.stream()
                .sorted(this::compare)
                .collect(Collectors.toCollection(ArrayDeque::new));
        Set<Rule> filtered = new HashSet<>();
        while (!temp.isEmpty()) {
            Rule pop = temp.pop();
            for (Rule rule : new ArrayList<>(temp)) {
                boolean isBetter = isBetter(pop, rule);
                if (isBetter) {
                    temp.remove(pop);
                    pop = rule;
                }
            }
            filtered.add(pop);
        }
        return new ArrayList<>(filtered);
    }

    private boolean isBetter(Rule ruleI, Rule ruleJ) {
        boolean sameSupport = Double.compare(ruleI.getSupport(), ruleJ.getSupport()) == 0;
        boolean sameConfidence = Double.compare(ruleI.getConfidence(), ruleJ.getConfidence()) == 0;
        boolean morePremises = ruleI.premises().size() <= ruleJ.premises().size();
        boolean sameMetricButMorePremises = sameSupport && sameConfidence && morePremises;

        boolean sameConsequences = ruleI.consequences().equals(ruleJ.consequences());
        boolean betterSupport = ruleI.getSupport() <= ruleJ.getSupport();
        boolean betterConfidence = ruleI.getConfidence() <= ruleJ.getConfidence();
        boolean betterSupportAndConfidence = sameConsequences && betterSupport && betterConfidence && morePremises;

        return sameMetricButMorePremises || betterSupportAndConfidence;
    }

    private int compare(Rule a, Rule b) {
        return b.premises().size() - a.premises().size();
    }
}
