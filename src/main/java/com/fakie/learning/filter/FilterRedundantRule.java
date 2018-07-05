package com.fakie.learning.filter;

import com.fakie.learning.LearningException;
import com.fakie.learning.Rule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterRedundantRule implements Filter {
    @Override
    public List<Rule> filter(List<Rule> rules) throws LearningException {
        Set<Rule> filtered = new HashSet<>();
        for (int i = 0; i < rules.size() - 1; i++) {
            Rule ruleI = rules.get(i);
            for (int j = i + 1; j < rules.size(); j++) {
                Rule ruleJ = rules.get(j);
                if (ruleI.getSupport() == ruleJ.getSupport() && ruleI.premises().size() < ruleJ.premises().size()) {
                    ruleI = ruleJ;
                }
            }
            filtered.add(ruleI);
        }
        return new ArrayList<>(filtered);
    }
}
