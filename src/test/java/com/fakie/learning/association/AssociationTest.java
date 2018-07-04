package com.fakie.learning.association;

import com.fakie.io.input.dataset.ARFFReader;
import com.fakie.learning.Algorithm;
import com.fakie.learning.Rule;
import com.fakie.utils.exceptions.FakieException;
import com.fakie.utils.logic.And;
import com.fakie.utils.logic.Expression;
import com.fakie.utils.logic.Implication;
import org.junit.Test;
import weka.associations.Apriori;
import weka.associations.FPGrowth;
import weka.core.Instances;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class AssociationTest {
    @Test
    public void fpgrowth() throws URISyntaxException, FakieException {
        URL iris = getClass().getClassLoader().getResource("arff/wikipedia.arff");
        assert iris != null : "Could not find wikipedia data set";
        ARFFReader arffReader = new ARFFReader();
        Instances dataset = arffReader.readDataset(Paths.get(iris.toURI()));
        FPGrowth fpGrowth = new FPGrowth();
        Algorithm algorithm = new Association(dataset, fpGrowth, fpGrowth);
        List<Rule> rules = algorithm.generateRules();

        And left = new And(Collections.singletonList(new Expression("number_of_methods_greater_than_40", true)));
        And right = new And(Collections.singletonList(new Expression("CODE_SMELL_BLOB", true)));
        Implication implication = new Implication(left, right);

        Rule expectedRule = new Rule(implication, 12);
        assertTrue(rules.contains(expectedRule));
    }

    @Test
    public void apriori() throws URISyntaxException, FakieException {
        URL iris = getClass().getClassLoader().getResource("arff/wikipedia.arff");
        assert iris != null : "Could not find wikipedia data set";
        ARFFReader arffReader = new ARFFReader();
        Instances dataset = arffReader.readDataset(Paths.get(iris.toURI()));
        Apriori apriori = new Apriori();
        Algorithm algorithm = new Association(dataset, apriori, apriori);
        List<Rule> rules = algorithm.generateRules();

        And left = new And(Collections.singletonList(new Expression("number_of_methods_greater_than_40", true)));
        And right = new And(Collections.singletonList(new Expression("CODE_SMELL_BLOB", true)));
        Implication implication = new Implication(left, right);

        Rule expectedRule = new Rule(implication, 12);
        assertTrue(rules.contains(expectedRule));
    }
}