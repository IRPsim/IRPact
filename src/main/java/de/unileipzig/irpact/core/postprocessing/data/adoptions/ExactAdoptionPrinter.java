package de.unileipzig.irpact.core.postprocessing.data.adoptions;

import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.InfoTag;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.postprocessing.data.ResultProcessor;

/**
 * @author Daniel Abitz
 */
public class ExactAdoptionPrinter implements ResultProcessor {

    public static final ExactAdoptionPrinter INSTANCE = new ExactAdoptionPrinter();

    public ExactAdoptionPrinter() {
    }

    @Override
    public void apply(SimulationEnvironment environment) {
        start();
        long total = 0;
        for(ConsumerAgent agent: environment.getAgents().iterableConsumerAgents()) {
            total += handleAgent(agent);
        }
        finish(total);
    }

    public void start() {
        IRPLogging.resultSeparator();
        IRPLogging.startResult(InfoTag.RESULT_EXACT_ADOPTIONS, true);
    }

    public void finish(long total) {
        IRPLogging.finishResult(InfoTag.RESULT_EXACT_ADOPTIONS);
        IRPLogging.resultWrite("total number of adoptions in this period: {}", total);
        IRPLogging.resultSeparator();
    }

    public int handleAgent(ConsumerAgent agent) {
        if(agent.getAdoptedProducts().isEmpty()) {
            handleNonadopter(agent);
            return 0;
        } else {
            for(AdoptedProduct product: agent.getAdoptedProducts()) {
                handleAdoption(agent, product);
            }
            return agent.getAdoptedProducts().size();
        }
    }

    public void handleNonadopter(ConsumerAgent agent) {
        IRPLogging.resultWrite(
                "{},-,-,-,-",
                agent.getName()
        );
    }

    public void handleAdoption(ConsumerAgent agent, AdoptedProduct product) {
        IRPLogging.resultWrite(
                "{},{},{},{},{}",
                agent.getName(),
                getSpatialInformationId(agent),
                product.getProduct().getName(),
                product.getPhase(),
                product.getTimestamp().getTime()
        );
    }

    protected static long getSpatialInformationId(ConsumerAgent agent) {
        SpatialInformation information = agent.getSpatialInformation();
        if(information == null) {
            return -2;
        }
        if(information.hasId()) {
            return information.getId();
        } else {
            SpatialAttribute attr = information.getAttribute(RAConstants.ID);
            if(attr == null) {
                return -1;
            } else {
                return attr.asValueAttribute().getLongValue();
            }
        }
    }
}
