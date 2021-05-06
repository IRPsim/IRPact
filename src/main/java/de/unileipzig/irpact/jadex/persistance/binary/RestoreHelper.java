package de.unileipzig.irpact.jadex.persistance.binary;

import de.unileipzig.irpact.start.CommandLineOptions;

import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public final class RestoreHelper {

    private CommandLineOptions options;

    public RestoreHelper() {
    }

    public void setOptions(CommandLineOptions options) {
        this.options = options;
    }

    public CommandLineOptions getOptions() {
        if(options == null) {
            throw new NoSuchElementException("options");
        }
        return options;
    }
}
