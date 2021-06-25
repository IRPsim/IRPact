package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.network.BasicSocialGraph;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonData;
import de.unileipzig.irpact.core.persistence.binary.BinaryPRBase;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicEdgePR extends BinaryPRBase<BasicSocialGraph.BasicEdge> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicEdgePR.class);

    public static final BasicEdgePR INSTANCE = new BasicEdgePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //persist
    //=========================

    @Override
    public Class<BasicSocialGraph.BasicEdge> getType() {
        return BasicSocialGraph.BasicEdge.class;
    }

    @Override
    protected BinaryJsonData doInitalizePersist(BasicSocialGraph.BasicEdge object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putInt(object.getType().id());
        data.putDouble(object.getWeight());

        manager.prepare(object.getSource().getAgent());
        manager.prepare(object.getTarget().getAgent());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicSocialGraph.BasicEdge object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getSource().getAgent()));
        data.putLong(manager.ensureGetUID(object.getTarget().getAgent()));
    }

    //=========================
    //restore
    //=========================


    @Override
    protected BasicSocialGraph.BasicEdge doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        return new BasicSocialGraph.BasicEdge();
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicSocialGraph.BasicEdge object, RestoreManager manager) throws RestoreException {
        BasicSocialGraph graph = manager.ensureGetSameClass(BasicSocialGraph.class);

        object.setType(SocialGraph.Type.get(data.getInt()));
        object.setWeight(data.getDouble());

        Agent srcAgent = manager.ensureGet(data.getLong());
        SocialGraph.Node srcNode;
        if(graph.hasNode(srcAgent)) {
            srcNode = graph.getNode(srcAgent);
        } else {
            srcNode = graph.addAgentAndGetNode(srcAgent);
            srcAgent.setSocialGraphNode(srcNode);
        }

        Agent tarAgent = manager.ensureGet(data.getLong());
        SocialGraph.Node tarNode;
        if(graph.hasNode(tarAgent)) {
            tarNode = graph.getNode(tarAgent);
        } else {
            tarNode = graph.addAgentAndGetNode(tarAgent);
            tarAgent.setSocialGraphNode(tarNode);
        }

        object.setSource(srcNode);
        object.setTarget(tarNode);

        graph.addEdgeDirect(object);
    }
}
