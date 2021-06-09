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
                add(agent, product);
            }
        }
    }

    void add(AdoptionEntry info);

    default void add(ConsumerAgent agent, AdoptedProduct product) {
        add(new BasicAdoptionEntry(agent, product));
    }

    default void addAll(Iterable<? extends AdoptionEntry> infos) {
        for(AdoptionEntry info: infos) {
            add(info);
        }
    }

    default void addAll(Iterator<? extends AdoptionEntry> infos) {
        while(infos.hasNext()) {
            add(infos.next());
        }
    }

    default void addAll(Stream<? extends AdoptionEntry> infos) {
        infos.forEach(this::add);
    }

    VarCollection getData();
}
