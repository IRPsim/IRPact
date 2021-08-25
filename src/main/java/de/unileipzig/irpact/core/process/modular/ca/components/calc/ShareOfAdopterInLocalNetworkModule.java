package de.unileipzig.irpact.core.process.modular.ca.components.calc;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.network.filter.NodeFilter;
import de.unileipzig.irpact.core.process.filter.ProcessPlanNodeFilterScheme;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentCalculationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.base.AbstractConsumerAgentModule;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class ShareOfAdopterInLocalNetworkModule extends AbstractConsumerAgentModule implements ConsumerAgentCalculationModule {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ShareOfAdopterInLocalNetworkModule.class);

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

    @Override
    public void preAgentCreationValidation() throws ValidationException {
        super.preAgentCreationValidation();
        if(nodeFilterScheme == null) {
            throw new ValidationException("missing node filter scheme");
        }
    }

    protected double weight = 1.0;
    public void setWeight(double weight) {
        this.weight = weight;
    }
    public double getWeight() {
        return weight;
    }

    protected ProcessPlanNodeFilterScheme nodeFilterScheme;
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

    protected NodeFilter getFilter(ConsumerAgentData data) {
        if(data.has(getName())) {
            return data.retrieveAs(getName(), NodeFilter.class);
        } else {
            NodeFilter filter = getValidNodeFilterScheme().createFilter(data.getPlan());
            data.store(getName(), filter);
            return filter;
        }
    }

    @Override
    public double calculate(ConsumerAgentData input) throws Throwable {
        NodeFilter filter = getFilter(input);
        double value = getShareOfAdopterInLocalNetwork(input.getProduct(), filter);
        return getWeight() * value;
    }
}
