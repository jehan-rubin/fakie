package com.fakie;


import com.fakie.cli.FakieCLI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {
    private static final Logger logger = LogManager.getFormatterLogger();

    public static void main(String[] args) {
        logger.trace("Running fakie %s", () -> Arrays.stream(args).collect(Collectors.joining(" ")));
        new FakieCLI().parse(args);
        logger.trace("Exiting fakie");
    }
}
