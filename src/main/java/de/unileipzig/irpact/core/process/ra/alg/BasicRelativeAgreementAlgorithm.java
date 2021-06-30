package de.unileipzig.irpact.core.process.ra.alg;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.InfoTag;
import de.unileipzig.irpact.core.process.ra.uncert.Uncertainty;
import de.unileipzig.irpact.core.util.AttributeHelper;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.slf4j.event.Level;

/**
 * @author Daniel Abitz
 */
public class BasicRelativeAgreementAlgorithm extends AbstractRelativeAgreementAlgorithm {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicRelativeAgreementAlgorithm.class);

    public BasicRelativeAgreementAlgorithm() {
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.DEFAULT_NONNULL_CHECKSUM;
    }

    @Override
    public boolean apply(
            String name1,
            ConsumerAgentAttribute opinion1,
            Uncertainty uncertainty1,
            String name2,
            ConsumerAgentAttribute opinion2,
            Uncertainty uncertainty2) {
        double m1 = uncertainty1.getSpeedOfConvergence(opinion1);
        double o1 = opinion1.asValueAttribute().getDoubleValue();
        double u1 = uncertainty1.getUncertainty(opinion1);
        double m2 = uncertainty2.getSpeedOfConvergence(opinion2);
        double o2 = opinion2.asValueAttribute().getDoubleValue();
        double u2 = uncertainty2.getUncertainty(opinion2);

        IRPLogger logger = getLogger(loggingDisabled(), isLogData(), LOGGER);
        IRPSection section = getSection(isLogData());
        Level level = getLevel(isLogData());

        //1 influences 2
        boolean influenced2 = apply(
                logger, section, level,
                currentYear(),
                m2,
                name1, o1, u1,
                name2, o2, u2,
                opinion2, uncertainty2
        );
        //2 influences 1
        boolean influenced1 = apply(
                logger, section, level,
                currentYear(),
                m1,
                name2, o2, u2,
                name1, o1, u1,
                opinion1, uncertainty1
        );

        return influenced2 || influenced1;
    }

    //=========================
    //static
    //=========================

    public static boolean applyReverse(
            IRPLogger logger, IRPSection section, Level level,
            int currentYear,
            double mj,
            String namei, double oi, double ui,
            String namej, double oj, double uj,
            ConsumerAgentAttribute ojAttr, Uncertainty ujAttr) {
        return apply(logger, section, level, currentYear, mj, namei, oi, ui, namej, oj, uj, ojAttr, ujAttr, true);
    }

    public static boolean apply(
            IRPLogger logger, IRPSection section, Level level,
            int currentYear,
            double mj,
            String namei, double oi, double ui,
            String namej, double oj, double uj,
            ConsumerAgentAttribute ojAttr, Uncertainty ujAttr) {
        return apply(logger, section, level, currentYear, mj, namei, oi, ui, namej, oj, uj, ojAttr, ujAttr, false);
    }

    public static boolean apply(
            IRPLogger logger, IRPSection section, Level level,
            int currentYear,
            double mj,
            String namei, double oi, double ui,
            String namej, double oj, double uj,
            ConsumerAgentAttribute ojAttr, Uncertainty ujAttr,
            boolean reverse) {
        if(mj == 0.0) {
            return false;
        }

        double hij = Math.min(oi + ui, oj + uj) - Math.max(oi - ui, oj - uj);
        if(hij > ui) {
            double ra = hij / ui - 1.0;
            double tempOj = mj * ra * (oi - oj);
            double tempUj = mj * ra * (ui - uj);

            double newOj = reverse
                    ? oj - tempOj
                    : oj + tempOj;
            double newUj = reverse
                    ? uj - tempUj
                    : uj + tempUj;

            AttributeHelper.setDoubleValue(currentYear, ojAttr, newOj);
            ujAttr.updateUncertainty(ojAttr, newUj);

            logRelativeAgreementSuccess(logger, section, level, reverse, namei, namej, ojAttr.getName(), oi, ui, oj, uj, mj, hij, ra, newOj, newUj);
            return true;
        } else {
            logRelativeAgreementFailed(logger, section, level, reverse, namei, namej, ojAttr.getName(), oi, ui, oj, uj, hij);
            return false;
        }
    }

    protected static void logRelativeAgreementSuccess(
            IRPLogger logger, IRPSection section, Level level,
            boolean reversed,
            String ai, String aj, String attribute,
            double xi, double ui, double xj, double uj, double m,
            double hij, double ra, double newXj, double newUj) {
        if(logger == null) {
            return;
        }
        logger.log(section, level,
                "{} [{}] (reversed={}) relative agreement between i='{}' and j='{}' for '{}' success (hij={} > ui={}) | xi={}, ui={}, xj={}, uj={} | hij = {} = Math.min({} + {}, {} + {}) - Math.max({} - {}, {} - {}) | ra = {} = {} / {} - 1.0 | newXj = {} = {} + {} * {} * ({} - {}) | newUj = {} = {} + {} * {} * ({} - {})",
                InfoTag.RELATIVE_AGREEMENT, ai, reversed, ai, aj, attribute, hij, ui,
                xi, ui, xj, uj,
                hij, xi, ui, xj, uj, xi, ui, xj, uj,
                ra, hij, ui,
                newXj, xj, m, ra, xi, xj,
                newUj, uj, m, ra, ui, uj
        );
    }

    protected static void logRelativeAgreementFailed(
            IRPLogger logger, IRPSection section, Level level,
            boolean reversed,
            String ai, String aj, String attribute,
            double xi, double ui, double xj, double uj, double hij) {
        if(logger == null) {
            return;
        }
        logger.log(section, level,
                "{} [{}] (reversed={}) relative agreement between i='{}' and j='{}' for '{}' failed (hij={} <= ui={})) | xi={}, ui={}, xj={}, uj={} | hij = {} = Math.min({} + {}, {} + {}) - Math.max({} - {}, {} - {})",
                InfoTag.RELATIVE_AGREEMENT, ai, reversed, ai, aj, attribute, hij, ui,
                xi, ui, xj, uj,
                hij, xi, ui, xj, uj, xi, ui, xj, uj
        );
    }
}
