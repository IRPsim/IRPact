package de.unileipzig.irpact.jadex.agent.simulation;

import de.unileipzig.irpact.core.simulation.EntityType;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.util.JadexUtil;
import jadex.bridge.IExternalAccess;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import jadex.micro.annotation.RequiredService;
import jadex.micro.annotation.RequiredServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
@RequiredServices(
        @RequiredService(type = KillPlatformService.class)
)
@Agent
public class JadexKillPlatformAgent extends JadexPlatformControlAgent {

    //Argument names
    public static final String NAME = "name";
    public static final String PLATFORM_TO_KILL = "platformToKill";

    private static final Logger logger = LoggerFactory.getLogger(JadexKillPlatformAgent.class);

    //Jadex parameter
    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;

    //ConsumerAgent parameter
    protected String name;
    protected String platformToKill;

    public JadexKillPlatformAgent() {
    }

    @Override
    protected void initArgs(Map<String, Object> args) {
        try {
            name = get(args, NAME);
            platformToKill = get(args, PLATFORM_TO_KILL);
        } catch (Throwable t) {
            String _name = name == null
                    ? getClass().getSimpleName()
                    : name;
            logger.error("[" + _name + "] initArgs error", t);
            throw t;
        }
    }

    @Override
    public JadexSimulationEnvironment getEnvironment() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean is(EntityType type) {
        switch (type) {
            case AGENT:
            case SIMULATION_AGENT:
                return true;

            default:
                return false;
        }
    }

    @Override
    public Logger logger() {
        return logger;
    }

    //=========================
    //lifecycle
    //=========================

    @OnInit
    @Override
    protected void onInit() {
        initArgs(resultsFeature.getArguments());
        logger.debug("[{}] onInit", name);
    }

    @OnStart
    @Override
    protected void onStart() {
        logger.debug("[{}] onStart", name);
        killMainPlatform();
    }

    @OnEnd
    @Override
    protected void onEnd() {
        logger.debug("[{}] onEnd", name);
    }

    //=========================
    //kill
    //=========================

    @Override
    public String getName() {
        return name;
    }

    @Override
    protected IExternalAccess getPlatform() {
        return agent.getExternalAccess(agent.getId().getRoot());
    }

    protected void killMainPlatform() {
        Collection<KillPlatformService> services = reqFeature.searchServices(JadexUtil.searchNetwork(KillPlatformService.class))
                .get();
        if(services.isEmpty()) {
            logger.error("[{}] No KillPlatformService found!", name);
        } else {
            logger.info("[{}] {} KillPlatformServices found.", name, services.size());
            int killCount = 0;
            for(KillPlatformService service: services) {
                IFuture<Boolean> killResult = service.killPlatform(platformToKill);
                if(killResult.get()) {
                    killCount++;
                }
            }
            logger.info("[{}] KillCount: {}", name, killCount);
        }
        killThisPlatformAsync();
    }
}
