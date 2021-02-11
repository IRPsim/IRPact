package de.unileipzig.irpact.core.log;

import de.unileipzig.irptools.util.log.IRPLoggingType;
import de.unileipzig.irptools.util.log.LoggingFilter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
/*
 * Dieser Filter arbeitet mit Bereichen. Ein Bereich kann geloggt werden oder auch nicht.
 *
 * - Die Level-Beschraenkungen des Loggers selber gelten weiterhin. Dieser Filter ist fuer die reine
 * Informationsausgabe gedacht und konzipiert.
 * - Das Error-Level kann nicht gefiltert werden.
 */
public final class LoggingPartFilter implements LoggingFilter {

    protected Map<IRPLoggingType, Set<Integer>> partMap;
    protected boolean logAll = false;
    protected boolean logNothing = false;
    protected boolean logIfTypeIsNull = true;
    protected boolean logIfTypeIsUnknown = true;

    public LoggingPartFilter() {
        this(new HashMap<>());
    }

    public LoggingPartFilter(Map<IRPLoggingType, Set<Integer>> partMap) {
        this.partMap = partMap;
    }

    public void setLogAll(boolean logAll) {
        this.logAll = logAll;
    }

    public void setLogNothing(boolean logNothing) {
        this.logNothing = logNothing;
    }

    public void setLogIfTypeIsNull(boolean logIfTypeIsNull) {
        this.logIfTypeIsNull = logIfTypeIsNull;
    }

    public void setLogIfTypeIsUnknown(boolean logIfTypeIsUnknown) {
        this.logIfTypeIsUnknown = logIfTypeIsUnknown;
    }

    public void put(IRPLoggingType type, int level) {
        Set<Integer> levelSet = partMap.computeIfAbsent(type, _type -> new HashSet<>());
        levelSet.add(level);
    }

    public boolean remove(IRPLoggingType type, int level) {
        Set<Integer> levelSet = partMap.get(type);
        if(levelSet == null) {
            return false;
        }
        return levelSet.remove(level);
    }

    public boolean has(IRPLoggingType type, int level) {
        Set<Integer> levelSet = partMap.get(type);
        if(levelSet == null) {
            return false;
        }
        return levelSet.contains(level);
    }

    @Override
    public boolean doLogging(IRPLoggingType type, int level) {
        if(logAll) {
            return true;
        }
        if(logNothing) {
            return false;
        }
        if(type == null) {
            return logIfTypeIsNull;
        }
        Set<Integer> levelSet = partMap.get(type);
        if(levelSet == null) {
            return logIfTypeIsUnknown;
        }
        return levelSet.contains(level);
    }
}
