package de.unileipzig.irpact.jadex.examples.deprecated.tests.configs;

import de.unileipzig.irpact.core.agent.company.CompanyAgentBase;
import de.unileipzig.irpact.core.agent.company.IgnoreProductIntroduction;
import de.unileipzig.irpact.core.agent.company.advertisement.NoAdvertisement;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.TakeFirstProductAdoptionDecision;
import de.unileipzig.irpact.core.agent.policy.NoTaxes;
import de.unileipzig.irpact.core.agent.policy.PolicyAgentBase;
import de.unileipzig.irpact.core.agent.pos.*;
import de.unileipzig.irpact.core.need.IgnoreNeedSatisfy;
import de.unileipzig.irpact.core.need.NoNeedDevelopment;
import de.unileipzig.irpact.core.need.NoNeedExpiration;
import de.unileipzig.irpact.core.product.BasicProductGroup;
import de.unileipzig.irpact.core.spatial.dim2.DummyPoint2DDistribution;
import de.unileipzig.irpact.jadex.agent.consumer.JadexUseKnownProducts;
import de.unileipzig.irpact.OLD.io.config.JadexConfiguration;
import de.unileipzig.irpact.OLD.io.config.JadexConfigurationBuilder;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Daniel Abitz
 */
public final class AllAgentsAnd2Groups {

    public static JadexConfigurationBuilder builder() {
        JadexConfigurationBuilder cb = new JadexConfigurationBuilder()
                .initMinimal();

        cb.addProductGroup(new BasicProductGroup(
                cb.getEnvironment(),
                new HashSet<>(),
                "haus",
                new HashSet<>(),
                new HashSet<>()
        ));
        cb.addProductGroup(new BasicProductGroup(
                cb.getEnvironment(),
                new HashSet<>(),
                "baum",
                new HashSet<>(),
                new HashSet<>()
        ));
        BasicConsumerAgentGroup cag0 = new BasicConsumerAgentGroup(
                cb.getEnvironment(),
                "consumer_baum",
                new HashSet<>(),
                new HashSet<>(),
                new HashMap<>(),
                new HashMap<>(),
                1.0,
                DummyPoint2DDistribution.INSTANCE,
                JadexUseKnownProducts.INSTANCE,
                TakeFirstProductAdoptionDecision.INSTANCE,
                NoNeedDevelopment.INSTANCE,
                NoNeedExpiration.INSTANCE,
                IgnoreNeedSatisfy.INSTANCE
        );
        cag0.addEntity(cag0.deriveAgent());
        cag0.addEntity(cag0.deriveAgent());
        cag0.addEntity(cag0.deriveAgent());
        cb.addConsumerAgentGroup(cag0);

        BasicConsumerAgentGroup cag1 = new BasicConsumerAgentGroup(
                cb.getEnvironment(),
                "consumer_haus",
                new HashSet<>(),
                new HashSet<>(),
                new HashMap<>(),
                new HashMap<>(),
                1.0,
                DummyPoint2DDistribution.INSTANCE,
                JadexUseKnownProducts.INSTANCE,
                TakeFirstProductAdoptionDecision.INSTANCE,
                NoNeedDevelopment.INSTANCE,
                NoNeedExpiration.INSTANCE,
                IgnoreNeedSatisfy.INSTANCE
        );
        cag1.addEntity(cag1.deriveAgent());
        cag1.addEntity(cag1.deriveAgent());
        cb.addConsumerAgentGroup(cag1);

        cb.addPointOfSaleAgent(new PointOfSaleAgentBase(
                cb.getEnvironment(),
                "pos_test",
                1.0,
                DummyPoint2DDistribution.INSTANCE.drawValue(),
                IgnoreNewProduct.INSTANCE,
                IgnoreProductAvailabilityChange.INSTANCE,
                IgnoreProductSoldOut.INSTANCE,
                IgnoreProductPriceChange.INSTANCE
        ));

        cb.addPolicyAgent(new PolicyAgentBase(
                cb.getEnvironment(),
                "policy_trump",
                Double.MAX_VALUE,
                NoTaxes.INSTANCE
        ));

        cb.addCompanyAgent(new CompanyAgentBase(
                cb.getEnvironment(),
                "company_test",
                42,
                NoAdvertisement.INSTANCE,
                IgnoreProductIntroduction.INSTANCE,
                new HashSet<>()
        ));

        return cb;
    }

    public static JadexConfiguration get() {
        return builder().validate().build();
    }
}
