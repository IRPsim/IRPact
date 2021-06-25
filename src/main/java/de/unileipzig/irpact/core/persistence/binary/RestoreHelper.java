package de.unileipzig.irpact.core.persistence.binary;

import de.unileipzig.irpact.core.start.IRPactRestoreUpdater;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.start.MainCommandLineOptions;

import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public final class RestoreHelper {

    private MainCommandLineOptions options;
    private int year;
    private InRoot root;
    private IRPactRestoreUpdater updater;
    private ClassManager classManager;
    private boolean printLoggableOnPersist;

    public RestoreHelper() {
    }

    public void clear() {
        options = null;
        root = null;
        updater = null;
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

    public void setUpdater(IRPactRestoreUpdater updater) {
        this.updater = updater;
    }
    public IRPactRestoreUpdater getUpdater() {
        if(updater == null) {
            throw new NoSuchElementException("JadexRestoreUpdater");
        }
        return updater;
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
