package de.unileipzig.irpact.jadex.persistance.binary;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.start.MainCommandLineOptions;

import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public final class RestoreHelper {

    private MainCommandLineOptions options;
    private InRoot root;

    public RestoreHelper() {
    }

    public void clear() {
        options = null;
        root = null;
    }

    public void setOptions(MainCommandLineOptions options) {
        this.options = options;
    }

    public MainCommandLineOptions getOptions() {
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
