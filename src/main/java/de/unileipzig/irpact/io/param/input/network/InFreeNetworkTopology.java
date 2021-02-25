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
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;

/**
 * @author Daniel Abitz
 */
@Definition
public class InFreeNetworkTopology implements InGraphTopologyScheme {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InFreeNetworkTopology.class,
                res.getCachedElement("Netzwerk"),
                res.getCachedElement("Topologie"),
                res.getCachedElement("Freie Topologie")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Legt den Evaluator für die Abstände zwischen den Agenten fest.")
                .setGamsDescription("Evaluator für Abstände")
                .store(InFreeNetworkTopology.class, "distanceEvaluator");

        res.newEntryBuilder()
                .setGamsIdentifier("Knotenanzahl je KG")
                .setGamsDescription("Knotenanzahl")
                .store(InFreeNetworkTopology.class, "numberOfTies");

        res.newEntryBuilder()
                .setGamsIdentifier("Initiale Kantengewicht")
                .setGamsDescription("Initiale Gewicht der Kanten")
                .store(InCompleteGraphTopology.class, "initialWeight");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InFreeNetworkTopology.class);

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

    public InDistanceEvaluator getDistanceEvaluator() {
        return distanceEvaluator[0];
    }

    public InNumberOfTies[] getNumberOfTies() {
        return numberOfTies;
    }

    public double getInitialWeight() {
        return initialWeight;
    }

    @Override
    public Object parse(InputParser parser) throws ParsingException {
        Map<ConsumerAgentGroup, Integer> edgeCountMap = new HashMap<>();
        for(InNumberOfTies entry: getNumberOfTies()) {
            ConsumerAgentGroup cag = parser.parseEntityTo(entry.getCag());
            edgeCountMap.put(cag, entry.getCount());
        }

        AgentManager agentManager = parser.getEnvironment().getAgents();
        if(edgeCountMap.size() != agentManager.getConsumerAgentGroups().size()) {
            Set<ConsumerAgentGroup> cagSet = new HashSet<>(agentManager.getConsumerAgentGroups());
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
