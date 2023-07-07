package de.unileipzig.irpact.core.postprocessing.data3;

import de.unileipzig.irpact.commons.util.data.count.CountMap2D;
import de.unileipzig.irpact.commons.util.data.count.CountMap3D;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.*;
import java.util.function.BiPredicate;

/**
 * @author Daniel Abitz
 */
public abstract class AnnualEnumeratedAdoptionData<T> {

    protected CountMap2D<Product, T> initial = new CountMap2D<>();
    protected CountMap3D<Integer, Product, T> data = new CountMap3D<>();
    protected BiPredicate<? super ConsumerAgent, ? super AdoptedProduct> filter;
    protected boolean cumulated = false;

    public AnnualEnumeratedAdoptionData() {
    }

    public String printData() {
        StringBuilder sb = new StringBuilder();
        for(int year: data.getFirstKeys()) {
            for(Product p: data.getSecondKeys(year)) {
                for(T t: data.getThirdKeys(year, p)) {
                    int count = data.getCount(year, p, t);
                    sb.append(year)
                            .append(";").append(p.getName())
                            .append(";").append(t)
                            .append(";").append(count);
                    sb.append("\n");
                }
            }
        }
        return sb.toString();
    }

    public void setFilter(BiPredicate<? super ConsumerAgent, ? super AdoptedProduct> filter) {
        this.filter = filter;
    }

    public CountMap2D<Product, T> getInitial() {
        return initial;
    }

    public CountMap3D<Integer, Product, T> getData() {
        return data;
    }

    protected void setCumulated(boolean cumulated) {
        this.cumulated = cumulated;
    }

    public boolean isCumulated() {
        return cumulated;
    }

    public boolean isNotCumulated() {
        return !cumulated;
    }

    public void analyse(SimulationEnvironment environment) {
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            for(ConsumerAgent ca: cag.getAgents()) {
                for(AdoptedProduct ap: ca.getAdoptedProducts()) {
                    if(test(ca, ap)) {
                        update(ca, ap);
                    }
                }
            }
        }
    }

    protected boolean test(ConsumerAgent ca, AdoptedProduct ap) {
        return filter == null || filter.test(ca, ap);
    }

    public int getInitialCount(Product product, T value) {
        return initial.getCount(product, value);
    }

    public int getInitialCount(Product product, Collection<? extends T> values) {
        int total = 0;
        for(T value: values) {
            total += getInitialCount(product, value);
        }
        return total;
    }

    public int getCount(int year, Product product, Collection<? extends T> values) {
        int total = 0;
        for(T value: values) {
            total += getCount(year, product, value);
        }
        return total;
    }

    public int getCount(int year, Product product, T value) {
        return data.getCount(year, product, value);
    }

    public int sumAll(Product product) {
        int total = 0;
        for(int year: data.getFirstKeys()) {
            for(T value: data.getThirdKeys(year, product)) {
                total += getCount(year, product, value);
            }
        }
        return total;
    }

    public void update(ConsumerAgent ca, AdoptedProduct ap) {
        if(ap.isInitial()) {
            updateInitial(ca, ap);
        } else {
            update(ap.getYear(), ca, ap);
        }
    }

    protected abstract void updateInitial(ConsumerAgent ca, AdoptedProduct ap);

    protected abstract void update(int year, ConsumerAgent ca, AdoptedProduct ap);

    protected abstract AnnualEnumeratedAdoptionData<T> newInstance();

    public AnnualEnumeratedAdoptionData<T> cumulate() {
        NavigableSet<Integer> years = new TreeSet<>(data.getFirstKeys());
        return cumulate(years.first(), years.last());
    }

    public AnnualEnumeratedAdoptionData<T> cumulate(int first, int last) {
        NavigableSet<Integer> years = new TreeSet<>();
        for(int y = first; y < last; y++) {
            years.add(y);
        }
        return cumulate(new ArrayList<>(years));
    }

    public AnnualEnumeratedAdoptionData<T> cumulate(List<Integer> years) {
        if(isCumulated()) {
            return this;
        }

        AnnualEnumeratedAdoptionData<T> cumulated = newInstance();
        cumulated.setCumulated(true);
        Set<Product> products = data.getAllSecondKeys();
        Set<T> values = new LinkedHashSet<>();
        initial.getAllSecondsKeys(values);
        data.getAllThirdKeys(values);
        for(Product product: products) {
            for(T value: values) {
                cumulated.initial.init(product, value, initial.getCount(product, value));
                for(int i = 0; i < years.size(); i++) {
                    int thisYear = years.get(i);
                    int thisCount = getCount(thisYear, product, value);
                    if(i > 0) {
                        int preYear = years.get(i - 1);
                        int preCount = cumulated.getCount(preYear, product, value);
                        cumulated.data.init(thisYear, product, value, thisCount + preCount);
                    } else {
                        cumulated.data.init(thisYear, product, value, thisCount + initial.getCount(product, value));
                    }
                }
            }
        }
        return cumulated;
    }

    public AnnualEnumeratedAdoptionData<T> uncumulate() {
        if(isNotCumulated()) {
            return this;
        }

        throw new UnsupportedOperationException("initial bug");
//        AnnualEnumeratedAdoptionData<T> uncumulated = newInstance();
//        uncumulated.setCumulated(false);
//        List<Integer> years = new ArrayList<>(data.getFirstKeys());
//        Collections.sort(years);
//        Set<Product> products = data.getAllSecondKeys();
//        Set<T> values = data.getAllThirdKeys();
//        for(Product product: products) {
//            for(T value: values) {
//                for(int i = years.size() - 1; i >= 1; i--) {
//                    int thisYear = years.get(i);
//                    int preYear = years.get(i - 1);
//                    int thisCount = getCount(thisYear, product, value);
//                    int preCount = getCount(preYear, product, value);
//                    uncumulated.data.init(thisYear, product, value, thisCount - preCount);
//                }
//            }
//        }
//        return uncumulated;
    }
}
