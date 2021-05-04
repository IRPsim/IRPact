package de.unileipzig.irpact.commons.attribute.v2;

import de.unileipzig.irpact.commons.NameableBase;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractGroupAttribute extends NameableBase implements GroupAttribute {

    protected boolean artificial;

    @Override
    public boolean isArtificial() {
        return artificial;
    }

    public void setArtificial(boolean artificial) {
        this.artificial = artificial;
    }
}
