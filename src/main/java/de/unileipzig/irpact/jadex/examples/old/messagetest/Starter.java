package de.unileipzig.irpact.jadex.examples.old.messagetest;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.types.cms.CreationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class Starter {

    private static final Logger logger = LoggerFactory.getLogger("JadexLogger");

    @SuppressWarnings("SameParameterValue")
    private static CreationInfo createConsumerAgentBDIInfo(
            String name,
            Logger logger,
            Set<String> products,
            Set<IComponentIdentifier> ids,
            String need) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename("de.unileipzig.irpact.jadex.examples.old.messagetest.ConsumerAgentBDI.class");
        info.addArgument("name", name);
        info.addArgument("logger", logger);
        info.addArgument("products", products);
        info.addArgument("need", need);
        info.addArgument("ids", ids);
        return info;
    }

    public static void main(String[] args) {
        logger.debug("=====START=====");

        //IPlatformConfiguration config = PlatformConfigurationHandler.getDefaultNoGui();
        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        //config.setLogging(true);

        IExternalAccess platform = jadex.base.Starter.createPlatform(config)
                .get();

        Set<String> products = new HashSet<>();
        Set<IComponentIdentifier> ids = new HashSet<>();

        IExternalAccess agent0 = platform.createComponent(createConsumerAgentBDIInfo("agent0", logger, products, ids, "haus"))
                .get();
        ids.add(agent0.getId());

        IExternalAccess agent1 = platform.createComponent(createConsumerAgentBDIInfo("agent1", logger, products, ids, "haus"))
                .get();
        ids.add(agent1.getId());

        ConcurrentUtil.start(0L, () -> {

            logger.debug("kill in 20");
            ConcurrentUtil.sleepSilently(5000);
            logger.debug("kill in 15");
            ConcurrentUtil.sleepSilently(5000);
            logger.debug("kill in 10");
            ConcurrentUtil.sleepSilently(5000);
            logger.debug("kill in 5");
            ConcurrentUtil.sleepSilently(5000);

            platform.killComponent();
            logger.debug("=====END=====");
        });
    }
}
