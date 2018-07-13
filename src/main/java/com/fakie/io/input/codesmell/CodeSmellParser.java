package com.fakie.io.input.codesmell;

import com.fakie.io.input.FakieInputException;

import java.io.File;

public interface CodeSmellParser {
    boolean accept(File file);

    CodeSmells parse(File file) throws FakieInputException;
}
