package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.exception.UncheckedParsingException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.network.BasicSocialGraph;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.SupportedGraphStructure;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;

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
        data.putInt(object.getStructure().getID());
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
    public BasicSocialGraph initalize(Persistable persistable, RestoreManager manager) throws RestoreException {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        int graphStructureId = data.getInt();
        SupportedGraphStructure structure = SupportedGraphStructure.get(graphStructureId);
        if(structure == SupportedGraphStructure.UNKNOWN) {
            throw new RestoreException("Unsupported structure");
        }
        return new BasicSocialGraph(structure);
    }

    //Alles in Edge ausgelagert
    @Override
    public void setup(Persistable persistable, BasicSocialGraph object, RestoreManager manager) {
    }

    @Override
    public void finalize(Persistable persistable, BasicSocialGraph object, RestoreManager manager) {
    }
}
