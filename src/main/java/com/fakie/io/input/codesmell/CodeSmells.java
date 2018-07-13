package com.fakie.io.input.codesmell;

import com.fakie.model.processor.CodeSmell;

import java.util.*;
import java.util.stream.Collectors;

public class CodeSmells implements Iterable<CodeSmell> {
    private final List<CodeSmell> data;

    public CodeSmells() {
        this.data = new ArrayList<>();
    }

    void add(CodeSmell codeSmell) {
        this.data.add(codeSmell);
    }

    void addAll(CodeSmells codeSmells) {
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

    public CodeSmells groupByName(String name) {
        CodeSmells group = new CodeSmells();
        for (CodeSmell codeSmell : this.data) {
            if (codeSmell.getName().equals(name)) {
                group.add(codeSmell);
            }
        }
        return group;
    }

    @Override
    public Iterator<CodeSmell> iterator() {
        return data.iterator();
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
}
