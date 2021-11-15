package de.unileipzig.irpact.core.process2.modular.modules.core;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.process2.modular.HelperAPI2;
import de.unileipzig.irpact.core.process2.modular.SharedModuleData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractModule2<I, O>
        extends NameableBase
        implements Module2<I, O>, HelperAPI2, LoggingHelper {

    protected SharedModuleData sharedData;
    protected int priority = NORM_PRIORITY;
    protected boolean validated = false;
    protected boolean initalized = false;
    protected boolean setupCalled = false;

    protected boolean alreadyValidated() {
        return validated;
    }

    protected void setValidated() {
        validated = true;
    }

    protected boolean alreadyInitalized() {
        return initalized;
    }

    protected void setInitalized() {
        initalized = true;
    }

    protected boolean alreadySetupCalled() {
        return setupCalled;
    }

    public void setSetupCalled() {
        this.setupCalled = true;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public void setSharedData(SharedModuleData sharedData) {
        traceSetSharedData();
        this.sharedData = sharedData;
    }

    public SharedModuleData getSharedData() {
        return sharedData;
    }

    @Override
    public abstract IRPLogger getDefaultLogger();

    @Override
    public IRPSection getDefaultSection() {
        return IRPSection.SIMULATION_PROCESS;
    }
}
