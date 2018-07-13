package com.fakie.model.converter;

import com.fakie.model.FakieModelException;
import com.fakie.model.graph.Graph;

public interface Converter<T> {
    Graph load(T t);

    T dump(Graph graph) throws FakieModelException;
}
