package de.unileipzig.irpact.experimental.demos;

import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.io.input.InAttributeName;
import de.unileipzig.irpact.io.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.input.agent.consumer.InConsumerAgentGroupAttribute;
import de.unileipzig.irpact.io.input.awareness.InAwareness;
import de.unileipzig.irpact.io.input.awareness.InThresholdAwareness;
import de.unileipzig.irpact.io.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.input.time.InDiscreteTimeModel;
import de.unileipzig.irpact.io.input.time.InTimeModel;
import org.junit.jupiter.api.Disabled;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
@Disabled
public class DemoUtil {

    public static InTimeModel[] getDiscrete1D() {
        InDiscreteTimeModel model = new InDiscreteTimeModel(
                "Diskret",
                TimeUnit.DAYS.toMillis(1)
        );
        return new InTimeModel[]{model};
    }

    private static InAttributeName getName(Map<String, Object> cache, String name) {
        return (InAttributeName) cache.computeIfAbsent(name, InAttributeName::new);
    }

    public static InConsumerAgentGroup createGroup(
            String name,
            String d1Dist,
            Map<String, Object> cache) {
        InConsumerAgentGroup grp = new InConsumerAgentGroup();
        grp._name = name;
        grp.informationAuthority = 1.0;
        grp.numberOfAgentsX = 10;
        grp.cagAwareness = new InAwareness[]{new InThresholdAwareness(name + "_" + "awa", 10.0)};
        grp.cagAttributes = new InConsumerAgentGroupAttribute[]{
                new InConsumerAgentGroupAttribute(
                        name + "_" + RAConstants.PURCHASE_POWER,
                        getName(cache, RAConstants.PURCHASE_POWER),
                        (InUnivariateDoubleDistribution) cache.get("DiraqDistribution1")),
                
                new InConsumerAgentGroupAttribute(
                        name + "_" + RAConstants.NOVELTY_SEEKING,
                        getName(cache, RAConstants.NOVELTY_SEEKING),
                        (InUnivariateDoubleDistribution) cache.get("DiraqDistribution1")),
                new InConsumerAgentGroupAttribute(
                        name + "_" + RAConstants.NOVELTY_SEEKING_UNCERTAINTY,
                        getName(cache, RAConstants.NOVELTY_SEEKING_UNCERTAINTY),
                        (InUnivariateDoubleDistribution) cache.get("DiraqDistribution1")),
                new InConsumerAgentGroupAttribute(
                        name + "_" + RAConstants.NOVELTY_SEEKING_CONVERGENCE,
                        getName(cache, RAConstants.NOVELTY_SEEKING_CONVERGENCE),
                        (InUnivariateDoubleDistribution) cache.get("DiraqDistribution1")),

                new InConsumerAgentGroupAttribute(
                        name + "_" + RAConstants.DEPENDENT_JUDGMENT_MAKING,
                        getName(cache, RAConstants.DEPENDENT_JUDGMENT_MAKING),
                        (InUnivariateDoubleDistribution) cache.get("DiraqDistribution1")),
                new InConsumerAgentGroupAttribute(
                        name + "_" + RAConstants.DEPENDENT_JUDGMENT_MAKING_UNCERTAINTY,
                        getName(cache, RAConstants.DEPENDENT_JUDGMENT_MAKING_UNCERTAINTY),
                        (InUnivariateDoubleDistribution) cache.get("DiraqDistribution1")),
                new InConsumerAgentGroupAttribute(
                        name + "_" + RAConstants.DEPENDENT_JUDGMENT_MAKING_CONVERGENCE,
                        getName(cache, RAConstants.DEPENDENT_JUDGMENT_MAKING_CONVERGENCE),
                        (InUnivariateDoubleDistribution) cache.get("DiraqDistribution1")),

                new InConsumerAgentGroupAttribute(
                        name + "_" + RAConstants.ENVIRONMENTAL_CONCERN,
                        getName(cache, RAConstants.ENVIRONMENTAL_CONCERN),
                        (InUnivariateDoubleDistribution) cache.get("DiraqDistribution1")),
                new InConsumerAgentGroupAttribute(
                        name + "_" + RAConstants.ENVIRONMENTAL_CONCERN_UNCERTAINTY,
                        getName(cache, RAConstants.ENVIRONMENTAL_CONCERN_UNCERTAINTY),
                        (InUnivariateDoubleDistribution) cache.get("DiraqDistribution1")),
                new InConsumerAgentGroupAttribute(
                        name + "_" + RAConstants.ENVIRONMENTAL_CONCERN_CONVERGENCE,
                        getName(cache, RAConstants.ENVIRONMENTAL_CONCERN_CONVERGENCE),
                        (InUnivariateDoubleDistribution) cache.get("DiraqDistribution1")),

                new InConsumerAgentGroupAttribute(
                        name + "_" + RAConstants.SHARE_1_2_HOUSE,
                        getName(cache, RAConstants.SHARE_1_2_HOUSE),
                        (InUnivariateDoubleDistribution) cache.get("DiraqDistribution1")),
                new InConsumerAgentGroupAttribute(
                        name + "_" + RAConstants.HOUSE_OWNER,
                        getName(cache, RAConstants.HOUSE_OWNER),
                        (InUnivariateDoubleDistribution) cache.get("DiraqDistribution0")),

                new InConsumerAgentGroupAttribute(
                        name + "_" + RAConstants.INITIAL_PRODUCT_AWARENESS,
                        getName(cache, RAConstants.HOUSE_OWNER),
                        (InUnivariateDoubleDistribution) cache.get(d1Dist))
        };

        return grp;
    }
}
