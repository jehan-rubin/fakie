package com.fakie.cli;

import com.fakie.Fakie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

public abstract class FakieSubCommand extends FakieCommand {
    private static final Logger logger = LogManager.getFormatterLogger();

    @CommandLine.ParentCommand
    private FakieCommand parent;

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
        logger.debug("Using command \'%s\'", this::name);
        if (noSubCommandSelected()) {
            logger.warn("%s requires at least one command", this::name);
            CommandLine.usage(this, std().err());
        }
        else {
            process();
        }
    }

    protected abstract void process();

    @Override
    public CommandLine.ParseResult getSubResult() {
        return parent.getSubResult().subcommand();
    }

    public String name() {
        CommandLine.ParseResult subResult = parent.getSubResult();
        return subResult.commandSpec().name();
    }

    private boolean noSubCommandSelected() {
        CommandLine.ParseResult subResult = parent.getSubResult();
        return !subResult.hasSubcommand();
    }
}
