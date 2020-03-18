package de.unileipzig.irpact.jadex.examples.sendCustomMessage;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.company.CompanyAgentBase;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.TakeFirstProductAdoptionDecision;
import de.unileipzig.irpact.core.agent.policy.NoTaxes;
import de.unileipzig.irpact.core.agent.policy.PolicyAgentBase;
import de.unileipzig.irpact.core.agent.pos.IgnoreProductAvailabilityChange;
import de.unileipzig.irpact.core.agent.pos.IgnoreProductPriceChange;
import de.unileipzig.irpact.core.agent.pos.IgnoreProductSoldOut;
import de.unileipzig.irpact.core.agent.pos.PointOfSaleAgentBase;
import de.unileipzig.irpact.core.message.BasicMessageEvent;
import de.unileipzig.irpact.core.message.Message;
import de.unileipzig.irpact.core.message.MessageEvent;
import de.unileipzig.irpact.core.need.IgnoreNeedSatisfy;
import de.unileipzig.irpact.core.need.NoNeedDevelopment;
import de.unileipzig.irpact.core.need.NoNeedExpiration;
import de.unileipzig.irpact.core.product.BasicProductGroup;
import de.unileipzig.irpact.core.spatial.dim2.DummyPoint2DDistribution;
import de.unileipzig.irpact.io.config.JadexConfiguration;
import de.unileipzig.irpact.io.config.JadexConfigurationBuilder;
import de.unileipzig.irpact.jadex.agent.consumer.JadexUseKnownProducts;
import de.unileipzig.irpact.jadex.message.JadexMessageSystem;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.start.StartSimulation;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Daniel Abitz
 */
public class Main {

    private static JadexConfigurationBuilder builder() {
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
                42
        ));

        return cb;
    }

    public static void main(String[] args) {
        JadexConfiguration configuration = builder().validate()
                .build();
        StartSimulation.start(configuration);

        BasicJadexSimulationEnvironment env = (BasicJadexSimulationEnvironment) configuration.getEnvironment();
        Agent a0 = env.getConfiguration().findEntity("policy_trump");
        Agent a1 = env.getConfiguration().findEntity("consumer_baum#2");

        ConcurrentUtil.sleepSilently(2000);
        env.getMessageSystem().send(a0, new Message() {
            @Override
            public void process(Agent sender, Agent receiver) {
                System.out.println("Hello World! -> " + sender.getName() + " | " + receiver.getName());
            }

            @Override
            public boolean isSerializable() {
                return true;
            }

            @Override
            public String serializeToString() {
                return "System.out.println(\"Hello World!\")";
            }
        }, a1);

        ConcurrentUtil.sleepSilently(2000);
        env.getMessageSystem().setMode(JadexMessageSystem.Mode.JADEX);
        env.getMessageSystem().send(a0, new Message() {
            @Override
            public void process(Agent sender, Agent receiver) {
                System.out.println("Hello JadexWorld!");
            }

            @Override
            public boolean isSerializable() {
                return true;
            }

            @Override
            public String serializeToString() {
                return "System.out.println(\"Hello JadexWorld!\")";
            }
        }, a1);

        ConcurrentUtil.sleepSilently(2000);
        env.getMessageSystem().setMode(JadexMessageSystem.Mode.BASIC);
        MessageEvent msgEvent = new BasicMessageEvent(
                env.getMessageSystem(),
                a1,
                a1, //self!
                new Message() {
                    @Override
                    public void process(Agent sender, Agent receiver) {
                        System.out.println("HELLO EVENT -> " + sender.getName() + " | " + receiver.getName());
                    }

                    @Override
                    public boolean isSerializable() {
                        return false;
                    }

                    @Override
                    public String serializeToString() {
                        throw new UnsupportedOperationException();
                    }
                }
        );
        env.getEventManager().schedule(msgEvent);
    }
}
