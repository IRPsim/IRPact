package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.attribute.AttributeUtil;
import de.unileipzig.irpact.commons.awareness.Awareness;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.ProcessPlanResult;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductAttribute;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.*;

/**
 * @author Daniel Abitz
 */
/*
 * http://jasss.soc.surrey.ac.uk/5/4/1.html
 */
public class RAProcessPlan implements ProcessPlan {

    protected SimulationEnvironment environment;
    protected Need need;
    protected Product product;
    protected ConsumerAgent agent;
    protected Rnd rnd;
    protected RAProcessModel model;

    protected RAPhase currentPhase = RAPhase.NONE;

    public RAProcessPlan() {
    }

    public RAProcessPlan(SimulationEnvironment environment, RAProcessModel model, Rnd rnd, ConsumerAgent agent, Need need, Product product) {
        setEnvironment(environment);
        setModel(model);
        setRnd(rnd);
        setAgent(agent);
        setNeed(need);
        setProduct(product);
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    public void setModel(RAProcessModel model) {
        this.model = model;
    }

    public RAProcessModel getModel() {
        return model;
    }

    public void setRnd(Rnd rnd) {
        this.rnd = rnd;
    }

    public Rnd getRnd() {
        return rnd;
    }

    public void setAgent(ConsumerAgent agent) {
        this.agent = agent;
    }

    public ConsumerAgent getAgent() {
        return agent;
    }

    public void setNeed(Need need) {
        this.need = need;
    }

    public Need getNeed() {
        return need;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setCurrentPhase(RAPhase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public RAPhase getCurrentPhase() {
        return currentPhase;
    }

    @Override
    public ProcessPlanResult execute() {
        if(currentPhase == RAPhase.NONE) {
            return initPlan();
        } else {
            return executePlan();
        }
    }

    @Override
    public void onAdopted() {
    }

    //=========================
    //phases
    //=========================

    protected ProcessPlanResult initPlan() {
        throw new IllegalStateException("EMILY IDEEN EINFÜGEN,guck dazu auf dem usb-stick");
    }

    protected ProcessPlanResult executePlan() {
        if(isNewYear()) {
            adjustParametersOnNewYear();
        }
        switch (currentPhase) {
            case AWARENESS:
                return handleAwareness();

            case FEASIBILITY:
                return handleFeasibility();

            case DECISION_MAKING:
                return handleDecisionMaking();

            case ADOPTED:
                return ProcessPlanResult.ADOPTED;

            case IMPEDED:
                return ProcessPlanResult.IMPEDED;

            default:
                throw new IllegalStateException("unknown phase: " + currentPhase);
        }
    }

    protected void adjustParametersOnNewYear() {
        if(currentPhase == RAPhase.IMPEDED) {
            currentPhase = RAPhase.DECISION_MAKING;
        }
    }

    protected ProcessPlanResult handleAwareness() {
        Awareness<Product> productAwareness = agent.getProductAwareness();
        if(productAwareness.isInterested(product)) {
            currentPhase = RAPhase.FEASIBILITY;
            return handleFeasibility();
        }
        if(isAware(agent)) {
            if(isUnderConstruction(agent) || isUnderRenovation(agent)) {
                productAwareness.makeInterested(product);
                return ProcessPlanResult.IN_PROCESS;
            }
            return tryCommunication();
        }
        return ProcessPlanResult.IN_PROCESS;
    }

    protected ProcessPlanResult handleAwareness(boolean init) {
        Awareness<Product> productAwareness = agent.getProductAwareness();
        if(productAwareness.isInterested(product)) {
            currentPhase = RAPhase.FEASIBILITY;
            if(init) {
                return handleFeasibility();
            } else {
                return ProcessPlanResult.IN_PROCESS;
            }
        }
        if(isAware(agent)) {
            if(isUnderConstruction(agent) || isUnderRenovation(agent)) {
                productAwareness.makeInterested(product);
                return ProcessPlanResult.IN_PROCESS;
            }
            return tryCommunication();
        }
        return ProcessPlanResult.IN_PROCESS;
    }

    protected ProcessPlanResult tryCommunication() {
        if(doCommunicate()) {
            return communicate();
        } else {
            return ProcessPlanResult.IN_PROCESS;
        }
    }

    protected boolean doCommunicate() {
        double r = rnd.nextDouble();
        double freq = getCommunicationFrequencySN(agent);
        return r < freq;
    }

    protected ProcessPlanResult communicate() {
        if(agent.aquireAction()) {
            SocialGraph graph = environment.getNetwork().getGraph();
            SocialGraph.Node node = agent.getSocialGraphNode();
            LinkedList<SocialGraph.Node> targetList = new LinkedList<>();
            graph.getTargets(node, SocialGraph.Type.COMMUNICATION, targetList);
            Collections.shuffle(targetList, rnd.getRandom());
            while(targetList.size() > 0) {
                SocialGraph.Node targetNode = targetList.removeFirst();
                ConsumerAgent targetAgent = targetNode.getAgent(ConsumerAgent.class);
                if(targetAgent.aquireAction()) {
                    int myPoints = getInterestPoints(agent);
                    int targetPoints = getInterestPoints(targetAgent);
                    updateAwareness(agent, targetPoints);
                    updateAwareness(targetAgent, myPoints);
                    updateCommunicationGraph(graph, targetNode);
                    applyRelativeAgreement(targetAgent);
                    return ProcessPlanResult.IN_PROCESS;
                }
            }
        }
        return ProcessPlanResult.IN_PROCESS;
    }

    protected void updateCommunicationGraph(SocialGraph graph, SocialGraph.Node target) {
        if(!graph.hasEdge(target, agent.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
            graph.addEdge(target, agent.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION, 1.0);
        }
    }

    protected ProcessPlanResult handleFeasibility() {
        if(isShareOf1Or2FamilyHouse(agent) || isHouseOwner(agent) || isUnderConstruction(agent)) {
            currentPhase = RAPhase.DECISION_MAKING;
            return handleDecisionMaking();
        }
        return ProcessPlanResult.IN_PROCESS;
    }

    protected ProcessPlanResult handleFeasibility(boolean init) {
        if(isShareOf1Or2FamilyHouse(agent) || isHouseOwner(agent) || isUnderConstruction(agent)) {
            currentPhase = RAPhase.DECISION_MAKING;
            if(init) {
                return handleDecisionMaking();
            } else {
                return ProcessPlanResult.IN_PROCESS;
            }
        }
        return ProcessPlanResult.IN_PROCESS;
    }

    protected ProcessPlanResult handleDecisionMaking() {
        double a = modelData().a();
        double b = modelData().b();
        double c = modelData().c();
        double d = modelData().d();

        double B = 0.0;
        if(a != 0.0) {
            double financial = getFinancialComponent();
            double financialThreshold = getFinancialThresholdProduct(product);
            //check D3 reached
            if(financial < financialThreshold) {
                currentPhase = RAPhase.IMPEDED;
                return ProcessPlanResult.IMPEDED;
            }
            B += a * financial;
        }
        if(b != 0.0) {
            B += b * getNoveltyCompoenent();
        }
        if(c != 0.0) {
            B += c * getEnvironmentalComponent();
        }
        if(d != 0.0) {
            B += d * getSocialComponent();
        }
        double adoptionThreshold = getAdoptionThreshold(product);
        if(B < adoptionThreshold) {
            currentPhase = RAPhase.IMPEDED;
            return ProcessPlanResult.IMPEDED;
        } else {
            agent.adopt(need, product);
            currentPhase = RAPhase.ADOPTED;
            return ProcessPlanResult.ADOPTED;
        }
    }

    //=========================
    //util
    //=========================

    protected static double getDouble(ConsumerAgent agent, String attrName) {
        Attribute<?> attr = agent.findAttribute(attrName);
        return getDouble(attr, attrName);
    }

    protected static double getDouble(Attribute<?> attr, String attrName) {
        if(attr == null) {
            throw new NoSuchElementException("missing attribute: '" + attrName + "'");
        }
        return AttributeUtil.getDoubleValue(attr, () -> "attribute '" + attrName + "' has no number");
    }

    protected RAModelData modelData() {
        return model.getModelData();
    }

    protected double getFinancialComponent() {
        double ftThis = getFinancialThresholdAgent(agent);
        double ftAvg = getAverageFinancialThresholdAgent();

        double npvThis = modelData().NPV(agent, environment.getTimeModel().getYear());
        double npvAvg = modelData().avgNPV(environment.getTimeModel().getYear());

        return Math.log(ftThis - ftAvg) / Math.log(npvThis - npvAvg);
    }

    protected double getNoveltyCompoenent() {
        return getNoveltySeeking(agent);
    }

    protected double getEnvironmentalComponent() {
        return getEnvironmentalConcern(agent);
    }

    protected double getSocialComponent() {
        return getDependentJudgmentMaking(agent) * getShareOfAdopterInSocialNetwork();
    }

    protected boolean isNewYear() {
        return false;
    }

    protected static double getFinancialThresholdAgent(ConsumerAgent agent) {
        double pp = getPurchasePower(agent);
        double ns = getNoveltySeeking(agent);
        return pp * ns;
    }

    protected double getAverageFinancialThresholdAgent() {
        double sum = environment.getAgents()
                .streamConsumerAgents()
                .mapToDouble(RAProcessPlan::getFinancialThresholdAgent)
                .sum();
        return sum / environment.getAgents().getTotalNumberOfConsumerAgents();
    }

    protected static double getFinancialThresholdProduct(Product product) {
        ProductAttribute attr = product.getAttribute(RAConstants.FINANCIAL_THRESHOLD);
        return attr.getDoubleValue();
    }

    protected static double getAdoptionThreshold(Product product) {
        ProductAttribute attr = product.getAttribute(RAConstants.ADOPTION_THRESHOLD);
        return attr.getDoubleValue();
    }

    protected static double getCommunicationFrequencySN(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.COMMUNICATION_FREQUENCY_SN);
        return attr.getDoubleValue();
    }

    protected static double getPurchasePower(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.PURCHASE_POWER);
        return attr.getDoubleValue();
    }

    protected static double getNoveltySeeking(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.NOVELTY_SEEKING);
        return attr.getDoubleValue();
    }

    protected static double getDependentJudgmentMaking(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.DEPENDENT_JUDGMENT_MAKING);
        return attr.getDoubleValue();
    }

    protected static double getEnvironmentalConcern(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.ENVIRONMENTAL_CONCERN);
        return attr.getDoubleValue();
    }

    protected static boolean isShareOf1Or2FamilyHouse(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.SHARE_1_2_HOUSE);
        return attr.getDoubleValue() == 1.0;
    }

    protected static boolean isHouseOwner(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.HOUSE_OWNER);
        return attr.getDoubleValue() == 1.0;
    }

    protected static double getConstructionRate(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.CONSTRUCTION_RATE);
        return attr.getDoubleValue();
    }

    protected static boolean isUnderConstruction(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.UNDER_CONSTRUCTION);
        return attr != null && attr.getDoubleValue() == 1.0;
    }

    protected static double getRenovationRate(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.RENOVATION_RATE);
        return attr.getDoubleValue();
    }

    protected static boolean isUnderRenovation(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.UNDER_RENOVATION);
        return attr != null && attr.getDoubleValue() == 1.0;
    }

    protected boolean isAdopter(ConsumerAgent agent) {
        return agent.hasAdopted(product);
    }

    protected boolean isInterested(ConsumerAgent agent) {
        Awareness<Product> awareness = agent.getProductAwareness();
        return awareness.isInterested(product);
    }

    protected boolean isAware(ConsumerAgent agent) {
        Awareness<Product> awareness = agent.getProductAwareness();
        return awareness.isAware(product);
    }

    protected int getInterestPoints(ConsumerAgent agent) {
        if(isAdopter(agent)) {
            return modelData().getAdopterPoints();
        }
        if(isInterested(agent)) {
            return modelData().getInterestedPoints();
        }
        if(isAware(agent)) {
            return modelData().getAwarePoints();
        }
        return modelData().getUnknownPoints();
    }

    protected void updateAwareness(ConsumerAgent agent, double points) {
        Awareness<Product> awareness = agent.getProductAwareness();
        awareness.update(product, points);
    }

    protected double getShareOfAdopterInSocialNetwork() {
        long adopterCount = environment.getAgents().streamConsumerAgents()
                .filter(ca -> ca.hasAdopted(product))
                .count();
        long total = environment.getAgents().getTotalNumberOfConsumerAgents();
        return (double) adopterCount / total;
    }

    protected void applyRelativeAgreement(ConsumerAgent target) {
        //A2
        applyRelativeAgreement(target, RAConstants.NOVELTY_SEEKING);
        //A3
        applyRelativeAgreement(target, RAConstants.DEPENDENT_JUDGMENT_MAKING);
        //A4
        applyRelativeAgreement(target, RAConstants.ENVIRONMENTAL_CONCERN);
    }

    protected void applyRelativeAgreement(ConsumerAgent target, String attrName) {
        ConsumerAgentAttribute o1Attr = agent.getAttribute(attrName);
        UncertaintyAttribute u1Attr = (UncertaintyAttribute) agent.getAttribute(RAConstants.getUncertaintyAttributeName(attrName));
        ConsumerAgentAttribute o2Attr = target.getAttribute(attrName);
        UncertaintyAttribute u2Attr = (UncertaintyAttribute) target.getAttribute(RAConstants.getUncertaintyAttributeName(attrName));
        applyRelativeAgreement(o1Attr, u1Attr, o2Attr, u2Attr);
    }

    protected static void applyRelativeAgreement(
            ConsumerAgentAttribute o1Attr,
            UncertaintyAttribute u1Attr,
            ConsumerAgentAttribute o2Attr,
            UncertaintyAttribute u2Attr) {
        //anmerkung: werte extrahieren um keine ueberschreibungen zu erhalten
        double o1 = o1Attr.getDoubleValue();
        double u1 = u1Attr.getUncertainty();
        double m1 = u1Attr.getConvergence();
        double o2 = o2Attr.getDoubleValue();
        double u2 = u2Attr.getUncertainty();
        double m2 = u2Attr.getConvergence();
        //1 beeinflusst 2
        applyRelativeAgreement(o1, u1, o2, u2, o2Attr, u2Attr, m2);
        //2 beeinflusst 1
        applyRelativeAgreement(o2, u2, o1, u1, o1Attr, u1Attr, m1);
    }

    /**
     * Calculates the realtive agreement betwenn agent i and j, where i influences j and updates j.
     *
     * @param oi opinion of i
     * @param ui uncertainty of i
     * @param oj opinion of j
     * @param uj uncertainty of j
     * @param ojAttr corresponding parameter to store the opinion of j
     * @param ujAttr corresponding parameter to store the uncertainty of j
     * @param m convergence parameter
     */
    protected static void applyRelativeAgreement(
            double oi,
            double ui,
            double oj,
            double uj,
            ConsumerAgentAttribute ojAttr,
            ConsumerAgentAttribute ujAttr,
            double m) {
        double hij = Math.min(oi + ui, oj + uj) - Math.max(oi - ui, oj -uj);
        if(hij > ui) {
            double ra = hij / ui - 1.0;
            double newOj = oj + m * ra * (oi - oj);
            double newUj = uj + m * ra * (ui - uj);
            ojAttr.setDoubleValue(newOj);
            ujAttr.setDoubleValue(newUj);
        }
    }
}
