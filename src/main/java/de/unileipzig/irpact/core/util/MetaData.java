package de.unileipzig.irpact.core.util;

import de.unileipzig.irpact.commons.resource.ResourceLoader;
import de.unileipzig.irpact.core.simulation.Settings;

import java.util.Collection;
import java.util.Locale;

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

    Locale getLocale();

    ResourceLoader getLoader();
}
