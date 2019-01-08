package com.fakie.model.processor;

import com.fakie.model.graph.Edge;
import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import com.fakie.utils.exceptions.FakieException;
import com.fakie.utils.paprika.Label;
import com.fakie.utils.paprika.Relationship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class OwnCloseableObject implements Processor {
    @Override
    public Graph process(Graph graph) throws FakieException {
        for(Vertex cls : graph.findVerticesByLabel(Label.CLASS.toString())){
            try{
                addOwnCloseable(cls);
            }catch(Exception e)
            {
                System.out.println(e.toString());
            }

        }
        return graph;
    }


    private void addOwnCloseable(Vertex cls) throws IOException {
        List<String> closeableClassList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(OwnCloseableObject.class.getResourceAsStream("/closeableObject.fakie")))) {
            while (br.ready()) {
                closeableClassList.add(br.readLine());
            }
        }
        for(Edge classOwnsMethod : cls.outputEdges(Relationship.CLASS_OWNS_METHOD.toString())){
            String flag = "false";
            Vertex method = classOwnsMethod.getDestination();
            for(Edge methodCall : method.outputEdges(Relationship.CALLS.toString())){
                Vertex callMethod = methodCall.getDestination();
                String full_name = (String)callMethod.getProperty("full_name");
                for(String closeable_object : closeableClassList){
                    if(full_name.equals("<init>#"+closeable_object)){
                        flag = "true";
                    }
                }
            }
            cls.setProperty("ownCloseableObject", flag);
        }
    }
}
