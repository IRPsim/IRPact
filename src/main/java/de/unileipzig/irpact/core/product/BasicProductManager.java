package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.ProcessModelManager;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class BasicProductManager implements ProductManager {

    private SimulationEnvironment environment;
    private Map<String, ProductGroup> products;

    public BasicProductManager() {
        this(new LinkedHashMap<>());
    }

    public BasicProductManager(Map<String, ProductGroup> products) {
        this.products = products;
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public int getChecksum() {
        return Objects.hash(
                ChecksumComparable.getCollChecksum(getGroups())
        );
    }

    @Override
    public void initialize() {
    }

    @Override
    public void preAgentCreationValidation() throws ValidationException {
    }

    @Override
    public void makeKnownInSimulation(Product product) {
        AgentManager agentManager = environment.getAgents();

        for(ConsumerAgentGroup cag: agentManager.getConsumerAgentGroups()) {
            for(ConsumerAgent ca: cag.getAgents()) {
                ca.updateProductRelatedAttributes(product);
            }
        }

        ProcessModelManager processModelManager = environment.getProcessModels();
        for(ProcessModel processModel: processModelManager.getProcessModels()) {
            processModel.handleNewProduct(product);
        }
    }

    public boolean has(String name) {
        return products.containsKey(name);
    }

    public void add(ProductGroup group) {
        products.put(group.getName(), group);
    }

    @Override
    public Collection<ProductGroup> getGroups() {
        return products.values();
    }

    @Override
    public ProductGroup getGroup(String name) {
        return products.get(name);
    }
}
