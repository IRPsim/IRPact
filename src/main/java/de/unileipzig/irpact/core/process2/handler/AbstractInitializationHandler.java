package de.unileipzig.irpact.core.process2.handler;

import de.unileipzig.irpact.commons.NameableBase;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractInitializationHandler extends NameableBase implements InitializationHandler {

    protected int priority = NORM_PRIORITY;

    public AbstractInitializationHandler() {
    }

    @Override
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
