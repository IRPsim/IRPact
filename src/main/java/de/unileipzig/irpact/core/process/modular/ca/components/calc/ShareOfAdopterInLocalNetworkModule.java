package de.unileipzig.irpact.core.process.modular.ca.components.calc;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.filter.NodeFilter;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.filter.ProcessPlanNodeFilterScheme;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentCalculationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.base.AbstractConsumerAgentModule;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class ShareOfAdopterInLocalNetworkModule extends AbstractConsumerAgentModule implements ConsumerAgentCalculationModule {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ShareOfAdopterInLocalNetworkModule.class);

    protected ProcessPlanNodeFilterScheme nodeFilterScheme;
    protected double weight = 1.0;

    public ShareOfAdopterInLocalNetworkModule() {
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                getName(),
                getWeight(),
                getNodeFilterScheme()
        );
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    public double getWeight() {
        return weight;
    }

    public void setNodeFilterScheme(ProcessPlanNodeFilterScheme nodeFilterScheme) {
        this.nodeFilterScheme = nodeFilterScheme;
    }
    public ProcessPlanNodeFilterScheme getNodeFilterScheme() {
        return nodeFilterScheme;
    }
    protected ProcessPlanNodeFilterScheme getValidNodeFilterScheme() {
        ProcessPlanNodeFilterScheme filter = getNodeFilterScheme();
        if(filter == null) {
            throw new NullPointerException("ProcessPlanNodeFilterScheme");
        }
        return filter;
    }

    protected final Map<ProcessPlan, NodeFilter> filters = new HashMap<>();
    protected NodeFilter getFilter(ProcessPlan plan) {
        NodeFilter filter = filters.get(plan);
        if(filter == null) {
            return getFilter0(plan);
        } else {
            return filter;
        }
    }
    protected synchronized NodeFilter getFilter0(ProcessPlan plan) {
        NodeFilter filter = filters.get(plan);
        if(filter == null) {
            filter = getValidNodeFilterScheme().createFilter(plan);
            filters.put(plan, filter);
        }
        return filter;
    }

    @Override
    public double calculate(ConsumerAgentData input) {
        NodeFilter filter = getFilter(input.getPlan());
        double value = getShareOfAdopterInLocalNetwork(input.getProduct(), filter);
        return getWeight() * value;
    }
}
