package de.unileipzig.irpact.core.log;

import de.unileipzig.irptools.util.log.LoggingFilter;
import de.unileipzig.irptools.util.log.LoggingSection;

import java.util.*;

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
public final class SectionLoggingFilter implements LoggingFilter {

    protected EnumSet<IRPSection> sections;
    protected boolean logAll = false;
    protected boolean logNothing = false;
    protected boolean logIfSectionIsNull = false;
    protected boolean logIfTypeIsUnknown = true;
    protected boolean logDefault = true;

    public SectionLoggingFilter() {
        this(EnumSet.noneOf(IRPSection.class));
    }

    public SectionLoggingFilter(EnumSet<IRPSection> sections) {
        this.sections = sections;
    }

    public void setLogAll(boolean logAll) {
        this.logAll = logAll;
    }

    public void setLogNothing(boolean logNothing) {
        this.logNothing = logNothing;
    }

    public void setLogIfSectionIsNull(boolean logIfSectionIsNull) {
        this.logIfSectionIsNull = logIfSectionIsNull;
    }

    public void setLogIfTypeIsUnknown(boolean logIfTypeIsUnknown) {
        this.logIfTypeIsUnknown = logIfTypeIsUnknown;
    }

    public void setLogDefault(boolean logDefault) {
        this.logDefault = logDefault;
    }

    public void add(IRPSection section) {
        sections.add(section);
    }

    public boolean remove(IRPSection section) {
        return sections.remove(section);
    }

    public boolean has(IRPSection section) {
        return sections.contains(section);
    }

    public Set<IRPSection> getSections() {
        return sections;
    }

    @Override
    public boolean doLogging() {
        return logDefault;
    }

    @Override
    public boolean doLogging(LoggingSection section) {
        if(logAll) {
            return true;
        }
        if(logNothing) {
            return false;
        }
        if(section == null) {
            return logIfSectionIsNull;
        }
        return section.getClass() == IRPSection.class
                && has((IRPSection) section);
    }
}
