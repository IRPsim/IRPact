package de.unileipzig.irpact.io.param.input.agent.consumer;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.product.interest.ProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irpact.util.AddToRoot;
import de.unileipzig.irpact.util.Todo;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.TreeResourceApplier;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.core.process.ra.RAConstants.*;

/**
 * @author Daniel Abitz
 */
@Todo("einbauen + schauen, ob alles da ist")
@AddToRoot
@Definition
public class InPVactConsumerAgentGroup implements InConsumerAgentGroup {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
        TreeResourceApplier.callAllSubInitResSilently(thisClass(), res);
    }
    public static void applyRes(TreeAnnotationResource res) {
        TreeResourceApplier.callAllSubApplyResSilently(thisClass(), res);
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    public static void initRes0(TreeAnnotationResource res) {
    }
    public static void applyRes0(TreeAnnotationResource res) {
    }
    @FieldDefinition
    public InUnivariateDoubleDistribution[] noveltySeeking;

    public static void initRes1(TreeAnnotationResource res) {
    }
    public static void applyRes1(TreeAnnotationResource res) {
    }
    @FieldDefinition
    public InUnivariateDoubleDistribution[] independentJudgmentMaking;

    public static void initRes2(TreeAnnotationResource res) {
    }
    public static void applyRes2(TreeAnnotationResource res) {
    }
    @FieldDefinition
    public InUnivariateDoubleDistribution[] environmentalConcern;

    public static void initRes3(TreeAnnotationResource res) {
    }
    public static void applyRes3(TreeAnnotationResource res) {
    }
    @FieldDefinition
    public InUnivariateDoubleDistribution[] interestThreshold;

    public static void initRes4(TreeAnnotationResource res) {
    }
    public static void applyRes4(TreeAnnotationResource res) {
    }
    @FieldDefinition
    public InUnivariateDoubleDistribution[] financialThreshold;

    public static void initRes5(TreeAnnotationResource res) {
    }
    public static void applyRes5(TreeAnnotationResource res) {
    }
    @FieldDefinition
    public InUnivariateDoubleDistribution[] adoptionThreshold;

    public static void initRes6(TreeAnnotationResource res) {
    }
    public static void applyRes6(TreeAnnotationResource res) {
    }
    @FieldDefinition
    public InUnivariateDoubleDistribution[] communication;

    public static void initRes7(TreeAnnotationResource res) {
    }
    public static void applyRes7(TreeAnnotationResource res) {
    }
    @FieldDefinition
    public InUnivariateDoubleDistribution[] rewire;

    public static void initRes8(TreeAnnotationResource res) {
    }
    public static void applyRes8(TreeAnnotationResource res) {
    }
    @FieldDefinition
    public InUnivariateDoubleDistribution[] initialAdopter;

    public static void initRes9(TreeAnnotationResource res) {
    }
    public static void applyRes9(TreeAnnotationResource res) {
    }
    @FieldDefinition
    public InSpatialDistribution[] spatialDistribution;

    public static void initRes10(TreeAnnotationResource res) {
    }
    public static void applyRes10(TreeAnnotationResource res) {
    }
    @FieldDefinition
    public double informationAuthority;

    public static void initRes11(TreeAnnotationResource res) {
    }
    public static void applyRes12(TreeAnnotationResource res) {
    }
    @FieldDefinition
    public int numberOfPVactConsumerAgents;

    public InPVactConsumerAgentGroup() {
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public double getInformationAuthority() {
        return informationAuthority;
    }

    public void setInformationAuthority(double informationAuthority) {
        this.informationAuthority = informationAuthority;
    }

    @Override
    public int getNumberOfAgents() {
        return numberOfPVactConsumerAgents;
    }

    public void setNumberOfAgents(int numberOfAgents) {
        this.numberOfPVactConsumerAgents = numberOfAgents;
    }

    public InUnivariateDoubleDistribution getNoveltySeeking() throws ParsingException {
        return ParamUtil.getInstance(noveltySeeking, "noveltySeeking");
    }

    public void setNoveltySeeking(InUnivariateDoubleDistribution noveltySeeking) {
        this.noveltySeeking = new InUnivariateDoubleDistribution[]{noveltySeeking};
    }

    public InUnivariateDoubleDistribution getIndependentJudgmentMaking() throws ParsingException {
        return ParamUtil.getInstance(independentJudgmentMaking, "independentJudgmentMaking");
    }

    public void setIndependentJudgmentMaking(InUnivariateDoubleDistribution independentJudgmentMaking) {
        this.independentJudgmentMaking = new InUnivariateDoubleDistribution[]{independentJudgmentMaking};
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
        jcag.setInformationAuthority(getInformationAuthority());

        if(agentManager.hasConsumerAgentGroup(getName())) {
            throw new ParsingException("ConsumerAgentGroup '" + getName() + "' already exists");
        }

        addGroupAttribute(parser, jcag, getNoveltySeeking(), NOVELTY_SEEKING);
        addGroupAttribute(parser, jcag, getIndependentJudgmentMaking(), DEPENDENT_JUDGMENT_MAKING);
        addGroupAttribute(parser, jcag, getEnvironmentalConcern(), ENVIRONMENTAL_CONCERN);
        addGroupAttribute(parser, jcag, getFinancialThreshold(), FINANCIAL_THRESHOLD);
        addGroupAttribute(parser, jcag, getAdoptionThreshold(), ADOPTION_THRESHOLD);
        addGroupAttribute(parser, jcag, getCommunication(), COMMUNICATION_FREQUENCY_SN);
        addGroupAttribute(parser, jcag, getRewire(), REWIRING_RATE);
        addGroupAttribute(parser, jcag, getInitialAdopter(), INITIAL_ADOPTER);

        UnivariateDoubleDistribution interestDist = parser.parseEntityTo(getInterestThreshold());
        ProductThresholdInterestSupplyScheme interestSupplyScheme = new ProductThresholdInterestSupplyScheme();
        interestSupplyScheme.setName(ParamUtil.concName(jcag.getName(), INTEREST_THRESHOLD));
        interestSupplyScheme.setDistribution(interestDist);
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
        UnivariateDoubleDistribution dist = (UnivariateDoubleDistribution) inDist.parse(parser);

        BasicConsumerAgentGroupAttribute cagAttr = new BasicConsumerAgentGroupAttribute();
        cagAttr.setName(name);
        cagAttr.setDistribution(dist);
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "add ConsumerAgentGroupAttribute '{}' ('{}') to group '{}'", cagAttr.getName(), cagAttr.getName(), jcag.getName());
        jcag.addGroupAttribute(cagAttr);
    }
}
