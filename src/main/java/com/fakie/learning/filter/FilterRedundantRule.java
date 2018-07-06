package com.fakie.learning.filter;

import com.fakie.learning.LearningException;
import com.fakie.learning.Rule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterRedundantRule implements Filter {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public List<Rule> filter(List<Rule> rules) throws LearningException {
        logger.info("Filter redundant rules");
        Set<Rule> filtered = new HashSet<>();
        for (Rule ruleI : rules) {
            for (Rule ruleJ : rules) {
                ruleI = pickBest(ruleI, ruleJ);
            }
            filtered.add(ruleI);
        }
        return new ArrayList<>(filtered);
    }

    private Rule pickBest(Rule ruleI, Rule ruleJ) {
        boolean sameSupport = Double.compare(ruleI.getSupport(), ruleJ.getSupport()) == 0;
        boolean sameConfidence = Double.compare(ruleI.getConfidence(), ruleJ.getConfidence()) == 0;
        boolean morePremises = ruleI.premises().size() <= ruleJ.premises().size();
        boolean sameMetricButMorePremises = sameSupport && sameConfidence && morePremises;

        boolean sameConsequences = ruleI.consequences().equals(ruleJ.consequences());
        boolean betterSupport = ruleI.getSupport() <= ruleJ.getSupport();
        boolean betterConfidence = ruleI.getConfidence() <= ruleJ.getConfidence();
        boolean betterSupportAndConfidence = sameConsequences && betterSupport && betterConfidence && morePremises;

        boolean isBetter = sameMetricButMorePremises || betterSupportAndConfidence;
        return isBetter ? ruleJ : ruleI;
    }
}
