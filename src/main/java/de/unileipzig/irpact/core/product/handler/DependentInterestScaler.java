package de.unileipzig.irpact.core.product.handler;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.util.data.map.Map3;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class DependentInterestScaler extends DefaultInterestHandler {

    protected Map3<Product, ConsumerAgent, Double> interestValues = Map3.newConcurrentHashMap();
    protected UnivariateDoubleDistribution distribution;
    protected String referenceAttributeName;

    public void setReferenceAttributeName(String referenceAttributeName) {
        this.referenceAttributeName = referenceAttributeName;
    }

    public String getReferenceAttributeName() {
        return referenceAttributeName;
    }

    public void setDistribution(UnivariateDoubleDistribution distribution) {
        this.distribution = distribution;
    }

    public UnivariateDoubleDistribution getDistribution() {
        return distribution;
    }

    @Override
    public void handleProduct(SimulationEnvironment environment, Product product) {
        initalizeMapping(environment, product);
        super.handleProduct(environment, product);
    }

    protected void initalizeMapping(SimulationEnvironment environment, Product product) {
        trace("[{}] initialize value mapping for product '{}'", product.getName());

        List<Double> interestList = new ArrayList<>();
        List<SortableAgent> agentList = new ArrayList<>();

        for(ConsumerAgent agent: environment.getAgents().iterableConsumerAgents()) {
            if(agent.hasAttribute(getReferenceAttributeName())) {
                double referenceValue = getReferenceValue(environment, agent, product);
                double interest = distribution.drawDoubleValue();

                agentList.add(new SortableAgent(agent, referenceValue));
                interestList.add(interest);
            }
        }

        Collections.sort(interestList);
        Collections.sort(agentList);
        interestValues.clear(product);

        for(int i = 0; i < interestList.size(); i++) {
            ConsumerAgent agent = agentList.get(i).getAgent();
            double referenceValue = agentList.get(i).getReferenceValue();
            double interest = interestList.get(i);

            interestValues.put(product, agent, interest);

            trace("[{}] update agent={}, product={}, reference attribute {}={}, update attribute {}={}",
                    getName(),
                    agent.getName(),
                    product.getName(),
                    referenceAttributeName,
                    referenceValue,
                    interestAttributeName,
                    interest
            );
        }
    }

    protected double getReferenceValue(SimulationEnvironment environment, ConsumerAgent agent, Product product) {
        return environment.getAttributeHelper().getDouble(agent, product, getReferenceAttributeName(), false);
    }

    @Override
    protected double getInitialProductInterest(SimulationEnvironment environment, ConsumerAgent agent, Product product) {
        Double value = interestValues.get(product, agent);
        if(value == null) {
            throw new NoSuchElementException("missing data for agent '" + agent.getName() + "'");
        } else {
            return value;
        }
    }

    /**
     * @author Daniel Abitz
     */
    private static final class SortableAgent implements Comparable<SortableAgent> {

        private final ConsumerAgent agent;
        private final double referenceValue;

        private SortableAgent(ConsumerAgent agent, double referenceValue) {
            this.agent = agent;
            this.referenceValue = referenceValue;
        }

        private ConsumerAgent getAgent() {
            return agent;
        }

        private double getReferenceValue() {
            return referenceValue;
        }

        @Override
        public int compareTo(SortableAgent o) {
            return Double.compare(referenceValue, o.referenceValue);
        }
    }
}
