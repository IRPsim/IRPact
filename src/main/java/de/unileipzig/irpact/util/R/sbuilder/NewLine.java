package de.unileipzig.irpact.util.R.sbuilder;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class NewLine implements Element {

    public static final NewLine INSTANCE = new NewLine();

    public NewLine() {
    }

    @Override
    public boolean print(StringSettings settings, Appendable target) throws IOException {
        target.append(settings.getNewLine());
        return true;
    }
}
