package de.unileipzig.irpact.core.logging;

import de.unileipzig.irptools.util.log.LoggingFilter;
import de.unileipzig.irptools.util.log.LoggingSection;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public final class SectionLoggingFilter implements LoggingFilter {

    protected final EnumSet<IRPSection> SECTIONS;

    protected boolean disabled = false;
    protected boolean logAll = false;
    protected boolean logIfSectionIsNull = true;
    protected boolean logIfTypeIsUnknown = false;
    protected boolean logDefault = true;

    public SectionLoggingFilter() {
        this(EnumSet.noneOf(IRPSection.class));
    }

    public SectionLoggingFilter(EnumSet<IRPSection> sections) {
        this.SECTIONS = sections;
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
            return has((IRPSection) section);
        }
        if(section.getType() == ComplexIRPSection.class) {
            return ((ComplexIRPSection) section).test(SECTIONS);
        }

        return logIfTypeIsUnknown;
    }
}
