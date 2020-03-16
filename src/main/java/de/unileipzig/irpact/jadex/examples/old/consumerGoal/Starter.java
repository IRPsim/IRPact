package de.unileipzig.irpact.jadex.examples.old.consumerGoal;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
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
            String need) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename("de.unileipzig.irpact.jadex.examples.old.consumerGoal.ConsumerAgentBDI.class");
        info.addArgument("name", name);
        info.addArgument("logger", logger);
        info.addArgument("products", products);
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
        info.setFilename("de.unileipzig.irpact.jadex.examples.old.consumerGoal.CompanyAgentBDI.class");
        info.addArgument("name", name);
        info.addArgument("logger", logger);
        info.addArgument("products", products);
        return info;
    }

    public static void main(String[] args) {
        logger.debug("=====START=====");

        //IPlatformConfiguration config = PlatformConfigurationHandler.getDefaultNoGui();
        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        //config.setLogging(true);
        //config.addComponent("de.unileipzig.irpact.demo.bdi.TestAgentBDI.class");


        IExternalAccess platform = jadex.base.Starter.createPlatform(config)
                .get();

        Set<String> products = new HashSet<>();
        products.add("baum");


        IExternalAccess agent0 = platform.createComponent(createConsumerAgentBDIInfo("agent0", logger, products, "haus"))
                .get();

        /*
        IExternalAccess agent1 = platform.createComponent(createConsumerAgentBDIInfo("agent1", logger, products, "baum"))
                .get();
         */

        IExternalAccess shop1 = platform.createComponent(createCompanyAgentBDIInfo("shop0", logger, products))
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

            platform.killComponent();
            logger.debug("=====END=====");
        });
    }
}
