package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core;

import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.modules.core.AbstractGenericMultiModuleBase2;
import de.unileipzig.irpact.core.process2.modular.modules.core.Module2;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractGenericCAMultiModuleBase2<O>
        extends AbstractGenericMultiModuleBase2<ConsumerAgentData2, O, ConsumerAgentData2> {

    @Override
    public abstract Module2<ConsumerAgentData2, ?> getSubmodule(int index);
}
