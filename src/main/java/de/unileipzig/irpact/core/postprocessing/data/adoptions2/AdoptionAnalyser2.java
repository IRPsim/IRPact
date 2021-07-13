package de.unileipzig.irpact.core.postprocessing.data.adoptions2;

import de.unileipzig.irpact.commons.util.data.VarCollection;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.postprocessing.data.ResultProcessor;
import de.unileipzig.irpact.core.postprocessing.data.ResultWriter;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface AdoptionAnalyser2 extends ResultProcessor, ResultWriter {

    @Override
    default void apply(SimulationEnvironment environment) {
        for(ConsumerAgent agent: environment.getAgents().iterableConsumerAgents()) {
            for(AdoptedProduct product: agent.getAdoptedProducts()) {
                add(agent, product);
            }
        }
    }

    @Override
    default void applyAndWrite(SimulationEnvironment environment) {
        apply(environment);
        write();
    }

    void write();

    boolean add(AdoptionEntry2 info);

    default void add(ConsumerAgent agent, AdoptedProduct product) {
        add(new BasicAdoptionEntry2(agent, product));
    }

    default void addAll(Iterable<? extends AdoptionEntry2> infos) {
        for(AdoptionEntry2 info: infos) {
            add(info);
        }
    }

    default void addAll(Iterator<? extends AdoptionEntry2> infos) {
        while(infos.hasNext()) {
            add(infos.next());
        }
    }

    default void addAll(Stream<? extends AdoptionEntry2> infos) {
        infos.forEach(this::add);
    }

    VarCollection getData();
}
