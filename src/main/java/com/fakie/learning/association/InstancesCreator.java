package com.fakie.learning.association;

import weka.core.Instances;

public interface InstancesCreator {
    Instances createInstances() throws CreatingInstancesException;
}
