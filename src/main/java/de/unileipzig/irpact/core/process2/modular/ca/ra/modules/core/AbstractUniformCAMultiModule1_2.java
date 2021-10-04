package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core;

import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.modules.core.AbstractUniformMultiModule1_2;
import de.unileipzig.irpact.core.process2.modular.modules.core.Module2;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractUniformCAMultiModule1_2<O, O2, M extends Module2<ConsumerAgentData2, O2>>
        extends AbstractUniformMultiModule1_2<ConsumerAgentData2, O, ConsumerAgentData2, O2, M> {
}
