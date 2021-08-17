package de.unileipzig.irpact.core.product.initial;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.util.MapSupplier;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public class ConsumerGroupBasedInitialAdoption extends NameableBase implements InitialAdoptionHandler, LoggingHelper {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ConsumerGroupBasedInitialAdoption.class);

    protected MapSupplier supplier;
    protected Map<ProductGroup, String> firstAttributeNames;
    protected Map<ProductGroup, String> secondAttributeNames;
    protected Map<ProductGroup, String> validationAttributeNames;
    protected Map<ProductGroup, Map<String, Map<String, Double>>> initialAdopterShares;
    protected Rnd rnd;
    protected boolean throwExceptionIfMissing = false;

    public ConsumerGroupBasedInitialAdoption() {
        this(MapSupplier.LINKED);
    }

    public ConsumerGroupBasedInitialAdoption(MapSupplier supplier) {
        this.supplier = supplier;
        firstAttributeNames = supplier.newMap();
        secondAttributeNames = supplier.newMap();
        initialAdopterShares = supplier.newMap();
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public IRPSection getDefaultResultSection() {
        return IRPSection.INITIALIZATION_PARAMETER;
    }

    public void setRnd(Rnd rnd) {
        this.rnd = rnd;
    }

    public Rnd getRnd() {
        return rnd;
    }

    public void register(
            ProductGroup group,
            String firstAttributeName,
            String secondAttributeName,
            String validationAttributeName) {
        firstAttributeNames.put(group, firstAttributeName);
        secondAttributeNames.put(group, secondAttributeName);
        validationAttributeNames.put(group, validationAttributeName);
    }

    public void setShare(
            ProductGroup group,
            String firstAttribute,
            String secondAttribute,
            double share) {
        Map<String, Map<String, Double>> map0 = initialAdopterShares.computeIfAbsent(group, _group -> supplier.newMap());
        Map<String, Double> map1 = map0.computeIfAbsent(firstAttribute, _firstAttribute -> supplier.newMap());
        map1.put(secondAttribute, share);
    }

    protected static String getStringValue(
            SimulationEnvironment environment,
            ConsumerAgent agent,
            String attributeName) {
        return environment.getAttributeHelper().findStringValue(agent, attributeName);
    }

    protected static boolean getBooleanValue(
            SimulationEnvironment environment,
            ConsumerAgent agent,
            String attributeName) {
        return environment.getAttributeHelper().findBooleanValue(agent, attributeName);
    }

    protected static String getAttributeName(Map<ProductGroup, String> map, Product product) {
        if(product == null) {
            throw new NullPointerException("Product");
        }
        ProductGroup pg = product.getGroup();
        if(pg == null) {
            throw new NullPointerException("ProductGroup");
        }
        String attributeName = map.get(pg);
        if(attributeName == null) {
            throw new IllegalArgumentException("missing attribute for product group '" + pg.getName() + "'");
        }
        return attributeName;
    }

    protected String getFirstAttribute(Product product) {
        return getAttributeName(firstAttributeNames, product);
    }

    protected String getSecondAttribute(Product product) {
        return getAttributeName(secondAttributeNames, product);
    }

    protected String getValidationAttribute(Product product) {
        return getAttributeName(validationAttributeNames, product);
    }

    protected boolean isValid(
            SimulationEnvironment environment,
            ConsumerAgent ca,
            String validationAttribute) {
        return getBooleanValue(environment, ca, validationAttribute);
    }

    protected double getShare(
            ConsumerAgent ca,
            Product product,
            String firstAttribute,
            String secondAttribute) {
        ProductGroup pg = product.getGroup();

        Map<String, Map<String, Double>> map0 = initialAdopterShares.get(pg);
        if(map0 == null) {
            if(throwExceptionIfMissing) {
                throw new NoSuchElementException("missing data for: " + ca.getName() + " " + product.getName());
            } else {
                return 0.0;
            }
        }

        Map<String, Double> map1 = map0.get(firstAttribute);
        if(map1 == null) {
            if(throwExceptionIfMissing) {
                throw new NoSuchElementException("missing data for: " + ca.getName() + " " + product.getName() + " " + firstAttribute);
            } else {
                return 0.0;
            }
        }

        Double share = map1.get(secondAttribute);
        if(share == null) {
            if(throwExceptionIfMissing) {
                throw new NoSuchElementException("missing data for: " + ca.getName() + " " + product.getName() + " " + firstAttribute + " " + secondAttribute);
            } else {
                return 0.0;
            }
        }

        return share;
    }

    @Override
    public void handleProduct(SimulationEnvironment environment, Product product) {
        final String firstAttribute = getFirstAttribute(product);
        final String secondAttribute = getSecondAttribute(product);
        final String validationAttribute = getValidationAttribute(product);

        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            for(ConsumerAgent ca: cag.getAgents()) {
                if(isValid(environment, ca, validationAttribute)) {
                    double share = getShare(ca, product, firstAttribute, secondAttribute);
                    if(share == 0.0) {
                        trace("share = 0, skip consumer agent '{}'", ca.getName());
                        continue;
                    }

                    double draw = getRnd().nextDouble();
                    boolean isAdopter = draw < share;
                    trace(
                            "Is (valid) consumer agent '{}' initial adopter of product '{}'? {} ({} < {})",
                            ca.getName(), product.getName(), isAdopter, draw, share
                    );
                    if(isAdopter) {
                        ca.adoptInitial(product);
                    }
                } else {
                    trace("consumer agent '{}' not valid for initial adoption", ca.getName());
                }
            }
        }
    }
}
