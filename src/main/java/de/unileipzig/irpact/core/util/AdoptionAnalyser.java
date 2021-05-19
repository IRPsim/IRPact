package de.unileipzig.irpact.core.util;

import de.unileipzig.irpact.commons.util.data.MutableInt;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;

import java.util.*;

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
public final class AdoptionAnalyser {

    private final NavigableSet<Integer> years = new TreeSet<>();
    private final List<Entry> entries = new ArrayList<>();

    public AdoptionAnalyser() {
    }

    public Set<Integer> getYears() {
        return years;
    }

    public void add(ConsumerAgent agent, AdoptedProduct adoption) {
        entries.add(new Entry(agent, adoption));
        years.add(adoption.getYear());
    }

    @SuppressWarnings("RedundantIfStatement")
    public Annual annual(int year, ConsumerAgentGroup cag, ProductGroup pg) {
        MutableInt adoptions = MutableInt.zero();
        entries.stream()
                .filter(entry -> {
                    if(entry.getYear() != year) {
                        return false;
                    }
                    if(entry.getConsumerAgentGroup() != cag) {
                        return false;
                    }
                    if(entry.getProductGroup() != pg) {
                        return false;
                    }
                    return true;
                })
                .forEach(entry -> {
                    adoptions.inc();
                });
        return new Annual(
                year,
                cag.getNumberOfAgents(),
                adoptions.get()
        );
    }

    public Result result(ConsumerAgentGroup cag, ProductGroup pg) {
        Result result = new Result();
        for(int year: getYears()) {
            Annual annual = annual(year, cag, pg);
            result.add(annual);
        }
        return result;
    }

    /**
     * @author Daniel Abitz
     */
    public static final class Result {

        private final NavigableMap<Integer, Annual> annualData = new TreeMap<>();

        private Result() {
        }

        public void add(Annual annual) {
            if(annualData.containsKey(annual.getYear())) {
                throw new IllegalArgumentException("year " + annual.getYear() + " already exists");
            }
            annualData.put(annual.getYear(), annual);
        }

        public Set<Integer> getYears() {
            return annualData.keySet();
        }

        public Annual get(int year) {
            return annualData.get(year);
        }

        public int getAdoptions(int until) {
            return annualData.values()
                    .stream()
                    .filter(annual -> annual.getYear() <= until)
                    .mapToInt(Annual::getAdoptions)
                    .sum();
        }

        public double getAdoptionShare(int until) {
            return annualData.values()
                    .stream()
                    .filter(annual -> annual.getYear() <= until)
                    .mapToDouble(Annual::getAdoptionShare)
                    .sum();
        }

        @Override
        public String toString() {
            return "Result{" + annualData + '}';
        }
    }

    /**
     * @author Daniel Abitz
     */
    public static final class Annual {

        private final int year;
        private final int total;
        private final int adoptions;

        private Annual(int year, int total, int adoptions) {
            this.year = year;
            this.total = total;
            this.adoptions = adoptions;
        }

        public int getYear() {
            return year;
        }

        public int getTotal() {
            return total;
        }

        public int getAdoptions() {
            return adoptions;
        }

        public double getAdoptionShare() {
            return (double) adoptions / (double) total;
        }

        @Override
        public String toString() {
            return "Annual{" +
                    "year=" + year +
                    ", total=" + total +
                    ", adoptions=" + adoptions +
                    '}';
        }
    }

    /**
     * @author Daniel Abitz
     */
    public static final class Entry {

        private final ConsumerAgent agent;
        private final AdoptedProduct adoptedProduct;

        private Entry(ConsumerAgent agent, AdoptedProduct adoptedProduct) {
            this.agent = agent;
            this.adoptedProduct = adoptedProduct;
        }

        public ConsumerAgent getAgent() {
            return agent;
        }

        public int getTotalAgents() {
            return agent.getGroup().getNumberOfAgents();
        }

        public ConsumerAgentGroup getConsumerAgentGroup() {
            return agent.getGroup();
        }

        public AdoptedProduct getAdoptedProduct() {
            return adoptedProduct;
        }

        public Product getProduct() {
            return adoptedProduct.getProduct();
        }

        public ProductGroup getProductGroup() {
            return adoptedProduct.getProduct().getGroup();
        }

        public int getYear() {
            return adoptedProduct.getYear();
        }
    }
}
