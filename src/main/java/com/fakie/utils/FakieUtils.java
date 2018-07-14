package com.fakie.utils;

import com.fakie.io.FakieIOException;
import com.fakie.model.graph.Property;
import com.fakie.utils.expression.Expression;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FakieUtils {
    private FakieUtils() {}

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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy MM dd - HH mm ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static boolean isACodeSmell(String key) {
        return key.startsWith(Keyword.CODE_SMELL.toString());
    }

    public static boolean isACodeSmell(Property property) {
        return FakieUtils.isACodeSmell(property.getKey());
    }

    public static boolean isACodeSmell(Expression expression) {
        return expression.getType().isVariable() && FakieUtils.isACodeSmell(expression.eval().toString());
    }
}
