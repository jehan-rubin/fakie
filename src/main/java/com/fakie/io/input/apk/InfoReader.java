package com.fakie.io.input.apk;

import com.fakie.io.input.FakieInputException;

import java.io.File;

public interface InfoReader {
    APKInfo readInfo(File file) throws FakieInputException;
}
