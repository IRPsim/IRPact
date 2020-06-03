package de.unileipzig.irpact.jadex.agent.policy;

import de.unileipzig.irpact.core.agent.policy.PolicyAgent;
import de.unileipzig.irpact.core.agent.policy.PolicyAgentData;
import de.unileipzig.irpact.core.agent.policy.TaxesScheme;
import de.unileipzig.irpact.core.simulation.EntityType;
import de.unileipzig.irpact.jadex.agent.Identifier;
import de.unileipzig.irpact.jadex.agent.JadexAgentBase;
import de.unileipzig.irpact.jadex.agent.JadexAgentService;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.component.IMessageFeature;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
@Service
@ProvidedServices({
        @ProvidedService(type = PolicyAgentService.class),
        @ProvidedService(type = JadexAgentService.class)
})
@Agent(type = BDIAgentFactory.TYPE)
public class JadexPolicyAgentBDI extends JadexAgentBase
        implements PolicyAgent, PolicyAgentService, JadexAgentService {

    //general
    private static final Logger logger = LoggerFactory.getLogger(JadexPolicyAgentBDI.class);

    //Jadex parameter
    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IBDIAgentFeature bdiFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;
    @AgentFeature
    protected IMessageFeature msgFeature;

    //non-Beliefs
    protected PolicyAgentData data;

    //Beliefs


    //=========================
    //Constructer
    //=========================

    public JadexPolicyAgentBDI() {
    }

    //=========================
    //ConsumerAgent
    //=========================

    @Override
    public double getInformationAuthority() {
        return data.getInformationAuthority();
    }

    @Override
    public boolean is(EntityType type) {
        switch (type) {
            case AGENT:
            case INFORMATION_AGENT:
            case POLICY_AGENT:
                return true;

            default:
                return false;
        }
    }

    @Override
    public TaxesScheme getTaxesScheme() {
        return data.getTaxesScheme();
    }

    //=========================
    //JadexAgentBase
    //=========================

    @Override
    protected Logger logger() {
        return logger;
    }

    @Override
    protected IComponentIdentifier getCompnentIdentifier() {
        return agent.getId();
    }

    @Override
    protected IMessageFeature getMessageFeature() {
        return msgFeature;
    }

    @Override
    protected void initArgsThis(Map<String, Object> args) {
        name = get(args, Identifier.NAME);
        environment = get(args, Identifier.ENVIRONMENT);
        data = get(args, Identifier.DATA);
    }

    //=========================
    //lifecycle
    //=========================

    @OnInit
    @Override
    protected void onInit() {
        initArgs(resultsFeature.getArguments());
        getEnvironment().getConfiguration().register(agent.getExternalAccess(), this);
        initMessageHandler();
        logger.trace("[{}] onInit", getName());
    }

    @OnStart
    @Override
    protected void onStart() {
        logger.trace("[{}] onStart", getName());
    }

    @OnEnd
    @Override
    protected void onEnd() {
        logger.trace("[{}] onEnd", getName());
    }

    //=========================
    //CompanyAgentService
    //=========================

    @Override
    public JadexPolicyAgentBDI getPolicyAgentSyn() {
        return this;
    }

    @Override
    public IFuture<JadexPolicyAgentBDI> getPolicyAgentAsyn() {
        return new Future<>(this);
    }

    //=========================
    //JadexAgentService
    //=========================

    @Override
    public JadexPolicyAgentBDI getAgentSyn() {
        return getPolicyAgentSyn();
    }

    @Override
    public IFuture<JadexPolicyAgentBDI> getAgentAsyn() {
        return getPolicyAgentAsyn();
    }
}
