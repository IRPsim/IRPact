package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.awareness.Awareness;
import de.unileipzig.irpact.core.misc.InitializationData;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentInitializationData extends InitializationData {

    SimulationEnvironment getEnvironment();

    SpatialInformation getSpatialInformation();

    double getInformationAuthority();

    ConsumerAgentGroup getGroup();

    Set<ConsumerAgentAttribute> getAttributes();

    Awareness<Product> getProductAwareness();
}
