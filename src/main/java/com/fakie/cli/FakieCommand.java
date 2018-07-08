package com.fakie.cli;

import com.fakie.Fakie;
import picocli.CommandLine;

@CommandLine.Command(
        mixinStandardHelpOptions = true,
        descriptionHeading = "@|bold %nDescription|@:%n  ",
        parameterListHeading = "@|bold %nParameters|@:%n",
        optionListHeading = "@|bold %nOptions|@:%n",
        commandListHeading = "@|bold %nCommands|@:%n")
public abstract class FakieCommand implements Runnable {
    public abstract Std std();

    public abstract Fakie fakie();

    public abstract CommandLine.ParseResult getSubResult();
}
