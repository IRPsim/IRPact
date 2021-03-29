package de.unileipzig.irpact.io.param.input.agent.consumer;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.product.awareness.ProductBinaryAwarenessSupplyScheme;
import de.unileipzig.irpact.core.product.interest.ProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.core.process.ra.RAConstants.*;
import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

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
        setInitialProductInterest(dist);
        setConstructionRate(dist);
        setRenovationRate(dist);
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

    public InUnivariateDoubleDistribution getDependentJudgmentMaking() throws ParsingException {
        return ParamUtil.getInstance(dependentJudgmentMaking, "independentJudgmentMaking");
    }

    public void setDependentJudgmentMaking(InUnivariateDoubleDistribution dependentJudgmentMaking) {
        this.dependentJudgmentMaking = new InUnivariateDoubleDistribution[]{dependentJudgmentMaking};
    }

    public InUnivariateDoubleDistribution getEnvironmentalConcern() throws ParsingException {
        return ParamUtil.getInstance(environmentalConcern, "environmentalConcern");
    }

    public void setEnvironmentalConcern(InUnivariateDoubleDistribution environmentalConcern) {
        this.environmentalConcern = new InUnivariateDoubleDistribution[]{environmentalConcern};
    }

    public InUnivariateDoubleDistribution getInterestThreshold() throws ParsingException {
        return ParamUtil.getInstance(interestThreshold, "interestThreshold");
    }

    public void setInterestThreshold(InUnivariateDoubleDistribution interestThreshold) {
        this.interestThreshold = new InUnivariateDoubleDistribution[]{interestThreshold};
    }

    public InUnivariateDoubleDistribution getFinancialThreshold() throws ParsingException {
        return ParamUtil.getInstance(financialThreshold, "financialThreshold");
    }

    public void setFinancialThreshold(InUnivariateDoubleDistribution financialThreshold) {
        this.financialThreshold = new InUnivariateDoubleDistribution[]{financialThreshold};
    }

    public InUnivariateDoubleDistribution getAdoptionThreshold() throws ParsingException {
        return ParamUtil.getInstance(adoptionThreshold, "adoptionThreshold");
    }

    public void setAdoptionThreshold(InUnivariateDoubleDistribution adoptionThreshold) {
        this.adoptionThreshold = new InUnivariateDoubleDistribution[]{adoptionThreshold};
    }

    public InUnivariateDoubleDistribution getCommunication() throws ParsingException {
        return ParamUtil.getInstance(communication, "communication");
    }

    public void setCommunication(InUnivariateDoubleDistribution communication) {
        this.communication = new InUnivariateDoubleDistribution[]{communication};
    }

    public InUnivariateDoubleDistribution getRewire() throws ParsingException {
        return ParamUtil.getInstance(rewire, "rewire");
    }

    public void setRewire(InUnivariateDoubleDistribution rewire) {
        this.rewire = new InUnivariateDoubleDistribution[]{rewire};
    }

    public InUnivariateDoubleDistribution getInitialAdopter() throws ParsingException {
        return ParamUtil.getInstance(initialAdopter, "initialAdopter");
    }

    public void setInitialAdopter(InUnivariateDoubleDistribution initialAdopter) {
        this.initialAdopter = new InUnivariateDoubleDistribution[]{initialAdopter};
    }

    public InUnivariateDoubleDistribution getRateOfConvergence() throws ParsingException {
        return ParamUtil.getInstance(rateOfConvergence, "rateOfConvergence");
    }

    public void setRateOfConvergence(InUnivariateDoubleDistribution rateOfConvergence) {
        this.rateOfConvergence = new InUnivariateDoubleDistribution[]{rateOfConvergence};
    }

    public InUnivariateDoubleDistribution getInitialProductInterest() throws ParsingException {
        return ParamUtil.getInstance(initialProductInterest, "initialProductInterest");
    }

    public void setInitialProductInterest(InUnivariateDoubleDistribution initialProductInterest) {
        this.initialProductInterest = new InUnivariateDoubleDistribution[]{initialProductInterest};
    }

    public InUnivariateDoubleDistribution getInitialProductAwareness() throws ParsingException {
        return ParamUtil.getInstance(initialProductAwareness, "initialProductAwareness");
    }

    public void setInitialProductAwareness(InUnivariateDoubleDistribution initialProductAwareness) {
        this.initialProductAwareness = new InUnivariateDoubleDistribution[]{initialProductAwareness};
    }

    public InUnivariateDoubleDistribution getConstructionRate() throws ParsingException {
        return ParamUtil.getInstance(constructionRate, "constructionRate");
    }

    public void setConstructionRate(InUnivariateDoubleDistribution constructionRate) {
        this.constructionRate = new InUnivariateDoubleDistribution[]{constructionRate};
    }

    public InUnivariateDoubleDistribution getRenovationRate() throws ParsingException {
        return ParamUtil.getInstance(renovationRate, "renovationRate");
    }

    public void setRenovationRate(InUnivariateDoubleDistribution renovationRate) {
        this.renovationRate = new InUnivariateDoubleDistribution[]{renovationRate};
    }

    public InSpatialDistribution getSpatialDistribution() throws ParsingException {
        return ParamUtil.getInstance(spatialDistribution, "spatialDistribution");
    }

    public void setSpatialDistribution(InSpatialDistribution dist) {
        this.spatialDistribution = new InSpatialDistribution[]{dist};
    }

    @Override
    public JadexConsumerAgentGroup parse(InputParser parser) throws ParsingException {

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
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "added InPVactConsumerAgentGroup '{}' to InRoot", getName());
            agentManager.addConsumerAgentGroup(jcag);
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "added JadexConsumerAgentGroup '{}'", jcag.getName());
        }

        addGroupAttribute(parser, jcag, getNoveltySeeking(), NOVELTY_SEEKING);
        addGroupAttribute(parser, jcag, getDependentJudgmentMaking(), DEPENDENT_JUDGMENT_MAKING);
        addGroupAttribute(parser, jcag, getEnvironmentalConcern(), ENVIRONMENTAL_CONCERN);
        addGroupAttribute(parser, jcag, getFinancialThreshold(), FINANCIAL_THRESHOLD);
        addGroupAttribute(parser, jcag, getAdoptionThreshold(), ADOPTION_THRESHOLD);
        addGroupAttribute(parser, jcag, getCommunication(), COMMUNICATION_FREQUENCY_SN);
        addGroupAttribute(parser, jcag, getRewire(), REWIRING_RATE);
        addGroupAttribute(parser, jcag, getInitialAdopter(), INITIAL_ADOPTER);
        addGroupAttribute(parser, jcag, getRateOfConvergence(), RATE_OF_CONVERGENCE);
        addGroupAttribute(parser, jcag, getInitialProductInterest(), INITIAL_PRODUCT_INTEREST);
        addGroupAttribute(parser, jcag, getInitialProductAwareness(), INITIAL_PRODUCT_AWARENESS);
        addGroupAttribute(parser, jcag, getInterestThreshold(), INTEREST_THRESHOLD);
        addGroupAttribute(parser, jcag, getConstructionRate(), CONSTRUCTION_RATE);
        addGroupAttribute(parser, jcag, getRenovationRate(), RENOVATION_RATE);

        ProductBinaryAwarenessSupplyScheme awarenessSupplyScheme = new ProductBinaryAwarenessSupplyScheme();
        awarenessSupplyScheme.setName(ParamUtil.concName(jcag.getName(), AWARENESS));
        jcag.setAwarenessSupplyScheme(awarenessSupplyScheme);

        ProductThresholdInterestSupplyScheme interestSupplyScheme = new ProductThresholdInterestSupplyScheme();
        interestSupplyScheme.setName(ParamUtil.concName(jcag.getName(), INTEREST));
        jcag.setInterestSupplyScheme(interestSupplyScheme);

        getSpatialDistribution().setup(parser, jcag);

        return jcag;
    }

    private static void addGroupAttribute(
            InputParser parser,
            JadexConsumerAgentGroup jcag,
            InUnivariateDoubleDistribution inDist,
            String name) throws ParsingException {
        if(jcag.hasGroupAttribute(name)) {
            throw new ParsingException("ConsumerAgentGroupAttribute '" + name + "' already exists in " + jcag.getName());
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

        BasicConsumerAgentGroupAttribute cagAttr = new BasicConsumerAgentGroupAttribute();
        cagAttr.setName(name);
        cagAttr.setUnivariateDoubleDistributionValue(dist);
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "add ConsumerAgentGroupAttribute '{}' ('{}') to group '{}'", cagAttr.getName(), cagAttr.getName(), jcag.getName());
        jcag.addGroupAttribute(cagAttr);
    }
}
