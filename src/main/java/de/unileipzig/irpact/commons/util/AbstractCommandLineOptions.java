package de.unileipzig.irpact.commons.util;

import picocli.CommandLine;

import java.util.concurrent.Callable;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractCommandLineOptions implements Callable<Integer> {

    protected final String[] ARGS;
    protected int errorCode;

    public AbstractCommandLineOptions(String[] args) {
        ARGS = args;
    }

    public int parse() {
        CommandLine cl = new CommandLine(this)
                .setUnmatchedArgumentsAllowed(true);
        errorCode = cl.execute(ARGS);
        return errorCode;
    }

    public String[] getArgs() {
        return ARGS;
    }
}
