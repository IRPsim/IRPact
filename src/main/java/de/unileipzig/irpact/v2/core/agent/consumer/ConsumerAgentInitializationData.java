package de.unileipzig.irpact.v2.core.agent.consumer;

import de.unileipzig.irpact.v2.commons.awareness.Awareness;
import de.unileipzig.irpact.v2.core.misc.InitializationData;
import de.unileipzig.irpact.v2.core.product.Product;
import de.unileipzig.irpact.v2.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.v2.core.spatial.SpatialInformation;

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
