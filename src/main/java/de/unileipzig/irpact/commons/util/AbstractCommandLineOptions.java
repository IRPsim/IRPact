package de.unileipzig.irpact.commons.util;

import picocli.CommandLine;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractCommandLineOptions implements Callable<Integer> {

    /**
     * @author Daniel Abitz
     */
    public static final class PathConverter implements CommandLine.ITypeConverter<Path> {

        @Override
        public Path convert(String value) throws Exception {
            return Paths.get(value);
        }
    }

    /**
     * @author Daniel Abitz
     */
    public static final class CharsetConverter implements CommandLine.ITypeConverter<Charset> {

        @Override
        public Charset convert(String value) throws Exception {
            return Charset.forName(value);
        }
    }

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
