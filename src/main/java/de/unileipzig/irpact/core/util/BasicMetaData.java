package de.unileipzig.irpact.core.util;

import de.unileipzig.irpact.commons.resource.ResourceLoader;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.simulation.Settings;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Collection;
import java.util.Locale;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * @author Daniel Abitz
 */
public class BasicMetaData implements MetaData {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicMetaData.class);

    private final NavigableSet<RunInfo> ALL_INFOS = new TreeSet<>(RunInfo.COMPARE_ID);
    private final RunInfo INFO = new RunInfo();

    protected Locale locale;
    protected ResourceLoader loader;

    public BasicMetaData() {
    }

    @Override
    public void init() {
        INFO.setId(0);
        ALL_INFOS.add(INFO);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "set run id: {}", INFO.getId());
    }

    @Override
    public void init(Collection<? extends RunInfo> oldInfos) {
        if(oldInfos.isEmpty()) {
            init();
        } else {
            ALL_INFOS.addAll(oldInfos);
            RunInfo last = ALL_INFOS.last();
            INFO.setId(last.getId() + 1);
            ALL_INFOS.add(INFO);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "set run id: {}", INFO.getId());
        }
    }

    @Override
    public void apply(Settings settings) {
        INFO.setFirstSimulationYear(settings.getFirstSimulationYear());
        INFO.setActualFirstSimulationYear(settings.getActualFirstSimulationYear());
        INFO.setLastSimulationYear(settings.getLastSimulationYear());
    }

    @Override
    public Collection<RunInfo> getAllRunInfos() {
        return ALL_INFOS;
    }

    @Override
    public RunInfo getOldestRunInfo() {
        return ALL_INFOS.first();
    }

    @Override
    public RunInfo getCurrentRunInfo() {
        return INFO;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public ResourceLoader getLoader() {
        return loader;
    }

    public void setLoader(ResourceLoader loader) {
        this.loader = loader;
    }
}
