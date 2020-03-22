package de.unileipzig.irpact.jadex.examples.deprecated.tests.configs;

import de.unileipzig.irpact.commons.distribution.BooleanDistribution;
import de.unileipzig.irpact.commons.distribution.ConstantDistribution;
import de.unileipzig.irpact.commons.distribution.RandomBoundedDistribution;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.TakeFirstProductAdoptionDecision;
import de.unileipzig.irpact.core.agent.policy.NoTaxes;
import de.unileipzig.irpact.core.agent.policy.PolicyAgentBase;
import de.unileipzig.irpact.core.agent.pos.*;
import de.unileipzig.irpact.core.need.BasicNeed;
import de.unileipzig.irpact.core.need.IgnoreNeedSatisfy;
import de.unileipzig.irpact.core.need.NoNeedDevelopment;
import de.unileipzig.irpact.core.need.NoNeedExpiration;
import de.unileipzig.irpact.core.product.BasicProductGroup;
import de.unileipzig.irpact.core.product.BasicProductGroupAttribute;
import de.unileipzig.irpact.core.spatial.dim2.DummyPoint2DDistribution;
import de.unileipzig.irpact.jadex.agent.consumer.JadexUseKnownProducts;
import de.unileipzig.irpact.io.config.JadexConfiguration;
import de.unileipzig.irpact.io.config.JadexConfigurationBuilder;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public final class BaumHaus2ConsumerType {

    public static JadexConfiguration get() {
        JadexConfigurationBuilder cb = new JadexConfigurationBuilder()
                .initMinimal()
                //haus
                .addNeed("haus", new BasicNeed("haus"))
                .addProductGroupAttribute("etagen", new BasicProductGroupAttribute("etagen", new ConstantDistribution("cd0", 2)))
                .addProductGroupAttribute("garten", new BasicProductGroupAttribute("garten", new ConstantDistribution("cd1", 0)))
                //baum
                .addNeed("baum", new BasicNeed("baum"))
                .addProductGroupAttribute("hoehe", new BasicProductGroupAttribute("hoehe", new RandomBoundedDistribution("rd0", new Random(42), 5, 10)))
                //group 1
                .addConsumerAgentGroupAttribute("baum", new BasicConsumerAgentGroupAttribute("baum", new BooleanDistribution("bd0", new Random(1337), 0.5)))
                .addConsumerAgentGroupAttribute("baumidiot", new BasicConsumerAgentGroupAttribute("idiot", new RandomBoundedDistribution("rd1", new Random(42), 0, 1)))
                //group 2
                .addConsumerAgentGroupAttribute("haus", new BasicConsumerAgentGroupAttribute("haus", new BooleanDistribution("bd1", new Random(7331), 0.5)))
                .addConsumerAgentGroupAttribute("hausidiot", new BasicConsumerAgentGroupAttribute("idiot", new RandomBoundedDistribution("rd2", new Random(24), 0, 1)))
                ;

        cb.addProductGroup(new BasicProductGroup(
                cb.getEnvironment(),
                new HashSet<>(),
                "haus",
                cb.getProductGroupAttributes(Arrays.asList("etagen", "garten")),
                cb.getNeeds(Collections.singletonList("haus"))
        ));
        cb.addProductGroup(new BasicProductGroup(
                cb.getEnvironment(),
                new HashSet<>(),
                "baum",
                cb.getProductGroupAttributes(Collections.singletonList("hoehe")),
                cb.getNeeds(Collections.singletonList("baum"))
        ));

        cb.addConsumerAgentGroup(new BasicConsumerAgentGroup(
                cb.getEnvironment(),
                "baum",
                new HashSet<>(),
                cb.getConsumerAgentGroupAttributes(Arrays.asList("baum", "baumidiot")),
                new HashMap<>(),
                new HashMap<>(),
                1.0,
                DummyPoint2DDistribution.INSTANCE,
                JadexUseKnownProducts.INSTANCE,
                TakeFirstProductAdoptionDecision.INSTANCE,
                NoNeedDevelopment.INSTANCE,
                NoNeedExpiration.INSTANCE,
                IgnoreNeedSatisfy.INSTANCE
        ));
        cb.addConsumerAgentGroup(new BasicConsumerAgentGroup(
                cb.getEnvironment(),
                "haus",
                new HashSet<>(),
                cb.getConsumerAgentGroupAttributes(Arrays.asList("haus", "hausidiot")),
                new HashMap<>(),
                new HashMap<>(),
                1.0,
                DummyPoint2DDistribution.INSTANCE,
                JadexUseKnownProducts.INSTANCE,
                TakeFirstProductAdoptionDecision.INSTANCE,
                NoNeedDevelopment.INSTANCE,
                NoNeedExpiration.INSTANCE,
                IgnoreNeedSatisfy.INSTANCE
        ));

        cb.addPointOfSaleAgent(new PointOfSaleAgentBase(
                cb.getEnvironment(),
                "testpos",
                1.0,
                DummyPoint2DDistribution.INSTANCE.drawValue(),
                IgnoreNewProduct.INSTANCE,
                IgnoreProductAvailabilityChange.INSTANCE,
                IgnoreProductSoldOut.INSTANCE,
                IgnoreProductPriceChange.INSTANCE
        ));

        cb.addPolicyAgent(new PolicyAgentBase(
                cb.getEnvironment(),
                "trump",
                Double.MAX_VALUE,
                NoTaxes.INSTANCE
        ));
        return cb.validate().build();
    }
}
