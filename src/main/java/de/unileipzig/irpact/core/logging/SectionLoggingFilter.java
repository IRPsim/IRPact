package de.unileipzig.irpact.core.logging;

import de.unileipzig.irptools.util.log.LoggingFilter;
import de.unileipzig.irptools.util.log.LoggingSection;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public final class SectionLoggingFilter implements LoggingFilter {

    private final EnumSet<IRPSection> SECTIONS;
    private EnumSet<IRPSection> forcedSections;

    private boolean disabled = false;
    private boolean logAll = false;
    private boolean logIfSectionIsNull = true;
    private boolean logIfTypeIsUnknown = false;
    private boolean logDefault = true;

    public SectionLoggingFilter() {
        this(EnumSet.noneOf(IRPSection.class), EnumSet.noneOf(IRPSection.class));
    }

    public SectionLoggingFilter(EnumSet<IRPSection> sections, EnumSet<IRPSection> forcedSections) {
        this.SECTIONS = sections;
        linkForcedSections(forcedSections);
    }

    //=========================
    // settings
    //=========================

    public void enable() {
        this.disabled = false;
    }

    public void disable() {
        disabled = true;
    }

    public void setLogAll(boolean logAll) {
        this.logAll = logAll;
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

    //=========================
    // sections
    //=========================

    public boolean add(IRPSection section) {
        return SECTIONS.add(section);
    }

    public boolean add(boolean add, IRPSection section) {
        if(add) {
            return add(section);
        } else {
            return false;
        }
    }

    public boolean addAll(IRPSection... sections) {
        return addAll(Arrays.asList(sections));
    }

    public boolean addAll(Collection<? extends IRPSection> sections) {
        return SECTIONS.addAll(sections);
    }

    public boolean remove(IRPSection section) {
        return SECTIONS.remove(section);
    }

    public boolean removeAll(IRPSection... sections) {
        return removeAll(Arrays.asList(sections));
    }

    public boolean removeAll(Collection<? extends IRPSection> sections) {
        return SECTIONS.removeAll(sections);
    }

    public boolean has(IRPSection section) {
        return SECTIONS.contains(section);
    }

    public Set<IRPSection> getSections() {
        return SECTIONS;
    }

    void linkForcedSections(EnumSet<IRPSection> sections) {
        this.forcedSections = sections == null
                ? EnumSet.noneOf(IRPSection.class)
                : sections;
    }

    private boolean test(IRPSection section) {
        return forcedSections.contains(section) || SECTIONS.contains(section);
    }

    private boolean test(ComplexIRPSection section) {
        return section.test(forcedSections) || section.test(SECTIONS);
    }

    //=========================
    // access
    //=========================

    @Override
    public boolean doLogging() {
        if(disabled) {
            return false;
        }
        if(logAll) {
            return true;
        }

        return logDefault;
    }

    @Override
    public boolean doLogging(LoggingSection section) {
        if(disabled) {
            return false;
        }
        if(logAll) {
            return true;
        }
        if(section == null) {
            return logIfSectionIsNull;
        }
        if(section.getType() == IRPSection.class) {
            return test((IRPSection) section);
        }
        if(section.getType() == ComplexIRPSection.class) {
            return test((ComplexIRPSection) section);
        }

        return logIfTypeIsUnknown;
    }
}
