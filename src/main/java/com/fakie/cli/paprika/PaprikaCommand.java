package com.fakie.cli.paprika;

import com.fakie.cli.FakieSubCommand;
import com.fakie.cli.learning.AprioriCommand;
import com.fakie.cli.learning.FPGrowthCommand;
import picocli.CommandLine;

@CommandLine.Command(
        subcommands = {FPGrowthCommand.class, AprioriCommand.class})
public abstract class PaprikaCommand extends FakieSubCommand {
    @Override
    public void run() {
        process();
    }

    @Override
    protected void process() {
        runPaprika();
    }

    protected abstract void runPaprika();
}
