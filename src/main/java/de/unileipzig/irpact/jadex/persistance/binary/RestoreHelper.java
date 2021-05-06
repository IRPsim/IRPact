package de.unileipzig.irpact.jadex.persistance.binary;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.start.CommandLineOptions;

import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public final class RestoreHelper {

    private CommandLineOptions options;
    private InRoot root;

    public RestoreHelper() {
    }

    public void setOptions(CommandLineOptions options) {
        this.options = options;
    }

    public CommandLineOptions getOptions() {
        if(options == null) {
            throw new NoSuchElementException("CommandLineOptions");
        }
        return options;
    }

    public void setInRoot(InRoot root) {
        this.root = root;
    }

    public InRoot getInRoot() {
        if(root == null) {
            throw new NoSuchElementException("InRoot");
        }
        return root;
    }
}
