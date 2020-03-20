package de.unileipzig.irpact.core.agent.company;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.InformationAgentBase;
import de.unileipzig.irpact.core.agent.company.advertisement.AdvertisementScheme;
import de.unileipzig.irpact.core.message.Message;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.EntityType;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class CompanyAgentBase extends InformationAgentBase implements CompanyAgent {

    protected AdvertisementScheme advertisementScheme;
    protected ProductIntroductionScheme productIntroductionScheme;
    protected Set<Product> initialProducts;

    public CompanyAgentBase(
            SimulationEnvironment environment,
            String name,
            double informationAuthority,
            AdvertisementScheme advertisementScheme,
            ProductIntroductionScheme productIntroductionScheme,
            Set<Product> initialProducts) {
        super(environment, name, informationAuthority);
        this.advertisementScheme = advertisementScheme;
        this.productIntroductionScheme = productIntroductionScheme;
        this.initialProducts = initialProducts;
    }

    @Override
    public Set<Product> getProductPortfolio() {
        throw new UnsupportedOperationException();
    }

    @Override
    public AdvertisementScheme getAdvertisementScheme() {
        return advertisementScheme;
    }

    @Override
    public ProductIntroductionScheme getProductIntroductionScheme() {
        return productIntroductionScheme;
    }

    @Override
    public boolean is(EntityType type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isHandling(Agent sender, Message msg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handleMessage(Agent sender, Message msg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addProductToPortfolio(Product product) {
        throw new UnsupportedOperationException();
    }

    public Set<Product> getInitialProducts() {
        return initialProducts;
    }
}
