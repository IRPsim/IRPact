package de.unileipzig.irpact.core.agent.company;

import de.unileipzig.irpact.core.agent.InformationAgentData;
import de.unileipzig.irpact.core.agent.company.advertisement.AdvertisementScheme;
import de.unileipzig.irpact.core.product.Product;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class CompanyAgentData extends InformationAgentData {

    public CompanyAgentData() {
    }

    protected AdvertisementScheme advertisementScheme;
    public AdvertisementScheme getAdvertisementScheme() {
        return advertisementScheme;
    }
    public void setAdvertisementScheme(AdvertisementScheme advertisementScheme) {
        this.advertisementScheme = advertisementScheme;
    }

    protected ProductIntroductionScheme productIntroductionScheme;
    public ProductIntroductionScheme getProductIntroductionScheme() {
        return productIntroductionScheme;
    }
    public void setProductIntroductionScheme(ProductIntroductionScheme productIntroductionScheme) {
        this.productIntroductionScheme = productIntroductionScheme;
    }

    protected Set<Product> initialProducts;
    public Set<Product> getInitialProducts() {
        return initialProducts;
    }
    public void setInitialProducts(Set<Product> initialProducts) {
        this.initialProducts = initialProducts;
    }
}
