package de.unileipzig.irpact.core.postprocessing.data3;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.performance.PerformanceMetric;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class PerformanceCalculator {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(PerformanceCalculator.class);

    protected PerformanceCalculator() {
    }

    protected static boolean isInvalidZip(ConsumerAgent agent, String zipKey, String zip) {
        Attribute zipAttr = agent.findAttribute(zipKey);
        return !Objects.equals(zip, zipAttr.asValueAttribute().getStringValue());
    }

    protected static boolean isInvalidZip(ConsumerAgent agent, String zipKey, Collection<String> validZips) {
        Attribute zipAttr = agent.findAttribute(zipKey);
        return !validZips.contains(zipAttr.asValueAttribute().getStringValue());
    }

    protected static boolean isAdopter(ConsumerAgent agent, String zipKey, String zip, Product product, int year) {
        if(isInvalidZip(agent, zipKey, zip)) {
            return false;
        }

        return isAdopter(agent, product, year);
    }

    protected static boolean isAdopter(ConsumerAgent agent, String zipKey, Collection<String> validZips, Product product, int year) {
        if(isInvalidZip(agent, zipKey, validZips)) {
            return false;
        }

        return isAdopter(agent, product, year);
    }

    protected static boolean isAdopter(ConsumerAgent agent, Product product, int year) {
        if(agent.hasAdopted(product)) {
            AdoptedProduct adoptedProduct = agent.getAdoptedProduct(product);
            return adoptedProduct.hasTimestamp() && adoptedProduct.getYear() == year;
        } else {
            return false;
        }
    }

    protected static int getNumberOfAdoptions(SimulationEnvironment environment, String zipKey, Collection<String> validZips, Product product, int year) {
        int count = 0;
        for(ConsumerAgent agent: environment.getAgents().iterableConsumerAgents()) {
            if(isAdopter(agent, zipKey, validZips, product, year)) {
                count++;
            }
        }
        return count;
    }

    protected static int getNumberOfAdoptions(SimulationEnvironment environment, String zipKey, String zip, Product product, int year) {
        int count = 0;
        for(ConsumerAgent agent: environment.getAgents().iterableConsumerAgents()) {
            if(isAdopter(agent, zipKey, zip, product, year)) {
                count++;
            }
        }
        return count;
    }

    public static double calculateGlobal(
            PerformanceMetric metric,
            RealAdoptionData realData,
            SimulationEnvironment environment,
            Product product,
            String zipKey,
            List<String> allZips,
            List<Integer> years) {

        double[] realAdoptions = new double[years.size()];
        double[] simuAdoptions = new double[years.size()];

        List<String> validZips = new ArrayList<>();
        List<String> invalidZips = new ArrayList<>();

        realData.getValidZips(allZips, validZips);
        realData.getInvalidZips(allZips, invalidZips);

        LOGGER.debug("all zips: {}", allZips);
        LOGGER.debug("valid zips: {}", validZips);
        LOGGER.info("invalid zips: {}", invalidZips);

        for(int i = 0; i < years.size(); i++) {
            int year = years.get(i);
            realAdoptions[i] = realData.getUncumulated(year, validZips);
            simuAdoptions[i] = getNumberOfAdoptions(environment, zipKey, validZips, product, year);
        }

        return metric.calc(realAdoptions, simuAdoptions);
    }

    public static double calculateZIP(
            PerformanceMetric metric,
            RealAdoptionData realData,
            SimulationEnvironment environment,
            Product product,
            String zipKey,
            String zip,
            List<Integer> years) {

        if(!realData.hasZip(zip)) {
            LOGGER.warn("unsupported zip: {}", zip);
            return Double.NaN;
        }

        double[] realAdoptions = new double[years.size()];
        double[] simuAdoptions = new double[years.size()];

        for(int i = 0; i < years.size(); i++) {
            int year = years.get(i);
            realAdoptions[i] = realData.getUncumulated(year, zip);
            simuAdoptions[i] = getNumberOfAdoptions(environment, zipKey, zip, product, year);
        }

        return metric.calc(realAdoptions, simuAdoptions);
    }
}
