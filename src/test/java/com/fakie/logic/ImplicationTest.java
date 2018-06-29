package com.fakie.logic;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class ImplicationTest {
    private static final String LM = "LONG_METHOD";
    private static final String LONG_METHOD = "method_contains_between_40_and_59_instructions";
    private static final String VERY_LONG_METHOD = "method_contains_more_than_60_instructions";
    
    @Test
    public void anImplicationIsEqualsToItsContrapositive() {
        And left = new And(Collections.singletonList(new Expression(LM, false)));
        And right = new And(Arrays.asList(new Expression(VERY_LONG_METHOD, false), new Expression(LONG_METHOD, false)));
        Implication implication = new Implication(left, right);
        Implication contrapositive = implication.contrapositive();
        assertEquals(implication, contrapositive);
    }

    @Test
    public void anImplicationIsEqualsToAnotherIfThereAreInContraposition() {
        Operator left = new And(Collections.singletonList(new Expression(LM, false)));
        Operator right = new And(Arrays.asList(new Expression(VERY_LONG_METHOD, false), new Expression(LONG_METHOD, false)));
        Implication implication = new Implication(left, right);

        left = new Or(Arrays.asList(new Expression(VERY_LONG_METHOD, true), new Expression(LONG_METHOD, true)));
        right = new Or(Collections.singletonList(new Expression(LM, true)));
        Implication contrapositive = new Implication(left, right);

        assertEquals(implication, contrapositive);
    }

    @Test
    public void anImplicationIsNotEqualsToOthersContrapositive() {
        Operator left = new And(Collections.singletonList(new Expression(LM, false)));
        Operator right = new And(Arrays.asList(new Expression(VERY_LONG_METHOD, false), new Expression(LONG_METHOD, false)));
        Implication implication = new Implication(left, right);

        left = new Or(Arrays.asList(new Expression(VERY_LONG_METHOD, false), new Expression(LONG_METHOD, true)));
        right = new Or(Collections.singletonList(new Expression(LM, true)));
        Implication contrapositive = new Implication(left, right);

        assertNotEquals(implication, contrapositive);

        left = new Or(Arrays.asList(new Expression(VERY_LONG_METHOD, true), new Expression(LONG_METHOD, false)));
        right = new Or(Collections.singletonList(new Expression(LM, true)));
        contrapositive = new Implication(left, right);

        assertNotEquals(implication, contrapositive);

        left = new Or(Arrays.asList(new Expression(VERY_LONG_METHOD, true), new Expression(LONG_METHOD, true)));
        right = new Or(Collections.singletonList(new Expression(LM, false)));
        contrapositive = new Implication(left, right);

        assertNotEquals(implication, contrapositive);

        left = new Or(Arrays.asList(new Expression(VERY_LONG_METHOD, false), new Expression(LONG_METHOD, false)));
        right = new Or(Collections.singletonList(new Expression(LM, false)));
        contrapositive = new Implication(left, right);

        assertNotEquals(implication, contrapositive);
    }

    @Test
    public void blobImplication() {
        Operator left = new And(Collections.singletonList(new Expression("number_of_methods_greater_than_40", true)));
        Operator right = new And(Collections.singletonList(new Expression("blob", true)));
        Implication implication = new Implication(left, right);

        left = new Or(Collections.singletonList(new Expression("blob", false)));
        right = new Or(Collections.singletonList(new Expression("number_of_methods_greater_than_40", false)));
        Implication contraposition = new Implication(left, right);

        assertEquals(implication, contraposition);
    }
}