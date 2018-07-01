package com.fakie.cli.learning;

import com.fakie.learning.Rule;
import com.fakie.utils.exceptions.FakieException;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(name = "FPGrowth", aliases = {"FPG"}, description = "Use the  FPGrowth algorithm on the dataset")
public class FPGrowthAlgorithm extends FakieLearningCommand {
    @Override
    public void run() {
        try {
            List<Rule> rules = fakie().fpGrowth();
            for (Rule rule : rules) {
                std().out().println(rule);
            }
        }
        catch (FakieException e) {
            std().err().println(e.getMessage());
        }
    }
}
