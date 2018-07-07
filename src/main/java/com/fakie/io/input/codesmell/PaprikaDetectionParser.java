package com.fakie.io.input.codesmell;

import com.fakie.io.input.FakieInputException;
import com.fakie.model.processor.CodeSmell;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaprikaDetectionParser implements CodeSmellParser {
    private static final String[] EXT = new String[]{"csv"};
    private static final Pattern FILENAME = Pattern.compile("([A-Z])+");

    @Override
    public boolean accept(File file) {
        return file.isDirectory();
    }

    @Override
    public List<CodeSmell> parse(File file) throws FakieInputException {
        return parseDirectory(file);
    }

    private List<CodeSmell> parseDirectory(File file) throws FakieInputException {
        Collection<File> files = FileUtils.listFiles(file, EXT, false);
        List<CodeSmell> codeSmells = new ArrayList<>();
        for (File csv : files) {
            Optional<String> name = codeSmellName(csv.getName());
            if (name.isPresent()) {
                codeSmells.addAll(parseFile(csv, name.get()));
            }

        }
        return codeSmells;
    }

    private List<CodeSmell> parseFile(File file, String name) throws FakieInputException {
        List<CodeSmell> codeSmells = new ArrayList<>();
        try {
            CSVParser csv = CSVParser.parse(file, Charset.defaultCharset(), CSVFormat.DEFAULT.withHeader());
            Map<String, Integer> headerMap = csv.getHeaderMap();
            for (CSVRecord record : csv) {
                Map<String, Object> properties = new HashMap<>();
                for (Map.Entry<String, Integer> header : headerMap.entrySet()) {
                    String value = record.get(header.getValue());
                    properties.put(header.getKey(), value);
                }
                CodeSmell codeSmell = new CodeSmell(new ArrayList<>(), properties, name);
                codeSmells.add(codeSmell);
            }
        }
        catch (IOException e) {
            throw new FakieInputException(e);
        }
        return codeSmells;
    }

    private Optional<String> codeSmellName(String filename) throws FakieInputException {
        Matcher matcher = FILENAME.matcher(filename);
        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        }
        return Optional.empty();
    }
}