package de.unileipzig.irpact.core.network.filter;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.network.SocialGraph;

/**
 * @author Daniel Abitz
 */
public class EntireNetworkNodeFilter extends NameableBase implements NodeDistanceFilter {

    public static final EntireNetworkNodeFilter INSTANCE = new EntireNetworkNodeFilter("DEFAULT_EntireNetworkNodeFilter");

    public EntireNetworkNodeFilter() {
    }

    public EntireNetworkNodeFilter(String name) {
        setName(name);
    }

    @Override
    public boolean test(SocialGraph.Node node) {
        return true;
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getNameChecksum(this);
    }
}
