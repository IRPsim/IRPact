package de.unileipzig.irpact.core.network.filter;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.network.SocialGraph;

import java.util.function.Predicate;

/**
 * Filter for social network nodes.
 *
 * @author Daniel Abitz
 */
public interface NodeFilter extends Nameable, Predicate<SocialGraph.Node> {
}
