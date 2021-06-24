package de.unileipzig.irpact.core.process.ra.alg;

import de.unileipzig.irpact.commons.attribute.AttributeUtil;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.InfoTag;
import de.unileipzig.irpact.core.process.ra.uncert.Uncertainty;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.slf4j.event.Level;

/**
 * @author Daniel Abitz
 */
public class DefaultRelativeAgreementAlgorithm extends AbstractRelativeAgreementAlgorithm {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DefaultRelativeAgreementAlgorithm.class);

    public DefaultRelativeAgreementAlgorithm() {
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

        //1 influences 2
        boolean influenced2 = apply(isLogData(), m2, name1, o1, u1, name2, o2, u2, opinion2, uncertainty2);
        //2 influences 1
        boolean influenced1 = apply(isLogData(), m1, name2, o2, u2, name1, o1, u1, opinion1, uncertainty1);

        return influenced2 || influenced1;
    }

    //=========================
    //util
    //=========================

    public static boolean applyReverse(
            boolean logData,
            double mj,
            String namei, double oi, double ui,
            String namej, double oj, double uj,
            ConsumerAgentAttribute ojAttr, Uncertainty ujAttr) {
        return apply(logData, mj, namei, oi, ui, namej, oj, uj, ojAttr, ujAttr, true);
    }

    public static boolean apply(
            boolean logData,
            double mj,
            String namei, double oi, double ui,
            String namej, double oj, double uj,
            ConsumerAgentAttribute ojAttr, Uncertainty ujAttr) {
        return apply(logData, mj, namei, oi, ui, namej, oj, uj, ojAttr, ujAttr, false);
    }

    public static boolean apply(
            boolean logData,
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

            AttributeUtil.setDoubleValue(ojAttr, newOj);
            ujAttr.updateUncertainty(ojAttr, newUj);

            logRelativeAgreementSuccess(logData, reverse, namei, namej, ojAttr.getName(), oi, ui, oj, uj, mj, hij, ra, newOj, newUj);
            return true;
        } else {
            logRelativeAgreementFailed(logData, reverse, namei, namej, ojAttr.getName(), oi, ui, oj, uj, hij);
            return false;
        }
    }

    protected static IRPLogger getLogger(boolean logData) {
        return logData
                ? IRPLogging.getResultLogger()
                : LOGGER;
    }

    protected static IRPSection getSection(boolean logData) {
        return IRPSection.SIMULATION_PROCESS.orGeneral(logData);
    }

    protected static Level getLevel(boolean logData) {
        return logData
                ? Level.INFO
                : Level.TRACE;
    }

    protected static void logRelativeAgreementSuccess(
            boolean logData, boolean reversed,
            String ai, String aj, String attribute,
            double xi, double ui, double xj, double uj, double m,
            double hij, double ra, double newXj, double newUj) {
        IRPLogger logger = getLogger(logData);
        IRPSection section = getSection(logData);
        Level level = getLevel(logData);
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
            boolean logData, boolean reversed,
            String ai, String aj, String attribute,
            double xi, double ui, double xj, double uj, double hij) {
        IRPLogger logger = getLogger(logData);
        IRPSection section = getSection(logData);
        Level level = getLevel(logData);
        logger.log(section, level,
                "{} [{}] (reversed={}) relative agreement between i='{}' and j='{}' for '{}' failed (hij={} <= ui={})) | xi={}, ui={}, xj={}, uj={} | hij = {} = Math.min({} + {}, {} + {}) - Math.max({} - {}, {} - {})",
                InfoTag.RELATIVE_AGREEMENT, ai, reversed, ai, aj, attribute, hij, ui,
                xi, ui, xj, uj,
                hij, xi, ui, xj, uj, xi, ui, xj, uj
        );
    }
}
