package com.fakie.cli.query;

import com.fakie.cli.FakieSubCommand;

public abstract class QueryLanguageCommand extends FakieSubCommand {
    @Override
    public void run() {
        process();
    }

    @Override
    protected void process() {
        createQueries();
    }

    protected abstract void createQueries();

}
