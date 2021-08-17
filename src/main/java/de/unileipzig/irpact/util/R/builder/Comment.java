package de.unileipzig.irpact.util.R.builder;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class Comment implements Element {

    protected String comment;

    public Comment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public boolean isPrintable() {
        return true;
    }

    @Override
    public boolean print(StringSettings settings, Appendable target) throws IOException {
        target.append(settings.getComment());
        target.append(comment);
        return true;
    }
}
