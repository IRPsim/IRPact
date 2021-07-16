package de.unileipzig.irpact.core.postprocessing.data.adoptions2.impl;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.util.data.VarCollection;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.*;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.AdoptionPhase;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public class AllAdoptions2 extends AbstractAdoptionAnalyser2 {

    public AllAdoptions2() {
    }

    @Override
    protected VarCollection newCollection() {
        return new VarCollection(
                String.class,           //0 agent name
                String.class,           //1 mileu
                Long.class,             //2 spatial info id
                String.class,           //3 zip
                String.class,           //4 product
                String.class,           //5 product group
                AdoptionPhase.class,    //6 phase
                ZonedDateTime.class     //7 time
        );
    }

    @Override
    protected String getTypeForLocalization() {
        return "allAgents";
    }

    @Override
    protected Function<? super Object[], ? extends Attribute[]> getMappingFunction() {
        return entry -> new Attribute[] {
                toStringAttribute(entry[0], IF_NULL_STR),
                toStringAttribute(entry[1], IF_NULL_STR),
                toDoubleAttribute(entry[2], IF_NULL_DOUBLE),
                toStringAttribute(entry[3], IF_NULL_STR),
                toStringAttribute(entry[4], IF_NULL_STR),
                toStringAttribute(entry[5], IF_NULL_STR),
                toStringAttribute(printPhase((AdoptionPhase) entry[6], IF_NULL_STR), IF_NULL_STR),
                toStringAttribute(printTime((ZonedDateTime) entry[7], IF_NULL_STR), IF_NULL_STR)
        };
    }

    @Override
    protected Function<? super Object[], ? extends String[]> getStringMappingFunction() {
        return entry -> new String[] {
                print(entry[0], IF_NULL_STR),
                print(entry[1], IF_NULL_STR),
                print(entry[2], IF_NULL_DOUBLE_STR),
                print(entry[3], IF_NULL_STR),
                print(entry[4], IF_NULL_STR),
                print(entry[5], IF_NULL_STR),
                printPhase((AdoptionPhase) entry[6], IF_NULL_STR),
                printTime((ZonedDateTime) entry[7], IF_NULL_STR)
        };
    }

    @Override
    public void apply(SimulationEnvironment environment) {
        for(ConsumerAgent agent: environment.getAgents().iterableConsumerAgents()) {
            add(agent);
        }
    }

    public void add(ConsumerAgent agent) {
        if(agent.getAdoptedProducts().isEmpty()) {
            addNonAdopter(agent);
        } else {
            addAdopter(agent);
        }
    }

    public void addAdopter(ConsumerAgent agent) {
        for(AdoptedProduct product: agent.getAdoptedProducts()) {
            add(agent, product);
        }
    }

    @Override
    public boolean add(AdoptionEntry2 entry) {
        getData().varAdd(
                entry.getAgent().getName(),
                entry.getAgentGroup().getName(),
                getSpatialInformationId(entry.getAgent()),
                getZip(entry.getAgent()),
                entry.getProductNameOrNull(),
                entry.getProductGroupNameOrNull(),
                entry.getPhase(),
                entry.getTimeOrNull()
        );
        return true;
    }

    public void addNonAdopter(ConsumerAgent agent) {
        getData().varAdd(
                agent.getName(),
                agent.getGroup().getName(),
                getSpatialInformationId(agent),
                getZip(agent),
                null,
                null,
                null,
                null
        );
    }

    @Override
    protected void add(int year, AdoptionEntry2 info) {
        throw new UnsupportedOperationException();
    }
}
