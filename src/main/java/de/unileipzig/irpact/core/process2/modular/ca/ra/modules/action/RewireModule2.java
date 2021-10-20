package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.action;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinities;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.InfoTag;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractCAVoidModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class RewireModule2
        extends AbstractCAVoidModule2
        implements RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RewireModule2.class);

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void validate() throws Throwable {
    }

    @Override
    public void initialize(SimulationEnvironment environment) throws Throwable {
    }

    @Override
    public void run(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleCall();

        ConsumerAgent agent = input.getAgent();
        SimulationEnvironment environment = input.getEnvironment();
        Rnd rnd = input.rnd();

        if(agent.tryAquireAttention()) {
            agent.actionPerformed();
            agent.aquireDataAccess(); //lock data
            agent.releaseAttention();
        } else {
            trace("[{}] rewire canceled, already occupied", agent.getName());
            return;
        }

        //data locked
        try {
            LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] rewire", agent.getName());

            SocialGraph graph = environment.getNetwork().getGraph();
            SocialGraph.Node node = agent.getSocialGraphNode();
            SocialGraph.LinkageInformation linkInfo = graph.getLinkageInformation(node);

            //remove
            SocialGraph.Node rndTarget = graph.getRandomTarget(node, SocialGraph.Type.COMMUNICATION, rnd);
            if(rndTarget == null) {
                logGraphUpdateEdgeRemoved(agent, null);
            } else {
                ConsumerAgentGroup tarCag = rndTarget.getAgent(ConsumerAgent.class).getGroup();
                SocialGraph.Edge edge = graph.getEdge(node, rndTarget, SocialGraph.Type.COMMUNICATION);
                graph.removeEdge(edge);
                logGraphUpdateEdgeRemoved(agent, rndTarget.getAgent());
                updateLinkCount(agent, linkInfo, tarCag, -1); //dec
            }

            ConsumerAgentGroupAffinities affinities = environment.getAgents()
                    .getConsumerAgentGroupAffinityMapping()
                    .get(agent.getGroup());

            SocialGraph.Node tarNode = null;
            ConsumerAgentGroup tarCag = null;
            while(tarNode == null) {
                if(affinities.isEmpty()) {
                    tarCag = null;
                    break;
                }

                tarCag = affinities.getWeightedRandom(rnd);

                int currentLinkCount = linkInfo.get(tarCag, SocialGraph.Type.COMMUNICATION);
                int maxTargetAgents = tarCag.getNumberOfAgents();
                if(tarCag == agent.getGroup()) {
                    maxTargetAgents -= 1;
                }

                trace("[{}] tar: {}, currentLinkCount: {}, max: {} (self: {})", agent.getName(), tarCag.getName(), currentLinkCount, maxTargetAgents, agent.getGroup() == tarCag);

                if(maxTargetAgents == currentLinkCount) {
                    affinities = affinities.createWithout(tarCag);
                    continue;
                }

                int unlinkedInCag = maxTargetAgents - currentLinkCount;

                tarNode = getRandomUnlinked(
                        agent,
                        environment,
                        rnd,
                        agent.getSocialGraphNode(),
                        tarCag,
                        unlinkedInCag
                );
            }

            if(tarNode == null) {
                LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] no valid rewire target found", agent.getName());
            } else {
                graph.addEdge(agent.getSocialGraphNode(), tarNode, SocialGraph.Type.COMMUNICATION, 1.0);
                logGraphUpdateEdgeAdded(agent, tarNode.getAgent());
                updateLinkCount(agent, linkInfo, tarCag, 1); //inc
            }
        } finally {
            agent.releaseDataAccess();
        }
    }

    protected void updateLinkCount(
            ConsumerAgent agent,
            SocialGraph.LinkageInformation linkInfo,
            ConsumerAgentGroup tarCag,
            int delta) {
        int oldLinkCount = linkInfo.get(tarCag, SocialGraph.Type.COMMUNICATION);
        linkInfo.update(tarCag, SocialGraph.Type.COMMUNICATION, delta);
        int newLinkCount = linkInfo.get(tarCag, SocialGraph.Type.COMMUNICATION);
        trace("[{}] update link count to cag '{}': {} -> {}", agent.getName(), tarCag.getName(), oldLinkCount, newLinkCount);
    }

    protected void logGraphUpdateEdgeAdded(Agent agent, Agent target) {
        LOGGER.trace(IRPSection.SIMULATION_PROCESS,
                "{} [{}] graph update, edge added: '{}' -> '{}'",
                InfoTag.GRAPH_UPDATE, agent.getName(),
                agent.getName(), target.getName()
        );
    }

    protected void logGraphUpdateEdgeRemoved(Agent agent, Agent target) {
        if(target == null) {
            LOGGER.trace(IRPSection.SIMULATION_PROCESS,
                    "{} [{}] graph not updated, no valid target found",
                    InfoTag.GRAPH_UPDATE, agent.getName()
            );
        } else {
            LOGGER.trace(IRPSection.SIMULATION_PROCESS,
                    "{} [{}] graph update, edge removed: '{}' -> '{}'",
                    InfoTag.GRAPH_UPDATE, agent.getName(),
                    agent.getName(), target.getName()
            );
        }
    }

    protected SocialGraph.Node getRandomUnlinked(
            ConsumerAgent agent,
            SimulationEnvironment environment,
            Rnd rnd,
            SocialGraph.Node srcNode,
            ConsumerAgentGroup tarCag,
            int unlinkedInCag) {
        SocialGraph graph = environment.getNetwork().getGraph();

        int rndId = rnd.nextInt(unlinkedInCag);
        int id = 0;

        for(Agent tar: tarCag.getAgents()) {
            if(tar == agent) {
                continue;
            }
            SocialGraph.Node tarNode = tar.getSocialGraphNode();
            if(graph.hasNoEdge(srcNode, tarNode, SocialGraph.Type.COMMUNICATION)) {
                if(id == rndId) {
                    return tarNode;
                } else {
                    id++;
                }
            }
        }

        return null;
    }
}
