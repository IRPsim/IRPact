package de.unileipzig.irpact.core.util;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.product.Product;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Daniel Abitz
 */
/*
zeitraum:
cag -> daten (total)
cag -> productgroup -> daten (nach gruppe)
total:
year -> cag -> productgroup -> daten
year -> cag -> fixproduct -> daten
 */
public final class AdoptionResults2 implements Iterable<AdoptionResults2.Entry> {

    private final List<Entry> entries = new ArrayList<>();

    public AdoptionResults2() {
    }

    public void add(ConsumerAgentGroup group) {
        for(ConsumerAgent agent: group.getAgents()) {
            add(agent);
        }
    }

    public void add(ConsumerAgent agent) {
        for(AdoptedProduct product: agent.getAdoptedProducts()) {
            Entry entry = new Entry(agent, product);
            entries.add(entry);
        }
    }

    @Override
    public Iterator<Entry> iterator() {
        return entries.iterator();
    }

    /**
     * @author Daniel Abitz
     */
    public static final class Entry {

        private final ConsumerAgent agent;
        private final AdoptedProduct adoptedProduct;

        private Entry(ConsumerAgent agent, AdoptedProduct product) {
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
