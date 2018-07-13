package com.fakie.cli.paprika;

import com.fakie.cli.FakieSubCommand;
import com.fakie.cli.dataset.LoadNeo4jDatabase;
import com.fakie.cli.macro.Generate;
import picocli.CommandLine;

@CommandLine.Command(subcommands = {LoadNeo4jDatabase.class, Generate.class})
public abstract class PaprikaCommand extends FakieSubCommand {
    @Override
    public void run() {
        std().disableSystemOutput();
        process();
        std().enableSystemOutput();
    }

    @Override
    protected void process() {
        runPaprika();
    }

    protected abstract void runPaprika();
}
