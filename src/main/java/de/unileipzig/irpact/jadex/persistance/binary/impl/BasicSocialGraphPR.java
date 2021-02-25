package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.network.BasicSocialGraph;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;

/**
 * @author Daniel Abitz
 */
public class BasicSocialGraphPR implements Persister<BasicSocialGraph>, Restorer<BasicSocialGraph> {

    public static final BasicSocialGraphPR INSTANCE = new BasicSocialGraphPR();

    @Override
    public Class<BasicSocialGraph> getType() {
        return BasicSocialGraph.class;
    }

    @Override
    public Persistable persist(BasicSocialGraph object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        //Knoten werden ueber Agenten rekonstruiert
        //trick: wir speichern keine Kantenliste, sondern rekonstruieren diese auch ad hoc
        for(SocialGraph.Type type: SocialGraph.Type.values()) {
            for(SocialGraph.Edge edge: object.getEdges(type)) {
                manager.ensureGetUID(edge);
            }
        }
        return data;
    }

    @Override
    public BasicSocialGraph initalize(Persistable persistable) {
        return new BasicSocialGraph();
    }

    //Alles in Edge ausgelagert
    @Override
    public void setup(Persistable persistable, BasicSocialGraph object, RestoreManager manager) {
    }

    @Override
    public void finalize(Persistable persistable, BasicSocialGraph object, RestoreManager manager) {
    }
}
