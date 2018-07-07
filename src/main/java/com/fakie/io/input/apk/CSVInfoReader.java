package com.fakie.io.input.apk;

import com.fakie.io.input.FakieInputException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class CSVInfoReader implements InfoReader {
    @Override
    public APKInfo readInfo(File file) throws FakieInputException {
        APKInfo apkInfo = new APKInfo();
        try {
            CSVParser csv = CSVParser.parse(file, Charset.defaultCharset(), CSVFormat.DEFAULT);
            for (CSVRecord record : csv) {
                apkInfo.add(new APKInfo.Entry(record.get(0), record.get(1), record.get(2)));
            }
            return apkInfo;
        }
        catch (IOException e) {
            throw new FakieInputException(e);
        }
    }
}
