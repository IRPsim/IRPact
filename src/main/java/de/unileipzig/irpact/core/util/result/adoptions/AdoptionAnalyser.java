package de.unileipzig.irpact.core.util.result.adoptions;

import de.unileipzig.irpact.commons.util.data.VarCollection;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.result.ResultProcessor;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface AdoptionAnalyser extends ResultProcessor {

    @Override
    default void apply(SimulationEnvironment environment) {
        for(ConsumerAgent agent: environment.getAgents().iterableConsumerAgents()) {
            for(AdoptedProduct product: agent.getAdoptedProducts()) {
                BasicAdoptionInfo info = new BasicAdoptionInfo(agent, product);
                add(info);
            }
        }
    }

    void add(AdoptionInfo info);

    default void add(ConsumerAgent agent, AdoptedProduct product) {
        add(new BasicAdoptionInfo(agent, product));
    }

    default void addAll(Iterable<? extends AdoptionInfo> infos) {
        for(AdoptionInfo info: infos) {
            add(info);
        }
    }

    default void addAll(Iterator<? extends AdoptionInfo> infos) {
        while(infos.hasNext()) {
            add(infos.next());
        }
    }

    default void addAll(Stream<? extends AdoptionInfo> infos) {
        infos.forEach(this::add);
    }

    VarCollection getData();
}
