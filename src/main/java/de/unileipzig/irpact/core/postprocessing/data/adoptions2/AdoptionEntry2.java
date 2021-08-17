package de.unileipzig.irpact.core.postprocessing.data.adoptions2;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.util.AdoptionPhase;

import java.time.ZonedDateTime;

/**
 * @author Daniel Abitz
 */
public interface AdoptionEntry2 {

    ConsumerAgent getAgent();

    default ConsumerAgentGroup getAgentGroup() {
        return getAgent().getGroup();
    }

    AdoptedProduct getAdoptedProduct();

    default Product getProduct() {
        return getAdoptedProduct().getProduct();
    }

    default String getProductNameOrNull() {
        Product product = getProduct();
        return product == null ? null : product.getName();
    }

    default ProductGroup getProductGroup() {
        return getProduct().getGroup();
    }

    default String getProductGroupNameOrNull() {
        Product product = getProduct();
        if(product == null || product.getGroup() == null) {
            return null;
        } else {
            return product.getGroup().getName();
        }
    }

    default Timestamp getTimestamp() {
        return getAdoptedProduct().getTimestamp();
    }

    default ZonedDateTime getTimeOrNull() {
        Timestamp timestamp = getTimestamp();
        return timestamp == null ? null : timestamp.getTime();
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
