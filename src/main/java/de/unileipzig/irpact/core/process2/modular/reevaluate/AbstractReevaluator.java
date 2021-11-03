package de.unileipzig.irpact.core.process2.modular.reevaluate;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.process2.modular.SharedModuleData;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractReevaluator<I> extends NameableBase implements Reevaluator<I> {

    protected SharedModuleData sharedData;
    protected int priority = NORM_PRIORITY;

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setSharedData(SharedModuleData sharedData) {
        this.sharedData = sharedData;
    }

    public SharedModuleData getSharedData() {
        return sharedData;
    }
}
