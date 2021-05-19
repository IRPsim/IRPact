package de.unileipzig.irpact.commons.util;

import de.unileipzig.irpact.start.MainCommandLineOptions;

import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public class IRPArgs extends SimpleArgs {

    public IRPArgs() {
        super();
    }

    public IRPArgs setMode(int id) {
        set("--irpactMode", Integer.toString(id));
        return this;
    }

    public IRPArgs setNoSimulation(boolean value) {
        if(value) {
            set("--noSimulation");
        } else {
            remove("--noSimulation");
        }
        return this;
    }

    public IRPArgs setFilterError(boolean value) {
        if(value) {
            set("--filterError");
        } else {
            remove("--filterError");
        }
        return this;
    }

    public IRPArgs setLogPath(Path path) {
        set("--logPath", path.toString());
        remove("--logConsoleAndFile");
        return this;
    }

    public IRPArgs setLogPathWithConsole(Path path) {
        set("--logPath", path.toString());
        set("--logConsoleAndFile");
        return this;
    }

    public IRPArgs setInput(Path path) {
        if(path == null) {
            remove("-i");
        } else {
            set("-i", path.toString());
        }
        return this;
    }

    public IRPArgs setOutput(Path path) {
        if(path == null) {
            remove("-o");
        } else {
            set("-o", path.toString());
        }
        return this;
    }

    public IRPArgs setDataDir(Path path) {
        if(path == null) {
            remove("--dataDir");
        } else {
            set("--dataDir", path.toString());
        }
        return this;
    }

    public IRPArgs setUtilities() {
        set("--utilities");
        return this;
    }

    public IRPArgs setDecodeBinaryPaths(Path input, Path output) {
        setAll("--decodeBinaryPersist", input.toString(), output.toString());
        return this;
    }

    public IRPArgs setPrintCumulativeAdoptionsFor(String... miliues) {
        set("--printCumulativeAdoptions");
        setAll("--milieus", miliues);
        return this;
    }

    public IRPArgs setRexe(Path path) {
        set("-R", path.toString());
        return this;
    }

    public IRPArgs setRscript(Path path) {
        set("--rscript", path.toString());
        return this;
    }

    public IRPArgs setRInOut(Path in, Path out) {
        set("--rinput", in.toString());
        set("--routput", out.toString());
        return this;
    }
}
