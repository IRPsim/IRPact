package de.unileipzig.irpact.jadex.examples.singleAgentTests.consumer;

import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentBase;
import de.unileipzig.irpact.core.need.BasicNeed;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.product.*;
import de.unileipzig.irpact.core.product.perception.BasicProductAttributePerceptionSchemeManager;
import de.unileipzig.irpact.core.spatial.Metric;
import de.unileipzig.irpact.core.spatial.dim2.Point2D;
import de.unileipzig.irpact.core.spatial.dim2.SquareModel;
import de.unileipzig.irpact.core.spatial.dim2.SquareModelDistribution;
import de.unileipzig.irpact.jadex.agent.consumer.ConsumerAgentService;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.start.StartSimulation;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.ServiceScope;
import jadex.bridge.service.search.ServiceQuery;
import jadex.bridge.service.types.cms.CreationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class StartPlatform {

    private static BasicJadexSimulationEnvironment env = new BasicJadexSimulationEnvironment();
    private static Logger logger = LoggerFactory.getLogger(StartPlatform.class);


    private static Need testNeed = new BasicNeed("TestNeed");
    private static Product testProduct = getTestProduct();

    private static ConsumerAgentBase getAgentBase() {
        Set<ConsumerAgent> agents = new HashSet<>();
        BasicConsumerAgentGroup group = new BasicConsumerAgentGroup(
                env,
                "TestGroup",
                agents,
                new HashSet<>(),
                new HashMap<>(),
                new HashMap<>(),
                1.0,
                new SquareModelDistribution(
                        "TestSquareModelDistribution",
                        new SquareModel("TestSquareModel", Metric.EUCLIDEAN, 0, 0, 1, 1),
                        42),
                (environment, agent, need) -> {
                    logger.debug("[ProductFindingScheme] Agent '{}' Need '{}'",
                            agent.getName(),
                            need.print()
                    );
                    return Collections.singleton(testProduct);
                },
                (environment, agent, potentialProducts) -> {
                    logger.debug("[ProductAdoptionDecisionScheme] Agent '{}' ProductsSize '{}'",
                            agent.getName(),
                            potentialProducts.size());
                    Product p = CollectionUtil.getFirst(potentialProducts);
                    return p == testProduct
                            ? p
                            : null;
                },
                agent -> {
                    logger.debug("[NeedDevelopmentScheme] ");
                    return Collections.emptySet();
                },
                agent -> {
                    logger.debug("[NeedExpirationScheme] ");
                    return Collections.emptySet();
                },
                (agent, need) -> {
                    logger.debug("[NeedSatisfyScheme] Agent '{}' Need '{}'",
                            agent.getName(),
                            need.print());
                }
        );
        ConsumerAgentBase agentBase = new ConsumerAgentBase(
                env,
                "TestConsumer",
                new Point2D(0, 0),
                group,
                new HashSet<>(),
                new HashSet<>(),
                new BasicProductAttributePerceptionSchemeManager(new HashMap<>()),
                new HashSet<>()
        );
        agents.add(agentBase);
        return agentBase;
    }

    private static Product getTestProduct() {
        ProductGroup pg = new BasicProductGroup(
                env,
                new HashSet<>(),
                "TestGroup",
                new HashSet<>(),
                new HashSet<>()
        );
        return new BasicProduct(
                env,
                "TestProduct",
                pg,
                new HashSet<>()
        );
    }

    public static void main(String[] args) {
        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        config.setDefaultTimeout(-1L);

        IExternalAccess platform = Starter.createPlatform(config)
                .get();

        CreationInfo ci = new CreationInfo();
        ci.setName("Agent");
        ci.setFilename("de.unileipzig.irpact.jadex.agent.consumer.JadexConsumerAgentBDI.class");
        ci.addArgument(StartSimulation.AGENT_BASE, getAgentBase());

        IExternalAccess agent = platform.createComponent(ci)
                .get();

        Collection<ConsumerAgentService> coll = platform.searchServices(new ServiceQuery<>(ConsumerAgentService.class, ServiceScope.PLATFORM))
                .get();

        ConsumerAgentService cas = CollectionUtil.getFirst(coll);

        ConcurrentUtil.sleepSilently(2000);
        cas.getConsumerAgentSyn()
                .addNeed(testNeed);


        ConcurrentUtil.start(5000, platform::killComponent);
    }
}
