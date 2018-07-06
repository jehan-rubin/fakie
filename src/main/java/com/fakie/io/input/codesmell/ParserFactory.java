package com.fakie.io.input.codesmell;

import com.fakie.io.input.FakieInputException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ParserFactory {
    private static final Logger logger = LogManager.getFormatterLogger();
    private final List<CodeSmellParser> parsers;

    public ParserFactory(CodeSmellParser... codeSmellParsers) {
        this.parsers = Arrays.asList(codeSmellParsers);
    }

    public CodeSmellParser createInstance(File file) throws FakieInputException {
        logger.info("Creating parser for " + file);
        for (CodeSmellParser parser : parsers) {
            if (parser.accept(file)) {
                return parser;
            }
        }
        throw new FakieInputException("Unsupported file \'" + file + "\'");
    }
}
