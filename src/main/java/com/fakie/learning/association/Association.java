package com.fakie.learning.association;

import com.fakie.learning.Algorithm;
import com.fakie.learning.LearningException;
import com.fakie.learning.Rule;
import com.fakie.logic.And;
import com.fakie.logic.Expression;
import com.fakie.logic.Implication;
import weka.associations.*;

import java.util.ArrayList;
import java.util.List;

public class Association implements Algorithm {
    private final InstancesCreator instancesCreator;
    private final Associator associator;
    private final AssociationRulesProducer producer;

    public Association(InstancesCreator instancesCreator, Associator associator, AssociationRulesProducer producer) {
        this.instancesCreator = instancesCreator;
        this.associator = associator;
        this.producer = producer;
    }

    public List<Rule> generateRules() throws LearningException {
        try {
            associator.buildAssociations(instancesCreator.createInstances());
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
