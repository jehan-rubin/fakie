package com.fakie.io.input.codesmell;

import com.fakie.io.input.FakieInputException;
import com.fakie.model.processor.CodeSmell;

import java.io.File;
import java.util.List;

public interface CodeSmellParser {
    boolean accept(File file);

    List<CodeSmell> parse(File file) throws FakieInputException;
}
