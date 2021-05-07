package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.commons.ChecksumComparable;

/**
 * @author Daniel Abitz
 */
public interface Version extends ChecksumComparable {

    boolean isMatch(Version other);

    boolean supportsInput(Version other);

    /**
     * Major version of IRPact. Used to identify valid input data.
     *
     * @return major version
     */
    String major();

    /**
     * Minor version of IRPact. Used to identify valid input data.
     *
     * @return minor version
     */
    String minor();

    /**
     * Build version of IRPact.
     *
     * @return minor version
     */
    String build();

    /**
     * Prints full version.
     *
     * @return full version
     */
    String print();
}
