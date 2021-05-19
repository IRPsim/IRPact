package de.unileipzig.irpact.util.R;

import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public final class R {

    private final Path rPath;

    public R(Path rPath) {
        this.rPath = rPath;
    }

    public Path getRPath() {
        return rPath;
    }

    public String printRPatch() {
        return "\"" + rPath + "\"";
    }
}
