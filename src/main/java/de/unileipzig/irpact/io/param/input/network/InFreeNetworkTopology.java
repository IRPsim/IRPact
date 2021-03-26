package de.unileipzig.irpact.io.param.input.network;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.spatial.DistanceEvaluator;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.topology.FreeNetworkTopology;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.*;

import static de.unileipzig.irpact.io.param.IOConstants.NETWORK;
import static de.unileipzig.irpact.io.param.IOConstants.TOPOLOGY;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InFreeNetworkTopology implements InGraphTopologyScheme {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), NETWORK, TOPOLOGY, thisName());
        addEntry(res, thisClass(), "initialWeight");
        addEntry(res, thisClass(), "distanceEvaluator");
        addEntry(res, thisClass(), "numberOfTies");

        res.newEntryBuilder()
                .setGamsIdentifier("Knotenanzahl je KG")
                .setGamsDescription("Knotenanzahl")
                .store(thisClass(), "numberOfTies");

    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public InDistanceEvaluator[] distanceEvaluator;

    @FieldDefinition
    public InNumberOfTies[] numberOfTies;

    @FieldDefinition
    public double initialWeight;

    public InFreeNetworkTopology() {
    }

    public InFreeNetworkTopology(String name, InDistanceEvaluator evaluator, InNumberOfTies[] numberOfTies, double initialWeight) {
        this._name = name;
        this.distanceEvaluator = new InDistanceEvaluator[]{evaluator};
        this.numberOfTies = numberOfTies;
        this.initialWeight = initialWeight;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public InDistanceEvaluator getDistanceEvaluator() throws ParsingException {
        return ParamUtil.getInstance(distanceEvaluator, "distanceEvaluator");
    }

    public void setDistanceEvaluator(InDistanceEvaluator distanceEvaluator) {
        this.distanceEvaluator = new InDistanceEvaluator[]{distanceEvaluator};
    }

    public InNumberOfTies[] getNumberOfTies() {
        return numberOfTies;
    }

    public void setNumberOfTies(Collection<? extends InNumberOfTies> ties) {
        this.numberOfTies = ties.toArray(new InNumberOfTies[0]);
    }

    public double getInitialWeight() {
        return initialWeight;
    }

    public void setInitialWeight(double initialWeight) {
        this.initialWeight = initialWeight;
    }

    @Override
    public Object parse(InputParser parser) throws ParsingException {
        Map<ConsumerAgentGroup, Integer> edgeCountMap = new LinkedHashMap<>();
        for(InNumberOfTies entry: getNumberOfTies()) {
            for(InConsumerAgentGroup inCag: entry.getConsumerAgentGroups()) {
                ConsumerAgentGroup cag = parser.parseEntityTo(inCag);
                edgeCountMap.put(cag, entry.getCount());
            }
        }

        AgentManager agentManager = parser.getEnvironment().getAgents();
        if(edgeCountMap.size() != agentManager.getConsumerAgentGroups().size()) {
            Set<ConsumerAgentGroup> cagSet = new LinkedHashSet<>(agentManager.getConsumerAgentGroups());
            cagSet.removeAll(edgeCountMap.keySet());
            for(ConsumerAgentGroup cag: cagSet) {
                LOGGER.error("missing NumberOfTies-ConsumerGroup: '{}'", cag.getName());
            }
            throw new ParsingException(cagSet.size() + " NumberOfTies-ConsumerGroup(s) missing");
        }

        DistanceEvaluator distEval = parser.parseEntityTo(getDistanceEvaluator());
        Rnd rnd = parser.deriveRnd();
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "FreeNetworkTopology '{}' uses seed: {}", getName(), rnd.getInitialSeed());

        return new FreeNetworkTopology(
                SocialGraph.Type.COMMUNICATION,
                getName(),
                edgeCountMap,
                agentManager.getConsumerAgentGroupAffinityMapping(),
                distEval,
                getInitialWeight(),
                rnd
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InFreeNetworkTopology)) return false;
        InFreeNetworkTopology topology = (InFreeNetworkTopology) o;
        return Double.compare(topology.initialWeight, initialWeight) == 0 && Objects.equals(_name, topology._name) && Arrays.equals(distanceEvaluator, topology.distanceEvaluator) && Arrays.equals(numberOfTies, topology.numberOfTies);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(_name, initialWeight);
        result = 31 * result + Arrays.hashCode(distanceEvaluator);
        result = 31 * result + Arrays.hashCode(numberOfTies);
        return result;
    }

    @Override
    public String toString() {
        return "InFreeNetworkTopology{" +
                "_name='" + _name + '\'' +
                ", distanceEvaluator=" + Arrays.toString(distanceEvaluator) +
                ", numberOfTies=" + Arrays.toString(numberOfTies) +
                ", initialWeight=" + initialWeight +
                '}';
    }
}
