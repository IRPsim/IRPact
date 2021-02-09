package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.awareness.Awareness;
import de.unileipzig.irpact.core.misc.InitializationData;
import de.unileipzig.irpact.core.process.ProcessFindingScheme;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductFindingScheme;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentInitializationData extends InitializationData {

    SimulationEnvironment getEnvironment();

    SpatialInformation getSpatialInformation();

    double getInformationAuthority();

    ConsumerAgentGroup getGroup();

    Collection<ConsumerAgentAttribute> getAttributes();

    Awareness<Product> getProductAwareness();

    Collection<AdoptedProduct> getAdoptedProducts();

    ProductFindingScheme getProductFindingScheme();

    ProcessFindingScheme getProcessFindingScheme();
}
