package de.unileipzig.irpact.core.util.result.adoptions;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.util.AdoptionPhase;

/**
 * @author Daniel Abitz
 */
public interface AdoptionEntry {

    ConsumerAgent getAgent();

    default ConsumerAgentGroup getAgentGroup() {
        return getAgent().getGroup();
    }

    AdoptedProduct getAdoptedProduct();

    default Product getProduct() {
        return getAdoptedProduct().getProduct();
    }

    default ProductGroup getProductGroup() {
        return getProduct().getGroup();
    }

    default Timestamp getTimestamp() {
        return getAdoptedProduct().getTimestamp();
    }

    default int getYear() {
        return getTimestamp().getYear();
    }

    default Need getNeed() {
        return getAdoptedProduct().getNeed();
    }

    default AdoptionPhase getPhase() {
        return getAdoptedProduct().getPhase();
    }
}
