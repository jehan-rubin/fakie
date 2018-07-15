package com.fakie.io.input.codesmell;

import com.fakie.model.processor.CodeSmell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Binder {
    private List<CodeSmellBuilder> codeSmells;

    public List<CodeSmellBuilder> getCodeSmells() {
        return codeSmells;
    }

    public void setCodeSmells(List<CodeSmellBuilder> codeSmells) {
        this.codeSmells = codeSmells;
    }

    CodeSmells bind() {
        CodeSmells result = CodeSmells.createIndex();
        for (CodeSmellBuilder codeSmell : codeSmells) {
            CodeSmell cs = codeSmell.build();
            result.add(cs);
        }
        return result;
    }

    public static class CodeSmellBuilder {
        private List<String> labels = new ArrayList<>();
        private Map<String, Object> properties = new HashMap<>();
        private String name;

        public List<String> getLabels() {
            return labels;
        }

        public void setLabels(List<String> labels) {
            this.labels = labels;
        }

        public Map<String, Object> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private CodeSmell build() {
            return new CodeSmell(labels, properties, name);
        }
    }
}
