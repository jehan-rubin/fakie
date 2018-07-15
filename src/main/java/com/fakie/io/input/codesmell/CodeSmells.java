package com.fakie.io.input.codesmell;

import com.fakie.model.processor.CodeSmell;

import java.util.*;
import java.util.stream.Collectors;

public class CodeSmells implements Iterable<CodeSmell> {
    private final List<CodeSmell> data;

    private CodeSmells() {
        data = new ArrayList<>();
    }

    void add(CodeSmell codeSmell) {
        this.data.add(codeSmell);
    }

    void addAll(Iterable<CodeSmell> codeSmells) {
        for (CodeSmell codeSmell : codeSmells) {
            add(codeSmell);
        }
    }

    public int size() {
        return data.size();
    }

    public Set<String> names() {
        return this.data.stream().map(CodeSmell::getName).collect(Collectors.toSet());
    }

    @Override
    public Iterator<CodeSmell> iterator() {
        return data.iterator();
    }

    public CodeSmells groupByName(String name) {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CodeSmells that = (CodeSmells) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public String toString() {
        return data.toString();
    }

    static CodeSmells createIndex() {
        return new Index();
    }

    private static class Index extends CodeSmells {
        private final Map<String, CodeSmells> groupedByName;

        Index() {
            this.groupedByName = new HashMap<>();
        }

        @Override
        public void add(CodeSmell codeSmell) {
            super.add(codeSmell);
            this.groupedByName.putIfAbsent(codeSmell.getName(), new CodeSmells());
            this.groupedByName.get(codeSmell.getName()).add(codeSmell);
        }

        @Override
        public CodeSmells groupByName(String name) {
            return groupedByName.getOrDefault(name, new CodeSmells());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            if (!super.equals(o))
                return false;
            Index that = (Index) o;
            return Objects.equals(groupedByName, that.groupedByName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), groupedByName);
        }
    }
}
