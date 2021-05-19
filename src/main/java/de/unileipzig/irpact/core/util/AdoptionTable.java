package de.unileipzig.irpact.core.util;

import de.unileipzig.irpact.commons.util.data.VarMap;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;

import java.util.function.Predicate;

/**
 * @author Daniel Abitz
 */
public class AdoptionTable {

    private final VarMap MAP = new VarMap(
            Integer.class,
            ConsumerAgentGroup.class,
            ProductGroup.class,
            Product.class,
            Data.class
    );

    public static Predicate<Integer> forYear(int year) {
        return _year -> _year == year;
    }

    public static Predicate<ConsumerAgentGroup> forConsumerAgentGroup(ConsumerAgentGroup cag) {
        return _cag -> _cag == cag;
    }

    public static Predicate<ProductGroup> forProductGroup(ProductGroup pg) {
        return _pg -> _pg == pg;
    }

    //=========================
    //
    //=========================

    public AdoptionTable() {
    }

    public void addAll(ConsumerAgentGroup group) {
        for(ConsumerAgent agent: group.getAgents()) {
            addAll(agent);
        }
    }

    public void addAll(ConsumerAgent agent) {
        for(AdoptedProduct adoptedProduct: agent.getAdoptedProducts()) {
            MAP.putArray(
                    adoptedProduct.getYear(),
                    agent.getGroup(),
                    adoptedProduct.getProduct().getGroup(),
                    adoptedProduct.getProduct(),
                    new Data(agent, adoptedProduct)
            );
        }
    }

    public VarMap getMap() {
        return MAP;
    }

    /**
     * @author Daniel Abitz
     */
    public static final class Data {

        private final ConsumerAgent agent;
        private final AdoptedProduct adoptedProduct;

        private Data(ConsumerAgent agent, AdoptedProduct product) {
            this.agent = agent;
            this.adoptedProduct = product;
        }

        public ConsumerAgent getAgent() {
            return agent;
        }

        public AdoptedProduct getAdoptedProduct() {
            return adoptedProduct;
        }

        public Product getProduct() {
            return adoptedProduct.getProduct();
        }

        public int getYear() {
            return adoptedProduct.getYear();
        }
    }
}
