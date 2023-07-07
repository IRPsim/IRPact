package de.unileipzig.irpact.core.network.filter;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.agent.SpatialAgent;

/**
 * @author Daniel Abitz
 */
public class DisabledNodeFilterScheme extends NameableBase implements NodeDistanceFilterScheme {

    public static final DisabledNodeFilterScheme INSTANCE = new DisabledNodeFilterScheme("DEFAULT_DisabledProcessPlanNodeFilterScheme");

    public DisabledNodeFilterScheme() {
    }

    public DisabledNodeFilterScheme(String name) {
        setName(name);
    }

    @Override
    public DisabledNodeFilter createFilter(SpatialAgent agent) {
        return DisabledNodeFilter.INSTANCE;
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(getName());
    }
}
