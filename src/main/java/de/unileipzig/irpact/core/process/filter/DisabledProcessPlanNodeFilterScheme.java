package de.unileipzig.irpact.core.process.filter;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.network.filter.DisabledNodeFilter;
import de.unileipzig.irpact.core.network.filter.NodeFilter;
import de.unileipzig.irpact.core.process.ProcessPlan;

/**
 * @author Daniel Abitz
 */
public class DisabledProcessPlanNodeFilterScheme extends NameableBase implements ProcessPlanNodeFilterScheme {

    public static final DisabledProcessPlanNodeFilterScheme INSTANCE = new DisabledProcessPlanNodeFilterScheme("DEFAULT_DisabledProcessPlanNodeFilterScheme");

    public DisabledProcessPlanNodeFilterScheme() {
    }

    public DisabledProcessPlanNodeFilterScheme(String name) {
        setName(name);
    }

    @Override
    public NodeFilter createFilter(ProcessPlan plan) {
        return DisabledNodeFilter.INSTANCE;
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(getName());
    }
}
