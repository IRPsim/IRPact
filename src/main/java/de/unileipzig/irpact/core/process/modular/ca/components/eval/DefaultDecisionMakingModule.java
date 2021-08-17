package de.unileipzig.irpact.core.process.modular.ca.components.eval;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.commons.util.MathUtil;
import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPLoggingMessageCollection;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.InfoTag;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.network.filter.NodeFilter;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.filter.ProcessPlanNodeFilterScheme;
import de.unileipzig.irpact.core.process.modular.ca.AdoptionResult;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.Stage;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentEvaluationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.base.AbstractConsumerAgentModule;
import de.unileipzig.irpact.core.process.modular.ca.model.ConsumerAgentMPM;
import de.unileipzig.irpact.core.process.modular.ca.util.AdoptionPhaseDeterminer;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process.ra.npv.NPVCalculator;
import de.unileipzig.irpact.core.process.ra.npv.NPVData;
import de.unileipzig.irpact.core.process.ra.npv.NPVDataSupplier;
import de.unileipzig.irpact.core.process.ra.npv.NPVMatrix;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.AdoptionPhase;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class DefaultDecisionMakingModule extends AbstractConsumerAgentModule implements ConsumerAgentEvaluationModule {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DefaultDecisionMakingModule.class);

    public DefaultDecisionMakingModule() {
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    //=========================
    //param
    //=========================

    protected double a;
    public void setA(double a) {
        this.b = a;
    }
    public double getA() {
        return b;
    }

    protected double b;
    public void setB(double b) {
        this.b = b;
    }
    public double getB() {
        return b;
    }

    protected double c;
    public void setC(double c) {
        this.c = c;
    }
    public double getC() {
        return c;
    }

    protected double d;
    public void setD(double d) {
        this.d = d;
    }
    public double getD() {
        return d;
    }

    protected double logisticFactor;
    public void setLogisticFactor(double logisticFactor) {
        this.logisticFactor = logisticFactor;
    }
    public double getLogisticFactor() {
        return logisticFactor;
    }

    protected double weightFT;
    public void setWeightFT(double weightFT) {
        this.weightFT = weightFT;
    }
    public double getWeightFT() {
        return weightFT;
    }

    protected double weightNPV;
    public void setWeightNPV(double weightNPV) {
        this.weightNPV = weightNPV;
    }
    public double getWeightNPV() {
        return weightNPV;
    }

    protected double weightSocial;
    public void setWeightSocial(double weightSocial) {
        this.weightSocial = weightSocial;
    }
    public double getWeightSocial() {
        return weightSocial;
    }

    protected double weightLocal;
    public void setWeightLocal(double weightLocal) {
        this.weightLocal = weightLocal;
    }
    public double getWeightLocal() {
        return weightLocal;
    }

    protected NPVData npvData;
    public void setNPVData(NPVData npvData) {
        this.npvData = npvData;
    }
    public NPVData getNPVData() {
        return npvData;
    }
    protected NPVData getValidNPVData() {
        NPVData data = getNPVData();
        if(data == null) {
            throw new NullPointerException("NPVData");
        }
        return data;
    }

    protected final NPVDataSupplier npvDataSupplier = new NPVDataSupplier();
    public NPVDataSupplier getNPVDataSupplier() {
        return npvDataSupplier;
    }

    protected AdoptionPhaseDeterminer phaseDeterminer;
    public void setPhaseDeterminer(AdoptionPhaseDeterminer phaseDeterminer) {
        this.phaseDeterminer = phaseDeterminer;
    }
    public AdoptionPhaseDeterminer getPhaseDeterminer() {
        return phaseDeterminer;
    }

    protected ProcessPlanNodeFilterScheme nodeFilterScheme;
    public void setNodeFilterScheme(ProcessPlanNodeFilterScheme nodeFilterScheme) {
        this.nodeFilterScheme = nodeFilterScheme;
    }
    public ProcessPlanNodeFilterScheme getNodeFilterScheme() {
        return nodeFilterScheme;
    }
    protected ProcessPlanNodeFilterScheme getValidNodeFilterScheme() {
        ProcessPlanNodeFilterScheme filter = getNodeFilterScheme();
        if(filter == null) {
            throw new NullPointerException("ProcessPlanNodeFilterScheme");
        }
        return filter;
    }

    protected final Map<ProcessPlan, NodeFilter> filters = new HashMap<>();
    protected NodeFilter getFilter(ProcessPlan plan) {
        NodeFilter filter = filters.get(plan);
        if(filter == null) {
            return getFilter0(plan);
        } else {
            return filter;
        }
    }
    protected synchronized NodeFilter getFilter0(ProcessPlan plan) {
        NodeFilter filter = filters.get(plan);
        if(filter == null) {
            filter = getValidNodeFilterScheme().createFilter(plan);
            filters.put(plan, filter);
        }
        return filter;
    }

    //=========================
    //init
    //=========================

    @Override
    public void setEnvironment(SimulationEnvironment environment) {
        super.setEnvironment(environment);
        getNPVDataSupplier().setAttributeHelper(getAttributeHelper());
    }

    @Override
    public void preAgentCreation() throws MissingDataException {
        super.preAgentCreation();
        if(npvData != null) {
            initNPVMatrixWithFile();
        }
    }

    protected void initNPVMatrixWithFile() {
        initNPVMatrixWithFile(getValidNPVData(), getNPVDataSupplier());
    }

    @Override
    public void preAgentCreationValidation() throws ValidationException {
        super.preAgentCreationValidation();
        if(npvData == null) {
            throw new ValidationException("missing NPV data");
        }
        if(nodeFilterScheme == null) {
            throw new ValidationException("missing node filter scheme");
        }

        checkGroupAttributExistance();
    }

    protected void checkGroupAttributExistance() throws ValidationException {
        validateHasGroupAttribute(
                RAConstants.NOVELTY_SEEKING,
                RAConstants.DEPENDENT_JUDGMENT_MAKING,
                RAConstants.ENVIRONMENTAL_CONCERN,
                RAConstants.CONSTRUCTION_RATE,
                RAConstants.RENOVATION_RATE,
                RAConstants.REWIRING_RATE,
                RAConstants.COMMUNICATION_FREQUENCY_SN
        );
    }

    @Override
    public void postAgentCreationValidation() throws ValidationException {
        super.postAgentCreationValidation();
        checkAttributeExistence();
    }

    protected void checkAttributeExistence() throws ValidationException {
        validateHasDoubleAttribute(
                RAConstants.PURCHASE_POWER_EUR,
                RAConstants.ORIENTATION,
                RAConstants.SLOPE,
                RAConstants.SHARE_1_2_HOUSE,
                RAConstants.HOUSE_OWNER
        );
    }

    @Override
    public void handleMissingParametersRecursively(ConsumerAgentMPM model) {
        if(phaseDeterminer == null && model instanceof AdoptionPhaseDeterminer) {
            setPhaseDeterminer((AdoptionPhaseDeterminer) model);
        }
    }

    //=========================
    //core
    //=========================

    @Override
    public AdoptionResult evaluate(ConsumerAgentData data) throws Throwable {
        ConsumerAgent agent = data.getAgent();

        doSelfActionAndAllowAttention(agent);

        IRPLoggingMessageCollection logColl = new IRPLoggingMessageCollection()
                .setLazy(true)
                .setAutoDispose(true);
        logColl.append("{} [{}] calculate U", InfoTag.DECISION_MAKING, agent.getName());

        double a = getA();
        double b = getB();
        double c = getC();
        double d = getD();

        double B = 0.0;

        if(a != 0.0) {
            double financial = getFinancialComponent(agent);
            double threshold = getFinancialThreshold(agent, data.getProduct());

            if(financial < threshold) {
                logColl.append("financial component < financial threshold ({} < {}) = {}", financial, threshold, true);

                data.updateStage(Stage.IMPEDED);
                return AdoptionResult.IMPEDED;
            } else {
                double aPart = a * financial;
                logColl.append("a * financial component = {} * {} = {}", a, financial, aPart);
                B += aPart;
            }
        } else {
            logColl.append("a = 0");
        }

        if(b != 0.0) {
            double env = getEnvironmentalComponent(agent);
            double bPart = b * env;
            logColl.append("b * environmental component = {} * {} = {}", b, env, bPart);
            B += bPart;
        } else {
            logColl.append("b = 0");
        }

        if(c != 0.0) {
            double ns = getNoveltyCompoenent(agent);
            double cPart = b * ns;
            logColl.append("c * novelty component = {} * {} = {}", c, ns, cPart);
            B += cPart;
        } else {
            logColl.append("c = 0");
        }

        if(d != 0.0) {
            double social = getSocialComponent(agent, data.getProduct(), data.getPlan());
            double dPart = d * social;
            logColl.append("d * social component = {} * {} = {}", d, social, dPart);
            B += dPart;
        } else {
            logColl.append("d = 0");
        }

        double adoptionThreshold = getAdoptionThreshold(agent, data.getProduct());
        boolean noAdoption = B < adoptionThreshold;

        logColl.append("U < adoption threshold ({} < {}): {}", B, adoptionThreshold, noAdoption);

        if(noAdoption) {
            data.updateStage(Stage.IMPEDED);
            return AdoptionResult.IMPEDED;
        } else {
            Timestamp now = now();
            agent.adopt(data.getNeed(), data.getProduct(), now, determinePhase(now));
            data.updateStage(Stage.ADOPTED);
            return AdoptionResult.ADOPTED;
        }
    }

    protected double getFinancialComponent(ConsumerAgent agent) {
        double avgNPV = getAverageNPV();
        double agentNPV = getNPV(agent);

        double avgFT = getAverageFinancialPurchasePower();
        double agentFT = getFinancialPurchasePower(agent);

        double ft = getLogisticFactor() * (agentFT - avgFT);
        double npv = getLogisticFactor() * (agentNPV - avgNPV);

        double logisticFT = MathUtil.logistic(ft);
        double logisticNPV = MathUtil.logistic(npv);

        double weightedFT = getWeightFT() * logisticFT;
        double weightedNPV = getWeightNPV() * logisticNPV;

        return weightedFT + weightedNPV;
    }

    protected double getEnvironmentalComponent(ConsumerAgent agent) {
        return getEnvironmentalConcern(agent);
    }

    protected double getNoveltyCompoenent(ConsumerAgent agent) {
        return getNoveltySeeking(agent);
    }

    protected double getSocialComponent(ConsumerAgent agent, Product product, ProcessPlan plan) {
        MutableDouble shareOfAdopterInSocialNetwork = MutableDouble.zero();
        MutableDouble shareOfAdopterInLocalArea = MutableDouble.zero();

        getShareOfAdopterInSocialNetworkAndLocalArea(
                agent,
                product,
                getFilter(plan),
                shareOfAdopterInSocialNetwork,
                shareOfAdopterInLocalArea
        );

        double djm = getDependentJudgmentMaking(agent);

        double socialPart = getWeightSocial() * shareOfAdopterInSocialNetwork.get();
        double localPart = getWeightLocal() * shareOfAdopterInLocalArea.get();

        return djm * (socialPart + localPart);
    }

    protected void getShareOfAdopterInSocialNetworkAndLocalArea(
            ConsumerAgent agent,
            Product product,
            NodeFilter filter,
            MutableDouble social,
            MutableDouble local) {
        social.set(getShareOfAdopterInSocialNetwork(agent, product));
        local.set(getShareOfAdopterInLocalNetwork(product, filter));
    }

    //=========================
    //util
    //=========================


    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                getA(),
                getB(),
                getC(),
                getD(),
                getWeightFT(),
                getWeightNPV(),
                getWeightLocal(),
                getWeightSocial(),
                getLogisticFactor(),
                getNodeFilterScheme(),
                Checksums.SMART.getNamedChecksum(getPhaseDeterminer())
        );
    }

    protected AdoptionPhase determinePhase(Timestamp ts) {
        AdoptionPhaseDeterminer determiner = getPhaseDeterminer();
        if(determiner == null) {
            throw new NullPointerException("AdoptionPhaseDeterminer");
        }
        return determiner.determine(ts);
    }

    protected double getAverageNPV() {
        return getAverageNPV(getNPVDataSupplier());
    }

    protected double getNPV(ConsumerAgent agent) {
        return getNPV(getNPVDataSupplier(), agent);
    }

    protected double getAverageFinancialPurchasePower() {
        return getAverageFinancialPurchasePower(getNPVDataSupplier());
    }
}
