package de.unileipzig.irpact.jadex.agent.simulation;

import de.unileipzig.irpact.core.message.Message;
import de.unileipzig.irpact.core.simulation.EntityType;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.start.StartSimulation;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IMessageFeature;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.bridge.service.types.simulation.ISimulationService;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Service
@ProvidedServices(
        @ProvidedService(type = KillPlatformService.class)
)
@RequiredServices(
        @RequiredService(type = ISimulationService.class)
)
@Agent
public class JadexSimulationControlAgent extends JadexPlatformControlAgent implements KillPlatformService {

    //Argument names
    public static final String NAME = StartSimulation.NAME;
    public static final String ENVIRONMENT = StartSimulation.ENVIRONMENT;

    private static final Logger logger = LoggerFactory.getLogger(JadexSimulationControlAgent.class);

    //Jadex parameter
    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;

    //ConsumerAgent parameter
    protected String name;
    protected JadexSimulationEnvironment environment;

    public JadexSimulationControlAgent() {
    }

    @Override
    protected void initArgsThis(Map<String, Object> args) {
        try {
            name = get(args, NAME);
            environment = get(args, ENVIRONMENT);
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
        return environment;
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
    protected IComponentIdentifier getCompnentIdentifier() {
        return agent.getId();
    }

    @Override
    protected IMessageFeature getMessageFeature() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isHandling(de.unileipzig.irpact.core.agent.Agent sender, Message msg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handleMessage(de.unileipzig.irpact.core.agent.Agent sender, Message msg) {
        throw new UnsupportedOperationException();
    }

    //=========================
    //lifecycle
    //=========================

    @OnInit
    @Override
    protected void onInit() {
        initArgsThis(resultsFeature.getArguments());
        logger.debug("[{}] onInit", name);
    }

    @OnStart
    @Override
    protected void onStart() {
        logger.debug("[{}] onStart", name);
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
    protected Logger logger() {
        return logger;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    protected IExternalAccess getPlatform() {
        return getEnvironment().getPlatform();
    }

    @Override
    public IFuture<Boolean> killPlatform(String platformName) {
        if(Objects.equals(getPlatformName(), platformName) || platformName == null || platformName.isEmpty()) {
            killThisPlatformAsync();
            return IFuture.TRUE;
        } else {
            return IFuture.FALSE;
        }
    }
}
