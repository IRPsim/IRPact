package de.unileipzig.irpact.core.util;

import de.unileipzig.irpact.core.simulation.Settings;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface MetaData {

    void init();

    void init(Collection<? extends RunInfo> oldInfos);

    void apply(Settings settings);

    Collection<RunInfo> getAllRunInfos();

    RunInfo getOldestRunInfo();

    RunInfo getCurrentRunInfo();
}
