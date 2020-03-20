package de.unileipzig.irpact.core.agent.company;

import de.unileipzig.irpact.core.agent.pos.PointOfSaleAgent;
import de.unileipzig.irpact.core.currency.Price;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.availability.ProductAvailability;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public class ForTestProductIntroduction implements ProductIntroductionScheme {

    public static final String NAME = ForTestProductIntroduction.class.getSimpleName();

    private ProductAvailability availability;
    private Price price;

    public ForTestProductIntroduction(ProductAvailability availability, Price price) {
        this.availability = availability;
        this.price = price;
    }

    @Override
    public void handle(CompanyAgent agent, Product product) {
        Collection<? extends PointOfSaleAgent> posAgents = agent.getEnvironment()
                .getConfiguration()
                .getPointOfSaleAgents();
        for(PointOfSaleAgent posAgent: posAgents) {
            if(!posAgent.sellsProduct(product)) {
                posAgent.makeAvailable(product, availability, price);
            }
        }
    }
}
