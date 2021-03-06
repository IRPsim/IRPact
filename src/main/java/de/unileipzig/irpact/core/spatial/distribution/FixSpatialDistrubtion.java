package de.unileipzig.irpact.core.spatial.distribution;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public class FixSpatialDistrubtion extends NameableBase implements SpatialDistribution {

    protected SpatialInformation fixed;

    public FixSpatialDistrubtion() {
    }

    public void set(SpatialInformation fixed) {
        this.fixed = fixed;
    }

    public SpatialInformation get() {
        return fixed;
    }

    @Override
    public SpatialInformation drawValue() {
        if(fixed == null) {
            throw new NoSuchElementException();
        }
        return fixed;
    }

    @Override
    public boolean setUsed(SpatialInformation information) {
        return true;
    }
}

//    SCHAUEN WAS SICH NOCH PV-ACT MAESSIG VEREINFACHEN LAESST
//        + vllt das PVact-flag aendern und als Option bei Settings einbauen
