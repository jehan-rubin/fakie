package com.fakie.learning.filter;

import com.fakie.learning.LearningException;
import com.fakie.learning.Rule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimplifyExpression implements Filter {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public List<Rule> filter(List<Rule> rules) throws LearningException {
        logger.info("Simplify the rules (%d)", rules.size());
        Set<Rule> result = new HashSet<>();
        for (Rule rule : rules) {
            result.add(new Rule(rule.premises().simplify().imply(rule.consequences().simplify()),
                    rule.getSupport(), rule.getConfidence()));
        }
        return new ArrayList<>(result);
    }
}
