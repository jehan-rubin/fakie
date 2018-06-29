package com.fakie.learning.association;

import com.fakie.io.input.dataset.DatasetHolder;
import com.fakie.learning.Algorithm;
import com.fakie.learning.LearningException;
import com.fakie.learning.Rule;
import com.fakie.utils.logic.And;
import com.fakie.utils.logic.Expression;
import com.fakie.utils.logic.Implication;
import weka.associations.*;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;

public class Association implements Algorithm {
    private final DatasetHolder<Instances> holder;
    private final Associator associator;
    private final AssociationRulesProducer producer;

    public Association(DatasetHolder<Instances> holder, Associator associator, AssociationRulesProducer producer) {
        this.holder = holder;
        this.associator = associator;
        this.producer = producer;
    }

    public List<Rule> generateRules() throws LearningException {
        try {
            associator.buildAssociations(holder.getDataset());
            return produceRules();
        }
        catch (Exception e) {
            throw new LearningException(e);
        }
    }

    private List<Rule> produceRules() {
        List<Rule> rules = new ArrayList<>();
        if (producer.canProduceRules()) {
            AssociationRules associationRules = producer.getAssociationRules();
            for (AssociationRule associationRule : associationRules.getRules()) {
                rules.add(buildRuleFromAssociationRule(associationRule));
            }
        }
        return rules;
    }

    private Rule buildRuleFromAssociationRule(AssociationRule associationRule) {
        List<Expression> expressions = new ArrayList<>();
        for (Item item : associationRule.getPremise()) {
            expressions.add(new Expression(item.getAttribute().name(), Boolean.valueOf(item.getItemValueAsString())));
        }
        And left = new And(expressions);
        expressions.clear();
        for (Item item : associationRule.getConsequence()) {
            expressions.add(new Expression(item.getAttribute().name(), Boolean.valueOf(item.getItemValueAsString())));
        }
        And right = new And(expressions);
        return new Rule(new Implication(left, right), associationRule.getPrimaryMetricValue());
    }
}
