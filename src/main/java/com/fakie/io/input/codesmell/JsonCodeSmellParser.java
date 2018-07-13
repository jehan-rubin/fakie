package com.fakie.io.input.codesmell;

import com.fakie.io.input.FakieInputException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonCodeSmellParser implements CodeSmellParser {
    private static final Logger logger = LogManager.getFormatterLogger();
    private static final String EXT = ".json";

    @Override
    public boolean accept(File file) {
        return file.isFile() && file.getName().endsWith(EXT);
    }

    @Override
    public CodeSmells parse(File file) throws FakieInputException {
        logger.info("Parsing %s as a json file", file);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Binder binder = objectMapper.readValue(file, Binder.class);
            return binder.bind();
        }
        catch (IOException e) {
            throw new FakieInputException(e);
        }
    }
}
