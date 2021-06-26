package de.unileipzig.irpact.core.process.ra.alg;

import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ra.uncert.Uncertainty;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.slf4j.event.Level;

/**
 * @author Daniel Abitz
 */
public class ReversedRelativeAgreementAlgorithm extends AbstractRelativeAgreementAlgorithm {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ReversedRelativeAgreementAlgorithm.class);

    public ReversedRelativeAgreementAlgorithm() {
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
        boolean influenced2 = BasicRelativeAgreementAlgorithm.applyReverse(
                logger, section, level,
                m2,
                name1, o1, u1,
                name2, o2, u2,
                opinion2, uncertainty2
        );
        //2 influences 1
        boolean influenced1 = BasicRelativeAgreementAlgorithm.applyReverse(
                logger, section, level,
                m1,
                name2, o2, u2,
                name1, o1, u1,
                opinion1, uncertainty1
        );

        return influenced2 || influenced1;
    }
}
