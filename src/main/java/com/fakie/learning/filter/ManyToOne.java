package com.fakie.learning.filter;

import com.fakie.learning.Rule;
import com.fakie.utils.logic.Operator;

import java.util.ArrayList;
import java.util.List;

public class ManyToOne implements Filter {
    @Override
    public List<Rule> filter(List<Rule> rules) {
        List<Rule> filtered = new ArrayList<>();
        for (Rule rule : rules) {
            addOnlyManyToOneRule(filtered, rule);
        }
        return filtered;
    }

    private void addOnlyManyToOneRule(List<Rule> filtered, Rule rule) {
        Operator consequences = rule.consequences();
       if (consequences.size() == 1) {
           filtered.add(rule);
       }
    }
}
