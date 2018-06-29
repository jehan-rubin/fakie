package com.fakie.learning.association;

import com.fakie.learning.Algorithm;
import com.fakie.learning.LearningException;
import com.fakie.learning.Rule;
import com.fakie.logic.And;
import com.fakie.logic.Expression;
import com.fakie.logic.Implication;
import org.junit.Test;
import weka.associations.FPGrowth;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.assertTrue;

public class AssociationTest {
    @Test
    public void blobRule() throws URISyntaxException, LearningException {
        URL iris = getClass().getClassLoader().getResource("arff/wikipedia.arff");
        assert iris != null : "Could not find wikipedia data set";
        InstancesCreator instancesCreator = new ARFFReader(Paths.get(iris.toURI()));
        FPGrowth fpGrowth = new FPGrowth();
        Algorithm algorithm = new Association(instancesCreator, fpGrowth, fpGrowth);
        List<Rule> rules = algorithm.generateRules();

        And left = new And(Collections.singletonList(new Expression("number_of_methods_greater_than_40", true)));
        And right = new And(Collections.singletonList(new Expression("blob", true)));
        Implication implication = new Implication(left, right);

        Rule expectedRule = new Rule(implication, 0.9230769230769231);
        assertTrue(rules.contains(expectedRule));
    }
}