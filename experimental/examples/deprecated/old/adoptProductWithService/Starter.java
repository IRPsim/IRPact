package de.unileipzig.irpact.jadex.examples.deprecated.old.adoptProductWithService;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.commons.future.IFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Starter {

    private static final Logger logger = LoggerFactory.getLogger("JadexLogger");

    @SuppressWarnings("SameParameterValue")
    private static CreationInfo createConsumerAgentBDIInfo(
            String name,
            Logger logger,
            String need) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename("de.unileipzig.irpact.jadex.examples.old.adoptProductWithService.ConsumerAgentBDI.class");
        info.addArgument("name", name);
        info.addArgument("logger", logger);
        info.addArgument("need", need);
        return info;
    }

    @SuppressWarnings("SameParameterValue")
    private static CreationInfo createCompanyAgentBDIInfo(
            String name,
            Logger logger,
            Set<String> products) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename("de.unileipzig.irpact.jadex.examples.old.adoptProductWithService.CompanyAgentBDI.class");
        info.addArgument("name", name);
        info.addArgument("logger", logger);
        info.addArgument("products", products);
        return info;
    }

    public static void main(String[] args) {
        logger.debug("=====START=====");

        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);


        IExternalAccess platform = jadex.base.Starter.createPlatform(config)
                .get();

        Set<String> initialProducts = new HashSet<>();
        //initialProducts.add("baum");


        IExternalAccess agent0 = platform.createComponent(createConsumerAgentBDIInfo("agent0", logger, "haus"))
                .get();

        IExternalAccess agent1 = platform.createComponent(createConsumerAgentBDIInfo("agent1", logger, "baum"))
                .get();

        IExternalAccess shop0 = platform.createComponent(createCompanyAgentBDIInfo("shop0", logger, initialProducts))
                .get();

        ConcurrentUtil.start(0L, () -> {

            logger.debug("kill in 20");
            ConcurrentUtil.sleepSilently(5000);
            logger.debug("kill in 15");
            ConcurrentUtil.sleepSilently(5000);
            logger.debug("kill in 10");
            ConcurrentUtil.sleepSilently(5000);
            logger.debug("kill in 5");
            ConcurrentUtil.sleepSilently(5000);

            IFuture<Map<String, Object>> fAgent0 = agent0.getResultsAsync();
            IFuture<Map<String, Object>> fAgent1 = agent1.getResultsAsync();
            IFuture<Map<String, Object>> fShop0 = shop0.getResultsAsync();

            platform.killComponent();
            logger.debug("=====END=====");

            ConcurrentUtil.sleepSilently(2000);
            logger.debug("agent0: {}", fAgent0.get().get("needs"));
            logger.debug("agent1: {}", fAgent1.get().get("needs"));
            logger.debug("shop0: {}", fShop0.get().get("products"));
        });
    }
}
