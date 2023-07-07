package de.unileipzig.irpact.commons.attribute.v2.simple;

import de.unileipzig.irpact.commons.attribute.v2.AbstractAttributeBase2;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractAttribute2 extends AbstractAttributeBase2 implements Attribute2 {

    protected boolean artificial = false;

    public void setArtificial(boolean artificial) {
        this.artificial = artificial;
    }

    @Override
    public boolean isArtificial() {
        return artificial;
    }
}
