package com.fakie.utils;

import com.fakie.io.FakieIOException;
import com.fakie.model.graph.Properties;
import com.fakie.model.graph.Property;
import com.fakie.utils.expression.Expression;

import java.io.File;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class FakieUtils {
    private FakieUtils() {
    }

    public static File findResource(String resource) throws FakieIOException {
        URL url = FakieUtils.class.getClassLoader().getResource(resource);
        if (url == null) {
            throw new FakieIOException("Could not locate resource \' " + resource + "\'");
        }
        try {
            return new File(url.toURI());
        }
        catch (URISyntaxException e) {
            throw new FakieIOException(e);
        }
    }

    public static String uniqueName() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy MM dd - HH mm ss n");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static boolean isACodeSmell(String key) {
        return key.startsWith(Keyword.CODE_SMELL.toString());
    }

    public static boolean isACodeSmell(Property property) {
        return property.getKey().equals(Keyword.CODE_SMELL.toString()) ||
                FakieUtils.isACodeSmell(property.getKey()) && property.getValue().equals(true);
    }

    public static boolean isACodeSmell(Expression expression) {
        return expression.getType().isVariable() && FakieUtils.isACodeSmell(expression.eval().toString());
    }

    public static boolean containsACodeSmell(Properties properties) {
        for (Property property : properties) {
            if (isACodeSmell(property)) {
                return true;
            }
        }
        return false;
    }

    public static BigInteger pair(BigInteger a, BigInteger b) {
        return a.compareTo(b) >= 0 ? a.multiply(a).add(a).add(b) : a.add(b.multiply(b));
    }

    public static Pair unPair(BigInteger z) {
        BigInteger root = sqrt(z);
        BigInteger floor = root.multiply(root);
        return ((z.subtract(floor)).compareTo(root) >= 0) ?
                new Pair(root, z.subtract(floor).subtract(root)) :
                new Pair(z.subtract(floor), root);
    }

    public static BigInteger sqrt(BigInteger n) {
        BigInteger a = BigInteger.ONE;
        BigInteger b = n.shiftRight(5).add(BigInteger.valueOf(8));
        while (b.compareTo(a) >= 0) {
            BigInteger mid = a.add(b).shiftRight(1);
            if (mid.multiply(mid).compareTo(n) > 0)
                b = mid.subtract(BigInteger.ONE);
            else
                a = mid.add(BigInteger.ONE);
        }
        return a.subtract(BigInteger.ONE);
    }

    public static class Pair extends HashMap.SimpleImmutableEntry<BigInteger, BigInteger> {
        Pair(BigInteger key, BigInteger value) {
            super(key, value);
        }
    }
}
