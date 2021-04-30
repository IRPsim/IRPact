package de.unileipzig.irpact.commons.attribute.v2;

import de.unileipzig.irpact.commons.NameableBase;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractAttribute extends NameableBase implements Attribute {

    protected boolean artificial;

    @Override
    public boolean isArtificial() {
        return artificial;
    }

    public void setArtificial(boolean artificial) {
        this.artificial = artificial;
    }
}
