package de.unileipzig.irpact.commons.util;

import picocli.CommandLine;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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
    protected int exitCode;
    protected CommandLine cl;

    public AbstractCommandLineOptions(String[] args) {
        ARGS = args;
    }

    public AbstractCommandLineOptions(Args args) {
        this(args.toArray());
    }

    public int parse() {
        ResourceBundle fallback = getFallback();
        if(fallback == null) {
            throw new MissingResourceException("no fallback", getClass().getName(), "");
        }
        return parse(fallback);
    }

    public int parse(String pathToResource, Locale locale) {
        try {
            ResourceBundle bundle = locale == null
                    ? ResourceBundle.getBundle(pathToResource)
                    : ResourceBundle.getBundle(pathToResource, locale);
            return parse(bundle);
        } catch (MissingResourceException e) {
            ResourceBundle fallback = getFallback();
            if(fallback == null) {
                throw e;
            } else {
                return parse(fallback);
            }
        }
    }

    public int parse(ResourceBundle bundle) {
        cl = new CommandLine(this)
                .setUnmatchedArgumentsAllowed(true);
        cl.setResourceBundle(bundle);
        exitCode = cl.execute(ARGS);
        return exitCode;
    }

    public CommandLine getCommandLine() {
        if(cl == null) {
            throw new NoSuchElementException();
        }
        return cl;
    }

    protected abstract ResourceBundle getFallback();

    public String printArgs() {
        return Arrays.toString(ARGS);
    }

    public String[] getArgs() {
        return ARGS;
    }

    public String[] getArgsCopy() {
        return Arrays.copyOf(ARGS, ARGS.length);
    }
}
