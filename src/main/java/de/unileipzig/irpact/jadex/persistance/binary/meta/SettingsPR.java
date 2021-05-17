package de.unileipzig.irpact.jadex.persistance.binary.meta;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.persistence.PersistableBase;
import de.unileipzig.irpact.io.param.inout.persist.binary.BinaryPersistData;
import de.unileipzig.irpact.jadex.persistance.JadexPersistable;
import de.unileipzig.irpact.jadex.persistance.binary.io.BinaryPersistJson;

/**
 * @author Daniel Abitz
 */
public final class SettingsPR extends PersistableBase implements JadexPersistable {

    public static final String UID_PREFIX = "y";
    public static final long UID = 0;

    protected ObjectNode root;

    public SettingsPR(ObjectNode root) {
        this.root = root;
        setUID(UID);
    }

    public ObjectNode getRoot() {
        return root;
    }

    @Override
    public BinaryPersistData toPersistData() {
        return BinaryPersistJson.toData(root, UID_PREFIX, getUID());
    }
}
