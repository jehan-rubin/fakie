package com.fakie.learning.association;

import com.fakie.io.input.dataset.ARFFReader;
import com.fakie.learning.Algorithm;
import com.fakie.learning.Rule;
import com.fakie.utils.exceptions.FakieException;
import com.fakie.utils.expression.Expression;
import com.fakie.utils.expression.Implication;
import org.junit.Test;
import weka.associations.Apriori;
import weka.associations.FPGrowth;
import weka.core.Instances;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class AssociationTest {
    @Test
    public void fpgrowth() throws URISyntaxException, FakieException {
        URL wikipedia = getClass().getClassLoader().getResource("arff/wikipedia.arff");
        assert wikipedia != null : "Could not find wikipedia data set";
        ARFFReader arffReader = new ARFFReader();
        Instances dataset = arffReader.readDataset(Paths.get(wikipedia.toURI()));
        FPGrowth fpGrowth = new FPGrowth();
        Algorithm algorithm = new Association(dataset, fpGrowth, fpGrowth);
        List<Rule> rules = algorithm.generateRules();

        Implication implication = Expression.empty().and(Expression.of("CODE_SMELL_BLOB").eq(false))
                .imply(Expression.empty().and(Expression.of("number_of_methods_greater_than_40").eq(false)));

        Rule expectedRule = new Rule(implication, 0.6666666666666666, 0.9230769230769231);
        assertTrue(rules.contains(expectedRule));
    }

    @Test
    public void apriori() throws URISyntaxException, FakieException {
        URL wikipedia = getClass().getClassLoader().getResource("arff/wikipedia.arff");
        assert wikipedia != null : "Could not find wikipedia data set";
        ARFFReader arffReader = new ARFFReader();
        Instances dataset = arffReader.readDataset(Paths.get(wikipedia.toURI()));
        Apriori apriori = new Apriori();
        Algorithm algorithm = new Association(dataset, apriori, apriori);
        List<Rule> rules = algorithm.generateRules();

        Implication implication = Expression.empty().and(Expression.of("CODE_SMELL_BLOB").eq(false))
                .imply(Expression.empty().and(Expression.of("number_of_methods_greater_than_40").eq(false)));

        Rule expectedRule = new Rule(implication, 0.6666666666666666, 0.9230769230769231);
        assertTrue(rules.contains(expectedRule));
    }
}