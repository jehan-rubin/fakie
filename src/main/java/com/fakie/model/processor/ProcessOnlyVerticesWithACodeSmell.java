package com.fakie.model.processor;

import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import com.fakie.utils.FakieUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class ProcessOnlyVerticesWithACodeSmell implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph process(Graph graph) throws ProcessingException {
        logger.info("Keep only objects in %s with a code smell", graph);
        int stopKeeping = 0;
        int howMuchkeep = 0;
        Random random = new Random();
        for(Vertex vertex : graph.getVertices()){
            if(FakieUtils.containsACodeSmell(vertex)){
                stopKeeping += 1;
            }
        }
        for (Vertex vertex : graph.getVertices()) {
            if (!FakieUtils.containsACodeSmell(vertex)) {
                int n = random.nextInt(100);
                if((n%2) == 0 && howMuchkeep < stopKeeping)
                {
                    howMuchkeep +=1;
                    graph.remove(vertex);
                }else{
                    graph.remove(vertex);
                }
            }
        }
        return graph;
    }
}
