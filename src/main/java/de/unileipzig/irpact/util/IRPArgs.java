package de.unileipzig.irpact.util;

import de.unileipzig.irpact.commons.util.SimpleArgs;

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

    @Override
    public IRPArgs set(String option) throws IllegalArgumentException {
        super.set(option);
        return this;
    }

    @Override
    public IRPArgs set(String option, String arg) throws IllegalArgumentException {
        super.set(option, arg);
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

    public IRPArgs setImage(Path path) {
        if(path == null) {
            remove("--image");
        } else {
            set("--image", path.toString());
        }
        return this;
    }

    public IRPArgs setNoSimulation() {
        set("--noSimulation");
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

    public IRPArgs setOutputDir(Path path) {
        if(path == null) {
            remove("--outputDir");
        } else {
            set("--outputDir", path.toString());
        }
        return this;
    }

    public IRPArgs setDownloadDir(Path path) {
        if(path == null) {
            remove("--downloadDir");
        } else {
            set("--downloadDir", path.toString());
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

    public IRPArgs setGnuPlotCommand(Path path) {
        set("--gnuplotCommand", path.toString());
        return this;
    }

    public IRPArgs setRscriptCommand(Path path) {
        set("--rscriptCommand", path.toString());
        return this;
    }
}
