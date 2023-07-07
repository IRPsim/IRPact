package de.unileipzig.irpact.core.network.filter;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.agent.SpatialAgent;

/**
 * @author Daniel Abitz
 */
public class EntireNetworkNodeFilterScheme extends NameableBase implements NodeDistanceFilterScheme {

    public static final EntireNetworkNodeFilterScheme INSTANCE = new EntireNetworkNodeFilterScheme("DEFAULT_EntireNetworkNodeFilterScheme");

    public EntireNetworkNodeFilterScheme() {
    }

    public EntireNetworkNodeFilterScheme(String name) {
        setName(name);
    }

    @Override
    public EntireNetworkNodeFilter createFilter(SpatialAgent agent) {
        return EntireNetworkNodeFilter.INSTANCE;
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(getName());
    }
}
