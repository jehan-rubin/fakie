package com.fakie.io.input.codesmell;

import com.fakie.io.input.FakieInputException;
import com.fakie.model.processor.CodeSmell;

import java.io.File;
import java.util.List;

public interface CodeSmellParser {
    List<CodeSmell> parse(File file) throws FakieInputException;
}
