package com.fakie.cli.learning;

import com.fakie.Fakie;
import com.fakie.cli.FakieCommand;
import com.fakie.cli.Std;
import com.fakie.cli.dataset.GraphLoaderCommand;
import picocli.CommandLine;

public abstract class FakieLearningCommand extends FakieCommand {
    @CommandLine.ParentCommand
    private GraphLoaderCommand parent;

    @Override
    public Std std() {
        return parent.std();
    }

    @Override
    public Fakie fakie() {
        return parent.fakie();
    }

    @Override
    public void run() {
        applyAlgorithm();
    }

    protected abstract void applyAlgorithm();
}
