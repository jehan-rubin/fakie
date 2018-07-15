package com.fakie.learning.association;

import com.fakie.learning.Algorithm;
import com.fakie.learning.LearningException;
import com.fakie.learning.Rule;
import com.fakie.utils.expression.Expression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weka.associations.*;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;

public class Association implements Algorithm {
    private static final Logger logger = LogManager.getFormatterLogger();
    private static final int MIN_INSTANCES = 2;
    private final Instances dataset;
    private final Associator associator;
    private final AssociationRulesProducer producer;

    Association(Instances dataset, Associator associator, AssociationRulesProducer producer) {
        this.dataset = dataset;
        this.associator = associator;
        this.producer = producer;
    }

    @Override
    public List<Rule> generateRules() throws LearningException {
        logger.info("Generating rules from the " + producer.getClass().getSimpleName() + " algorithm");
        if (dataset.numInstances() < MIN_INSTANCES) {
            throw new LearningException("Not enough instances in the dataset to apply the association algorithm");
        }
        try {
            associator.buildAssociations(dataset);
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
        Expression left = Expression.empty();
        for (Item item : associationRule.getPremise()) {
            String key = item.getAttribute().name();
            Boolean value = Boolean.valueOf(item.getItemValueAsString());
            left = left.and(Expression.of(key).eq(value));
        }
        Expression right = Expression.empty();
        for (Item item : associationRule.getConsequence()) {
            String key = item.getAttribute().name();
            Boolean value = Boolean.valueOf(item.getItemValueAsString());
            right = right.and(Expression.of(key).eq(value));
        }
        double support = associationRule.getTotalSupport() * 1.0 / dataset.numInstances();
        double confidence = associationRule.getPrimaryMetricValue();
        return new Rule(left.imply(right), support, confidence);
    }
}
