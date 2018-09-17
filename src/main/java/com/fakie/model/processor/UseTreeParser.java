package com.fakie.model.processor;

import com.fakie.model.graph.Edge;
import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import com.fakie.utils.exceptions.FakieException;
import com.fakie.utils.paprika.Label;
import com.fakie.utils.paprika.Relationship;

public class UseTreeParser  implements Processor  {
    @Override
    public Graph process(Graph graph) throws FakieException {
        for (Vertex cls : graph.findVerticesByLabel(Label.CLASS.toString())){
            addUseTreeParser(cls);
        }
        return graph;
    }

    private void addUseTreeParser(Vertex cls){
        String flag = "false";
        for(Edge classOwnsMethod : cls.outputEdges(Relationship.CLASS_OWNS_METHOD.toString())){
            Vertex method = classOwnsMethod.getDestination();
            for(Edge methodCall : method.outputEdges(Relationship.CALLS.toString())){
                Vertex callMethod = methodCall.getDestination();
                String full_name = (String)callMethod.getProperty("full_name");
                if(full_name.matches("TreeParser")){
                    flag = "true";
                }
            }
        }
        cls.setProperty("callTreeParser", flag);
    }
}
