package de.unileipzig.irpact.core.affinity;

import de.unileipzig.irpact.commons.annotation.ToDo;
import de.unileipzig.irpact.commons.annotation.ToImpl;

import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
@ToDo("name vllt aendern")
@ToImpl("in consumer einfuegen")
public interface AffinityMapping<A, B> {

    double getValue(A from, B to) throws NoSuchElementException;

    double getReverseValue(B from, A to) throws NoSuchElementException;

    double getValue(A from, B to, double defaultValue);

    double getReverseValue(B from, A to, double defaultValue);
}
