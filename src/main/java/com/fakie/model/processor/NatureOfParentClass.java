package com.fakie.model.processor;

import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import com.fakie.model.graph.Edge;
import com.fakie.utils.exceptions.FakieException;
import com.fakie.utils.paprika.Key;
import com.fakie.utils.paprika.Label;
import com.fakie.utils.paprika.Relationship;

import java.util.Objects;

public class NatureOfParentClass implements Processor {
    @Override
    public Graph process(Graph graph) throws FakieException {
        for (Vertex cls : graph.findVerticesByLabel(Label.CLASS.toString())){
            addNatureOfClassMethod(cls);
        }
        return graph;
    }

    private void addNatureOfClassMethod(Vertex cls){
        Object is_async_task = cls.getProperty("is_async_task");
        Object is_broadcast_receiver = cls.getProperty("is_broadcast_receiver");
        Object is_content_provider = cls.getProperty("is_content_provider");
        Object is_service = cls.getProperty("is_service");
        Object is_view = cls.getProperty("is_view");
        Object is_activity = cls.getProperty("is_activity");
        if(Objects.isNull(is_async_task)){is_async_task = false;}
        if(Objects.isNull(is_broadcast_receiver)){is_broadcast_receiver =  false;}
        if(Objects.isNull(is_content_provider)){is_content_provider = false;}
        if(Objects.isNull(is_service)){is_service =  false;}
        if(Objects.isNull(is_view)){is_view =  false;}
        if(Objects.isNull(is_activity)){is_activity =  false;}

        for(Edge classOwnsMethod : cls.outputEdges(Relationship.CLASS_OWNS_METHOD.toString())){
            Vertex method = classOwnsMethod.getDestination();
            if((boolean)is_async_task == true){
                method.setProperty("natureOfParentClassIs_async_task", is_async_task.toString());
            }else if((boolean)is_broadcast_receiver == true){
                method.setProperty("natureOfParentClassIs_broadcast_receiver", is_broadcast_receiver.toString());
            }else if((boolean)is_content_provider == true){
                method.setProperty("natureOfParentClassIs_content_provider", is_content_provider.toString());
            }else if((boolean)is_service == true) {
                method.setProperty("natureOfParentClassIs_service", is_service.toString());
            }else if((boolean)is_view == true){
                method.setProperty("natureOfParentClassIs_view", is_view.toString());
            }else if((boolean)is_activity == true){
                method.setProperty("natureOfParentClassIs_activity", is_activity.toString());
            }else{
                Object normal_class = true;
                method.setProperty("natureOfParentClassIs_normalClass", normal_class.toString());
            }
        }
    }
}


