package de.unileipzig.irpact.core.process.mra.component.special;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPLoggingMessageCollection;
import de.unileipzig.irpact.core.logging.InfoTag;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.network.filter.NodeFilter;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.ProcessPlanResult;
import de.unileipzig.irpact.core.process.mra.AgentData;
import de.unileipzig.irpact.core.process.mra.ModularRAProcessPlan;
import de.unileipzig.irpact.core.process.mra.component.base.EvaluableComponent;
import de.unileipzig.irpact.core.process.mra.component.generic.ComponentType;
import de.unileipzig.irpact.core.process.ra.RAStage;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class DefaultHandleDecisionMakingComponent extends AbstractSingleMRAComponent implements EvaluableComponent {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DefaultHandleDecisionMakingComponent.class);
    private static final String FILTER_KEY = "$FILTER";

    public DefaultHandleDecisionMakingComponent() {
        super(ComponentType.OUTPUT);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                Checksums.SMART.getNamedChecksum(getModel()),
                getA(), getB(), getC(), getD(),
                getWeightFT(), getWeightNPV(), getWeightSocial(), getWeightLocal(),
                getLogisticFactor()
        );
    }

    @Override
    public void handleNewPlan(ProcessPlan plan) {
        ModularRAProcessPlan mPlan = (ModularRAProcessPlan) plan;
//        NodeFilter filter = getNodeFilterScheme().createFilter(mPlan);
//        mPlan.store(FILTER_KEY, filter);
    }

    @Override
    public void preAgentCreation() {
        trace("[{}] pre agent creation", getName());
        if(npvData != null) {
            initNPVMatrixWithFile();
        }
    }

    @Override
    public void preAgentCreationValidation() throws ValidationException {
        trace("[{}] pre agent creation validation", getName());
        if(npvData == null) {
            throw new ValidationException("missing npv data");
        }
        checkGroupAttributExistanceWithDefaultValues();
    }

    @Override
    public void postAgentCreationValidation() throws ValidationException {
        trace("[{}] post agent creation validation", getName());
        checkHasDefaultSpatialInformations();
    }

    @Override
    public ProcessPlanResult evaluate(Agent ag, AgentData data) {
        try {
            return evaluate0(ag, data);
        } catch (Exception e) {
            getEnvironment().getLifeCycleControl().handleFatalError(e);
            throw e;
        }
    }

    protected ProcessPlanResult evaluate0(Agent ag, AgentData data) {
        ConsumerAgent agent = (ConsumerAgent) ag;

        doSelfActionAndAllowAttention(agent);
        trace("[{}] handle decision making", agent.getName());

        IRPLoggingMessageCollection alm = new IRPLoggingMessageCollection()
                .setLazy(true)
                .setAutoDispose(true);
        alm.append("{} [{}] calculate U", InfoTag.DECISION_MAKING, agent.getName());

        double a = getA();
        double b = getB();
        double c = getC();
        double d = getD();

        double B = 0.0;

        if(a != 0.0) {
            double financial = getFinancialComponent(agent);
            double financialThreshold = getFinancialThreshold(agent, data.getProduct());
            //check D3 reached
            if(financial < financialThreshold) {
                alm.append("financial component < financial threshold ({} < {}) = {}", financial, financialThreshold, true);
                logCalculateDecisionMaking(alm);

                data.setStage(RAStage.IMPEDED);
                return ProcessPlanResult.IMPEDED;
            }
            double temp = a * financial;
            alm.append("a * financial component = {} * {} = {}", a, financial, temp);
            B += temp;
        } else {
            alm.append("a = 0");
        }

        if(b != 0.0) {
            double env = getEnvironmentalComponent(agent);
            double benv = b * env;
            alm.append("b * environmental component = {} * {} = {}", b, env, benv);
            B += benv;
        } else {
            alm.append("b = 0");
        }

        if(c != 0.0) {
            double nov = getNoveltyCompoenent(agent);
            double cnov = c * nov;
            alm.append("c * novelty component = {} * {} = {}", c, nov, cnov);
            B += cnov;
        } else {
            alm.append("c = 0");
        }

        if(d != 0.0) {
            double soc = getSocialComponent(agent, data.getProduct(), data.getExistingAs(FILTER_KEY));
            double dsoc = d * soc;
            alm.append("d * social component = {} * {} = {}", d, soc, dsoc);
            B += dsoc;
        } else {
            alm.append("d = 0");
        }

        double adoptionThreshold = getAdoptionThreshold(agent, data.getProduct());
        boolean noAdoption = B < adoptionThreshold;

        alm.append("U < adoption threshold ({} < {}): {}", B, adoptionThreshold, noAdoption);
        logCalculateDecisionMaking(alm);

        if(noAdoption) {
            data.setStage(RAStage.IMPEDED);
            return ProcessPlanResult.IMPEDED;
        } else {
            Timestamp now = now();
            agent.adopt(data.getNeed(), data.getProduct(), now, determinePhase(now));
            data.setStage(RAStage.ADOPTED);
            return ProcessPlanResult.ADOPTED;
        }
    }
}
