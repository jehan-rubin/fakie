package com.fakie.io.input.codesmell;

import com.fakie.io.input.FakieInputException;
import com.fakie.model.processor.CodeSmell;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonCodeSmellParser implements CodeSmellParser {
    @Override
    public List<CodeSmell> parse(File file) throws FakieInputException {
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
