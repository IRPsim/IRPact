package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.commons.attribute.v2.simple.Attribute2;

/**
 * @author Daniel Abitz
 */
public interface AttributableAgent extends Agent {

    Attribute2 getAttribute2(String name);

    Attribute2 findAttribute2(String name);
}
