package de.unileipzig.irpact.io.param.input.agent.consumer;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentAnnualGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentDoubleGroupAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.product.awareness.ProductBinaryAwarenessSupplyScheme;
import de.unileipzig.irpact.core.product.interest.ProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.core.start.InputParser;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.core.process.ra.RAConstants.*;
import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition
public class InPVactConsumerAgentGroup implements InConsumerAgentGroup {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), AGENTS, CONSUMER, CONSUMER_GROUP, thisName());
        addEntry(res, thisClass(), "noveltySeeking");
        addEntry(res, thisClass(), "dependentJudgmentMaking");
        addEntry(res, thisClass(), "environmentalConcern");
        addEntry(res, thisClass(), "interestThreshold");
        addEntry(res, thisClass(), "financialThreshold");
        addEntry(res, thisClass(), "adoptionThreshold");
        addEntry(res, thisClass(), "communication");
        addEntry(res, thisClass(), "rewire");
        addEntry(res, thisClass(), "initialAdopter");
        addEntry(res, thisClass(), "rateOfConvergence");
        addEntry(res, thisClass(), "initialProductInterest");
        addEntry(res, thisClass(), "initialProductAwareness");
        addEntry(res, thisClass(), "constructionRate");
        addEntry(res, thisClass(), "renovationRate");
        addEntry(res, thisClass(), "spatialDistribution");

        setDelta(res, thisClass(), "constructionRate");
        setDelta(res, thisClass(), "renovationRate");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] noveltySeeking;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] dependentJudgmentMaking;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] environmentalConcern;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] interestThreshold;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] financialThreshold;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] adoptionThreshold;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] communication;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] rewire;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] initialAdopter;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] rateOfConvergence;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] initialProductInterest;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] initialProductAwareness;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] constructionRate;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] renovationRate;

    @FieldDefinition
    public InSpatialDistribution[] spatialDistribution;

    public InGeneralConsumerAgentAnnualGroupAttribute[] dummyAnnuals;

    //=========================
    //toy model parameter
    //=========================

    public InUnivariateDoubleDistribution a1;
    public InUnivariateDoubleDistribution a5;
    public InUnivariateDoubleDistribution a6;

    public InPVactConsumerAgentGroup() {
    }

    public void setForAll(InUnivariateDoubleDistribution dist) {
        setNoveltySeeking(dist);
        setDependentJudgmentMaking(dist);
        setEnvironmentalConcern(dist);
        setInterestThreshold(dist);
        setFinancialThreshold(dist);
        setAdoptionThreshold(dist);
        setCommunication(dist);
        setRewire(dist);
        setInitialAdopter(dist);
        setRateOfConvergence(dist);
        setInitialProductAwareness(dist);
        setInitialProductInterest(dist);
        setConstructionRate(dist);
        setRenovationRate(dist);
    }

    public void setForAllSpecial(InUnivariateDoubleDistribution dist) {
        a1 = dist;
        a5 = dist;
        a6 = dist;
    }

    @Override
    public InPVactConsumerAgentGroup copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InPVactConsumerAgentGroup newCopy(CopyCache cache) {
        InPVactConsumerAgentGroup copy = new InPVactConsumerAgentGroup();
        copy._name = _name;
        copy.noveltySeeking = cache.copyArray(noveltySeeking);
        copy.dependentJudgmentMaking = cache.copyArray(dependentJudgmentMaking);
        copy.environmentalConcern = cache.copyArray(environmentalConcern);
        copy.interestThreshold = cache.copyArray(interestThreshold);
        copy.financialThreshold = cache.copyArray(financialThreshold);
        copy.adoptionThreshold = cache.copyArray(adoptionThreshold);
        copy.communication = cache.copyArray(communication);
        copy.rewire = cache.copyArray(rewire);
        copy.initialAdopter = cache.copyArray(initialAdopter);
        copy.rateOfConvergence = cache.copyArray(rateOfConvergence);
        copy.initialProductInterest = cache.copyArray(initialProductInterest);
        copy.initialProductAwareness = cache.copyArray(initialProductAwareness);
        copy.constructionRate = cache.copyArray(constructionRate);
        copy.renovationRate = cache.copyArray(renovationRate);
        copy.spatialDistribution = cache.copyArray(spatialDistribution);
        copy.dummyAnnuals = cache.copyArray(dummyAnnuals);
        return copy;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public InUnivariateDoubleDistribution getNoveltySeeking() throws ParsingException {
        return ParamUtil.getInstance(noveltySeeking, "noveltySeeking");
    }

    public void setNoveltySeeking(InUnivariateDoubleDistribution noveltySeeking) {
        this.noveltySeeking = new InUnivariateDoubleDistribution[]{noveltySeeking};
    }
    public void setA2(InUnivariateDoubleDistribution noveltySeeking) {
        setNoveltySeeking(noveltySeeking);
    }

    public InUnivariateDoubleDistribution getDependentJudgmentMaking() throws ParsingException {
        return ParamUtil.getInstance(dependentJudgmentMaking, "independentJudgmentMaking");
    }

    public void setDependentJudgmentMaking(InUnivariateDoubleDistribution dependentJudgmentMaking) {
        this.dependentJudgmentMaking = new InUnivariateDoubleDistribution[]{dependentJudgmentMaking};
    }
    public void setA3(InUnivariateDoubleDistribution dependentJudgmentMaking) {
        setDependentJudgmentMaking(dependentJudgmentMaking);
    }

    public InUnivariateDoubleDistribution getEnvironmentalConcern() throws ParsingException {
        return ParamUtil.getInstance(environmentalConcern, "environmentalConcern");
    }

    public void setEnvironmentalConcern(InUnivariateDoubleDistribution environmentalConcern) {
        this.environmentalConcern = new InUnivariateDoubleDistribution[]{environmentalConcern};
    }
    public void setA4(InUnivariateDoubleDistribution environmentalConcern) {
        setEnvironmentalConcern(environmentalConcern);
    }

    public InUnivariateDoubleDistribution getInterestThreshold() throws ParsingException {
        return ParamUtil.getInstance(interestThreshold, "interestThreshold");
    }

    public void setInterestThreshold(InUnivariateDoubleDistribution interestThreshold) {
        this.interestThreshold = new InUnivariateDoubleDistribution[]{interestThreshold};
    }
    public void setD2(InUnivariateDoubleDistribution interestThreshold) {
        setInterestThreshold(interestThreshold);
    }

    public InUnivariateDoubleDistribution getFinancialThreshold() throws ParsingException {
        return ParamUtil.getInstance(financialThreshold, "financialThreshold");
    }

    public void setFinancialThreshold(InUnivariateDoubleDistribution financialThreshold) {
        this.financialThreshold = new InUnivariateDoubleDistribution[]{financialThreshold};
    }
    public void setD3(InUnivariateDoubleDistribution financialThreshold) {
        setFinancialThreshold(financialThreshold);
    }

    public InUnivariateDoubleDistribution getAdoptionThreshold() throws ParsingException {
        return ParamUtil.getInstance(adoptionThreshold, "adoptionThreshold");
    }

    public void setAdoptionThreshold(InUnivariateDoubleDistribution adoptionThreshold) {
        this.adoptionThreshold = new InUnivariateDoubleDistribution[]{adoptionThreshold};
    }
    public void setD4(InUnivariateDoubleDistribution adoptionThreshold) {
        setAdoptionThreshold(adoptionThreshold);
    }

    public InUnivariateDoubleDistribution getCommunication() throws ParsingException {
        return ParamUtil.getInstance(communication, "communication");
    }

    public void setCommunication(InUnivariateDoubleDistribution communication) {
        this.communication = new InUnivariateDoubleDistribution[]{communication};
    }
    public void setC1(InUnivariateDoubleDistribution communication) {
        setCommunication(communication);
    }

    public InUnivariateDoubleDistribution getRewire() throws ParsingException {
        return ParamUtil.getInstance(rewire, "rewire");
    }

    public void setRewire(InUnivariateDoubleDistribution rewire) {
        this.rewire = new InUnivariateDoubleDistribution[]{rewire};
    }
    public void setB6(InUnivariateDoubleDistribution rewire) {
        setRewire(rewire);
    }

    public InUnivariateDoubleDistribution getInitialAdopter() throws ParsingException {
        return ParamUtil.getInstance(initialAdopter, "initialAdopter");
    }

    public void setInitialAdopter(InUnivariateDoubleDistribution initialAdopter) {
        this.initialAdopter = new InUnivariateDoubleDistribution[]{initialAdopter};
    }
    public void setD5(InUnivariateDoubleDistribution initialAdopter) {
        setInitialAdopter(initialAdopter);
    }

    public InUnivariateDoubleDistribution getRateOfConvergence() throws ParsingException {
        return ParamUtil.getInstance(rateOfConvergence, "rateOfConvergence");
    }

    public void setRateOfConvergence(InUnivariateDoubleDistribution rateOfConvergence) {
        this.rateOfConvergence = new InUnivariateDoubleDistribution[]{rateOfConvergence};
    }
    public void setC3(InUnivariateDoubleDistribution rateOfConvergence) {
        setRateOfConvergence(rateOfConvergence);
    }

    public InUnivariateDoubleDistribution getInitialProductInterest() throws ParsingException {
        return ParamUtil.getInstance(initialProductInterest, "initialProductInterest");
    }

    public void setInitialProductInterest(InUnivariateDoubleDistribution initialProductInterest) {
        this.initialProductInterest = new InUnivariateDoubleDistribution[]{initialProductInterest};
    }
    public void setD6(InUnivariateDoubleDistribution initialProductInterest) {
        setInitialProductInterest(initialProductInterest);
    }

    public InUnivariateDoubleDistribution getInitialProductAwareness() throws ParsingException {
        return ParamUtil.getInstance(initialProductAwareness, "initialProductAwareness");
    }

    public void setInitialProductAwareness(InUnivariateDoubleDistribution initialProductAwareness) {
        this.initialProductAwareness = new InUnivariateDoubleDistribution[]{initialProductAwareness};
    }
    public void setD1(InUnivariateDoubleDistribution initialProductAwareness) {
        setInitialProductAwareness(initialProductAwareness);
    }

    public InUnivariateDoubleDistribution getConstructionRate() throws ParsingException {
        return ParamUtil.getInstance(constructionRate, "constructionRate");
    }

    public void setConstructionRate(InUnivariateDoubleDistribution constructionRate) {
        this.constructionRate = new InUnivariateDoubleDistribution[]{constructionRate};
    }
    public void setA7(InUnivariateDoubleDistribution constructionRate) {
        setConstructionRate(constructionRate);
    }

    public InUnivariateDoubleDistribution getRenovationRate() throws ParsingException {
        return ParamUtil.getInstance(renovationRate, "renovationRate");
    }

    public void setRenovationRate(InUnivariateDoubleDistribution renovationRate) {
        this.renovationRate = new InUnivariateDoubleDistribution[]{renovationRate};
    }
    public void setA8(InUnivariateDoubleDistribution renovationRate) {
        setRenovationRate(renovationRate);
    }

    @Override
    public InSpatialDistribution getSpatialDistribution() throws ParsingException {
        return ParamUtil.getInstance(spatialDistribution, "spatialDistribution");
    }

    @Override
    public void setSpatialDistribution(InSpatialDistribution dist) {
        this.spatialDistribution = new InSpatialDistribution[]{dist};
    }

    @Override
    public JadexConsumerAgentGroup parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored() && parser.getEnvironment().getAgents().hasConsumerAgentGroup(getName())) {
            JadexConsumerAgentGroup jcag = (JadexConsumerAgentGroup) parser.getEnvironment().getAgents().getConsumerAgentGroup(getName());
            return update(parser, jcag);
        }

        AgentManager agentManager = parser.getEnvironment().getAgents();

        JadexConsumerAgentGroup jcag = new JadexConsumerAgentGroup();
        jcag.setEnvironment(parser.getEnvironment());
        jcag.setName(getName());
        jcag.setInformationAuthority(1.0);
        jcag.setMaxNumberOfActions(1);

        if(agentManager.hasConsumerAgentGroup(getName())) {
            throw new ParsingException("ConsumerAgentGroup '" + getName() + "' already exists");
        }
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse InPVactConsumerAgentGroup '{}'", jcag.getName());
        if(parser.getRoot().addConsumerAgentGroup(this)) {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "added InPVactConsumerAgentGroup '{}' to InRoot", getName());
            agentManager.addConsumerAgentGroup(jcag);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "added JadexConsumerAgentGroup '{}'", jcag.getName());
        }

        addGroupAttributes(parser, jcag, false);
        addSupplySchemes(jcag, false);
        getSpatialDistribution().setup(parser, jcag);
        handleCustomAnnual(parser, jcag);

        return jcag;
    }

    public JadexConsumerAgentGroup update(IRPactInputParser parser, JadexConsumerAgentGroup restored) throws ParsingException {
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "update '{}'", restored.getName());

        addGroupAttributes(parser, restored, true);
        addSupplySchemes(restored, true);
        getSpatialDistribution().setup(parser, restored);
        handleCustomAnnual(parser, restored);
        return restored;
    }

    private void addGroupAttributes(IRPactInputParser parser, JadexConsumerAgentGroup jcag, boolean restored) throws ParsingException {
        addGroupAttribute(parser, jcag, getNoveltySeeking(), NOVELTY_SEEKING, restored);
        addGroupAttribute(parser, jcag, getDependentJudgmentMaking(), DEPENDENT_JUDGMENT_MAKING, restored);
        addGroupAttribute(parser, jcag, getEnvironmentalConcern(), ENVIRONMENTAL_CONCERN, restored);
        addGroupAttribute(parser, jcag, getFinancialThreshold(), FINANCIAL_THRESHOLD, restored);
        addGroupAttribute(parser, jcag, getAdoptionThreshold(), ADOPTION_THRESHOLD, restored);
        addGroupAttribute(parser, jcag, getCommunication(), COMMUNICATION_FREQUENCY_SN, restored);
        addGroupAttribute(parser, jcag, getRewire(), REWIRING_RATE, restored);
        addGroupAttribute(parser, jcag, getInitialAdopter(), INITIAL_ADOPTER, restored);
        addGroupAttribute(parser, jcag, getRateOfConvergence(), RATE_OF_CONVERGENCE, restored);
        addGroupAttribute(parser, jcag, getInitialProductInterest(), INITIAL_PRODUCT_INTEREST, restored);
        addGroupAttribute(parser, jcag, getInitialProductAwareness(), INITIAL_PRODUCT_AWARENESS, restored);
        addGroupAttribute(parser, jcag, getInterestThreshold(), INTEREST_THRESHOLD, restored);

        addAnnualGroupAttribute(parser, jcag, getConstructionRate(), CONSTRUCTION_RATE, restored);
        addAnnualGroupAttribute(parser, jcag, getRenovationRate(), RENOVATION_RATE, restored);

        if(a1 != null) addGroupAttribute(parser, jcag, a1, PURCHASE_POWER_EUR, restored);
        if(a5 != null) addGroupAttribute(parser, jcag, a5, SHARE_1_2_HOUSE, restored);
        if(a6 != null) addGroupAttribute(parser, jcag, a6, HOUSE_OWNER, restored);
    }

    private void handleCustomAnnual(IRPactInputParser parser, JadexConsumerAgentGroup jcag) throws ParsingException {
        LOGGER.trace("handle annual ConsumerAgentGroupAttributes: {}", dummyAnnuals == null ? -1 : dummyAnnuals.length);
        if(dummyAnnuals != null) {
            for(InGeneralConsumerAgentAnnualGroupAttribute dummyAnnual: dummyAnnuals) {
                dummyAnnual.setup(parser, jcag);
            }
        }
    }

    private static void addGroupAttribute(
            InputParser parser,
            JadexConsumerAgentGroup jcag,
            InUnivariateDoubleDistribution inDist,
            String name,
            boolean restored) throws ParsingException {
        if(jcag.hasAttribute(name)) {
            if(restored) {
                LOGGER.trace("restored ConsumerAgentGroup '{}' already has ConsumerAgentGroupAttribute '{}'", jcag.getName(), name);
                return;
            } else {
                throw new ParsingException("ConsumerAgentGroupAttribute '" + name + "' already exists in " + jcag.getName());
            }
        }

        UnivariateDoubleDistribution dist = parser.parseEntityTo(inDist);
        addGroupAttribute(jcag, dist, name);
    }

    private static void addGroupAttribute(
            JadexConsumerAgentGroup jcag,
            UnivariateDoubleDistribution dist,
            String name) throws ParsingException {
        if(jcag.hasGroupAttribute(name)) {
            throw new ParsingException("ConsumerAgentGroupAttribute '" + name + "' already exists in " + jcag.getName());
        }

        BasicConsumerAgentDoubleGroupAttribute cagAttr = new BasicConsumerAgentDoubleGroupAttribute();
        cagAttr.setName(name);
        cagAttr.setArtificial(false);
        cagAttr.setDistribution(dist);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "add ConsumerAgentGroupAttribute '{}' to group '{}'", cagAttr.getName(), jcag.getName());
        jcag.addGroupAttribute(cagAttr);
    }

    private void addSupplySchemes(JadexConsumerAgentGroup jcag, boolean restored) throws ParsingException {
        if(jcag.hasAwarenessSupplyScheme()) {
            if(restored) {
                LOGGER.trace("restored ConsumerAgentGroup '{}' already has an AwarenessSupplyScheme '{}'", jcag.getName(), jcag.getAwarenessSupplyScheme().getName());
            } else {
                throw new ParsingException("ConsumerAgentGroup '{}' already has an AwarenessSupplyScheme '{}'", jcag.getName(), jcag.getAwarenessSupplyScheme().getName());
            }
        } else {
            ProductBinaryAwarenessSupplyScheme awarenessSupplyScheme = new ProductBinaryAwarenessSupplyScheme();
            awarenessSupplyScheme.setName(ParamUtil.concName(jcag.getName(), AWARENESS));
            jcag.setAwarenessSupplyScheme(awarenessSupplyScheme);
        }

        if(jcag.hasInterestSupplyScheme()) {
            if(restored) {
                LOGGER.trace("restored ConsumerAgentGroup '{}' already has an InterestSupplyScheme '{}'", jcag.getName(), jcag.getAwarenessSupplyScheme().getName());
            } else {
                throw new ParsingException("ConsumerAgentGroup '{}' already has an InterestSupplyScheme '{}'", jcag.getName(), jcag.getAwarenessSupplyScheme().getName());
            }
        } else {
            ProductThresholdInterestSupplyScheme interestSupplyScheme = new ProductThresholdInterestSupplyScheme();
            interestSupplyScheme.setName(ParamUtil.concName(jcag.getName(), INTEREST));
            jcag.setInterestSupplyScheme(interestSupplyScheme);
        }
    }

    private static void addAnnualGroupAttribute(
            IRPactInputParser parser,
            JadexConsumerAgentGroup jcag,
            InUnivariateDoubleDistribution inDist,
            String name,
            boolean restored) throws ParsingException {
        BasicConsumerAgentAnnualGroupAttribute annualCagAttr;
        if(jcag.hasAttribute(name)) {
            annualCagAttr = (BasicConsumerAgentAnnualGroupAttribute) jcag.getGroupAttribute(name);
            if(annualCagAttr.hasYear(parser.getSimulationYear())) {
                if(restored) {
                    LOGGER.trace("restored ConsumerAgentGroup '{}' already has AnnualConsumerAgentGroupAttribute '{}' (year {})", jcag.getName(), name, parser.getSimulationYear());
                    return;
                } else {
                    throw new ParsingException("ConsumerAgentGroupAttribute '" + name + "' already exists in " + jcag.getName());
                }
            }
        } else {
            annualCagAttr = new BasicConsumerAgentAnnualGroupAttribute();
            annualCagAttr.setName(name);
            annualCagAttr.setArtificial(false);
            jcag.addGroupAttribute(annualCagAttr);
        }

        UnivariateDoubleDistribution dist = parser.parseEntityTo(inDist);
        addAnnualGroupAttribute(parser, annualCagAttr, dist, name);
    }

    private static void addAnnualGroupAttribute(
            IRPactInputParser parser,
            BasicConsumerAgentAnnualGroupAttribute annualCagAttr,
            UnivariateDoubleDistribution dist,
            String name) {
        BasicConsumerAgentDoubleGroupAttribute yearAttr = new BasicConsumerAgentDoubleGroupAttribute();
        yearAttr.setName(name + "_" + parser.getSimulationYear());
        yearAttr.setArtificial(true);
        yearAttr.setDistribution(dist);
        annualCagAttr.put(parser.getSimulationYear(), yearAttr);
    }
}
