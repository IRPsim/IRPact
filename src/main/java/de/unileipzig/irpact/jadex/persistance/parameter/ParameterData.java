package de.unileipzig.irpact.jadex.persistance.parameter;

import de.unileipzig.irpact.commons.persistence.PersistableBase;

/**
 * @author Daniel Abitz
 */
public class ParameterData extends PersistableBase {

    protected Object paramObject;

    public ParameterData() {
    }

    public void setParamObject(Object paramObject) {
        this.paramObject = paramObject;
    }

    @SuppressWarnings("unchecked")
    public <T> T getParamObject() {
        return (T) paramObject;
    }
}
