package de.unileipzig.irpact.core.process.filter;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.network.filter.EntireNetworkNodeFilter;
import de.unileipzig.irpact.core.network.filter.NodeFilter;
import de.unileipzig.irpact.core.process.ProcessPlan;

/**
 * @author Daniel Abitz
 */
public class EntireNetworkNodeFilterScheme extends NameableBase implements ProcessPlanNodeFilterScheme {

    public static final EntireNetworkNodeFilterScheme INSTANCE = new EntireNetworkNodeFilterScheme("DEFAULT_EntireNetworkNodeFilterScheme");

    public EntireNetworkNodeFilterScheme() {
    }

    public EntireNetworkNodeFilterScheme(String name) {
        setName(name);
    }

    @Override
    public NodeFilter createFilter(ProcessPlan plan) {
        return EntireNetworkNodeFilter.INSTANCE;
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(getName());
    }
}
