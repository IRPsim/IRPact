package de.unileipzig.irpact.core.network.filter;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.network.SocialGraph;

/**
 * @author Daniel Abitz
 */
public class DisabledNodeFilter extends NameableBase implements NodeFilter {

    public static final DisabledNodeFilter INSTANCE = new DisabledNodeFilter("DEFAULT_DisabledNodeFilter");

    public DisabledNodeFilter() {
    }

    public DisabledNodeFilter(String name) {
        setName(name);
    }

    @Override
    public boolean test(SocialGraph.Node node) {
        return false;
    }
}
