package de.unileipzig.irpact.util.R.builder;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class Library implements Element {

    protected String lib;

    public Library(String lib) {
        this.lib = lib;
    }

    public String getLibrary() {
        return lib;
    }

    @Override
    public boolean print(StringSettings settings, Appendable target) throws IOException {
        target.append("library(");
        target.append(lib);
        target.append(")");
        return true;
    }
}
