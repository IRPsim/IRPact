package de.unileipzig.irpact.core.logging;

import de.unileipzig.irptools.util.log.LoggingSection;

import java.util.EnumSet;

/**
 * @author Daniel Abitz
 */
public interface ComplexIRPSection extends LoggingSection {

    boolean test(EnumSet<IRPSection> set);

    @Override
    default Class<ComplexIRPSection> getType() {
        return ComplexIRPSection.class;
    }

    /**
     * @author Daniel Abitz
     */
    final class And implements ComplexIRPSection {

        private final IRPSection[] sections;

        public And(IRPSection... sections) {
            this.sections = sections;
        }

        public static And get(IRPSection... sections) {
            return new And(sections);
        }

        @Override
        public boolean test(EnumSet<IRPSection> set) {
            for(IRPSection section: sections) {
                if(!set.contains(section)) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * @author Daniel Abitz
     */
    final class Or implements ComplexIRPSection {

        private final IRPSection[] sections;

        public Or(IRPSection... sections) {
            this.sections = sections;
        }

        public static Or get(IRPSection... sections) {
            return new Or(sections);
        }

        @Override
        public boolean test(EnumSet<IRPSection> set) {
            for(IRPSection section: sections) {
                if(set.contains(section)) {
                    return true;
                }
            }
            return false;
        }
    }
}
