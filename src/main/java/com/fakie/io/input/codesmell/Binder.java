package com.fakie.io.input.codesmell;

import com.fakie.model.processor.CodeSmell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Binder {
    private List<CodeSmellBuilder> codeSmells;

    public List<CodeSmellBuilder> getCodeSmells() {
        return codeSmells;
    }

    public void setCodeSmells(List<CodeSmellBuilder> codeSmells) {
        this.codeSmells = codeSmells;
    }

    public List<CodeSmell> bind() {
        return codeSmells.stream().map(CodeSmellBuilder::build).collect(Collectors.toList());
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

        public CodeSmell build() {
            return new CodeSmell(labels, properties, name);
        }
    }
}
