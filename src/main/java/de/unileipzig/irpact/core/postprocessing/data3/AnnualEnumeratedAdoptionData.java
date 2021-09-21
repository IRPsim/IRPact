package de.unileipzig.irpact.core.postprocessing.data3;

import de.unileipzig.irpact.commons.util.data.count.CountMap3D;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public abstract class AnnualEnumeratedAdoptionData<T> {

    public static final int INITIAL_YEAR = Integer.MIN_VALUE;

    protected CountMap3D<Integer, Product, T> data = new CountMap3D<>();

    public AnnualEnumeratedAdoptionData() {
    }

    public CountMap3D<Integer, Product, T> getData() {
        return data;
    }

    public void analyse(SimulationEnvironment environment) {
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            for(ConsumerAgent ca: cag.getAgents()) {
                for(AdoptedProduct ap: ca.getAdoptedProducts()) {
                    update(ca, ap);
                }
            }
        }
    }

    public int getCount(int year, Product product, T value) {
        return data.getCount(year, product, value);
    }

    public abstract void update(ConsumerAgent ca, AdoptedProduct ap);

    protected abstract AnnualEnumeratedAdoptionData<T> newInstance();

    public AnnualEnumeratedAdoptionData<T> cumulate() {
        AnnualEnumeratedAdoptionData<T> cumulated = newInstance();
        List<Integer> years = new ArrayList<>(data.getFirstKeys());
        Set<Product> products = data.getAllSecondKeys();
        Set<T> values = data.getAllThirdKeys();
        for(Product product: products) {
            for(T value: values) {
                for(int i = 0; i < years.size() - 1; i++) {
                    int thisYear = years.get(i);
                    int nextYear = years.get(i + 1);
                    int thisCount = getCount(thisYear, product, value);
                    int nextCount = getCount(nextYear, product, value);
                    cumulated.data.init(thisYear, product, value, thisCount + nextCount);
                }
            }
        }
        return cumulated;
    }

    public AnnualEnumeratedAdoptionData<T> uncumulate() {
        AnnualEnumeratedAdoptionData<T> uncumulated = newInstance();
        List<Integer> years = new ArrayList<>(data.getFirstKeys());
        Set<Product> products = data.getAllSecondKeys();
        Set<T> values = data.getAllThirdKeys();
        for(Product product: products) {
            for(T value: values) {
                for(int i = years.size() - 1; i >= 1; i--) {
                    int thisYear = years.get(i);
                    int preYear = years.get(i - 1);
                    int thisCount = getCount(thisYear, product, value);
                    int preCount = getCount(preYear, product, value);
                    uncumulated.data.init(thisYear, product, value, thisCount - preCount);
                }
            }
        }
        return uncumulated;
    }
}
