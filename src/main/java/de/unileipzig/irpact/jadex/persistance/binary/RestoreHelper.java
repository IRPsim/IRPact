package de.unileipzig.irpact.jadex.persistance.binary;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.start.MainCommandLineOptions;

import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public final class RestoreHelper {

    private MainCommandLineOptions options;
    private int year;
    private InRoot root;
    private InputParser parser;
    private ClassManager classManager;
    private boolean printLoggableOnPersist;

    public RestoreHelper() {
    }

    public void clear() {
        options = null;
        root = null;
        parser = null;
        classManager = null;
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

    public void setYear(int year) {
        this.year = year;
    }
    public int getYear() {
        return year;
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

    public void setParser(InputParser parser) {
        this.parser = parser;
    }
    public InputParser getParser() {
        if(parser == null) {
            throw new NoSuchElementException("InputParser");
        }
        return parser;
    }

    public void setClassManager(ClassManager classManager) {
        this.classManager = classManager;
    }
    public ClassManager getClassManager() {
        return classManager;
    }

    public void setPrintLoggableOnPersist(boolean printLoggableOnPersist) {
        this.printLoggableOnPersist = printLoggableOnPersist;
    }
    public boolean isPrintLoggableOnPersist() {
        return printLoggableOnPersist;
    }
}
