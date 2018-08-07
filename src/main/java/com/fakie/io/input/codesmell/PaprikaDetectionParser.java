package com.fakie.io.input.codesmell;

import com.fakie.io.input.FakieInputException;
import com.fakie.model.processor.CodeSmell;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaprikaDetectionParser implements CodeSmellParser {
    private static final Logger logger = LogManager.getFormatterLogger();
    private static final String CSV = "csv";
    private static final String[] EXT = new String[]{CSV};
    private static final Pattern FILENAME = Pattern.compile("([A-Z]+)");

    @Override
    public boolean accept(File file) {
        return file.isDirectory() || file.getName().endsWith(CSV);
    }

    @Override
    public CodeSmells parse(File file) throws FakieInputException {
        logger.info("Parsing %s as a Paprika detection output folder", file);
        if (file.isDirectory()) {
            return parseDirectory(file);
        }
        else {
            return parseFile(file);
        }
    }

    private CodeSmells parseDirectory(File file) throws FakieInputException {
        Collection<File> files = FileUtils.listFiles(file, EXT, false);
        CodeSmells codeSmells = CodeSmells.createIndex();
        for (File csv : files) {
            codeSmells.addAll(parseFile(csv));
        }
        return codeSmells;
    }

    private CodeSmells parseFile(File file) throws FakieInputException {
        Optional<String> name = codeSmellName(file.getName());
        if (name.isPresent()) {
            return parseCSV(file, name.get());
        }
        return CodeSmells.createIndex();
    }

    private CodeSmells parseCSV(File file, String name) throws FakieInputException {
        logger.info("Parsing %s", file);
        CodeSmells codeSmells = CodeSmells.createIndex();
        try (CSVParser csv = CSVParser.parse(file, Charset.defaultCharset(), CSVFormat.DEFAULT.withHeader())) {
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

    private Optional<String> codeSmellName(String filename) {
        Matcher matcher = FILENAME.matcher(filename);
        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        }
        return Optional.empty();
    }

    private enum CSVHeader {
        FULL_NAME("full_name"),
        NAME("name");

        private final String name;

        CSVHeader(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
