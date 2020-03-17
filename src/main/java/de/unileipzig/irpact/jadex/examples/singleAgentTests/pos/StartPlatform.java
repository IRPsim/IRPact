package de.unileipzig.irpact.jadex.examples.singleAgentTests.pos;

import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import de.unileipzig.irpact.core.agent.pos.PointOfSaleAgentBase;
import de.unileipzig.irpact.core.currency.FinalPrice;
import de.unileipzig.irpact.core.product.*;
import de.unileipzig.irpact.core.product.availability.FiniteProductAvailability;
import de.unileipzig.irpact.core.product.availability.InfiniteProductAvailability;
import de.unileipzig.irpact.core.product.availability.NoProductAvailability;
import de.unileipzig.irpact.core.spatial.dim2.Point2D;
import de.unileipzig.irpact.jadex.agent.pos.PointOfSaleAgentService;
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

import java.util.Collection;
import java.util.HashSet;

public class StartPlatform {

    private static BasicJadexSimulationEnvironment env = new BasicJadexSimulationEnvironment();
    private static Logger logger = LoggerFactory.getLogger(StartPlatform.class);

    private static PointOfSaleAgentBase getAgentBase() {
        return new PointOfSaleAgentBase(
                env,
                "TestPOS",
                1.0,
                new Point2D(0, 0),
                (agent, product, oldAvailability, newAvailability) -> {
                    logger.debug("[ProductAvailabilityChangeScheme] Agent '{}' Product '{}' old '{}', new '{}'",
                            agent.getName(),
                            product.getName(),
                            oldAvailability,
                            newAvailability
                    );
                },
                (agent, product, availability) -> {
                    logger.debug("[ProductSoldOutScheme] Agent '{}' Product '{}' Avail '{}'",
                            agent.getName(),
                            product.getName(),
                            availability
                    );
                },
                (agent, product, oldProce, newPrice) -> {
                    logger.debug("[ProductPriceChangeScheme] Agent '{}' Product '{}' old '{}', new '{}'",
                            agent.getName(),
                            product.getName(),
                            oldProce,
                            newPrice
                    );
                }
        );
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
                pg,
                "TestProduct",
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
        ci.setFilename("de.unileipzig.irpact.jadex.agent.pos.JadexPointOfSaleAgentBDI.class");
        ci.addArgument(StartSimulation.AGENT_BASE, getAgentBase());

        IExternalAccess agent = platform.createComponent(ci)
                .get();

        Collection<PointOfSaleAgentService> coll = platform.searchServices(new ServiceQuery<>(PointOfSaleAgentService.class, ServiceScope.PLATFORM))
                .get();

        Product testProduct = getTestProduct();
        PointOfSaleAgentService posas = CollectionUtil.getFirst(coll);

        ConcurrentUtil.sleepSilently(2000);
        posas.getPointOfSaleAgentSyn()
                .updateAvailability(testProduct, InfiniteProductAvailability.INSTANCE);

        ConcurrentUtil.sleepSilently(2000);
        boolean success = posas.getPointOfSaleAgentSyn()
                .buyProduct(testProduct);
        logger.debug("buy: {}", success);

        ConcurrentUtil.sleepSilently(2000);
        posas.getPointOfSaleAgentSyn()
                .updateAvailability(testProduct, NoProductAvailability.INSTANCE);

        ConcurrentUtil.sleepSilently(2000);
        boolean success2 = posas.getPointOfSaleAgentSyn()
                .buyProduct(testProduct);
        logger.debug("buy2: {}", success2);

        ConcurrentUtil.sleepSilently(2000);
        posas.getPointOfSaleAgentSyn()
                .updateAvailability(testProduct, new FiniteProductAvailability(2));

        ConcurrentUtil.sleepSilently(2000);
        boolean success3 = posas.getPointOfSaleAgentSyn()
                .buyProduct(testProduct);
        logger.debug("buy3: {}", success3);

        //triggert soldout scheme
        ConcurrentUtil.sleepSilently(2000);
        boolean success4 = posas.getPointOfSaleAgentSyn()
                .buyProduct(testProduct);
        logger.debug("buy4: {}", success4);

        //price
        ConcurrentUtil.sleepSilently(2000);
        posas.getPointOfSaleAgentSyn()
                .updatePrice(testProduct, new FinalPrice(42));

        ConcurrentUtil.start(5000, platform::killComponent);
    }
}
