package com.fakie.model.processor;

import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Property;
import com.fakie.model.graph.Vertex;
import com.fakie.utils.exceptions.FakieException;
import com.fakie.utils.paprika.Label;

public class DeleteName implements Processor {

    @Override
    public Graph process(Graph graph) throws FakieException {
        for (Vertex cls : graph.findVerticesByLabel(Label.CLASS.toString())) {
            removeNameOfClass(cls);
        }
        return graph;
    }


    private void removeNameOfClass(Vertex cls){
        Object name_property = cls.getProperty("name");
        System.out.println(name_property.toString());
        if(name_property instanceof Property){
            cls.removeProperty((Property)name_property);
        }

    }
}
