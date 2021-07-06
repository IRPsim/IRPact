package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.resource.ResourceLoader;
import de.unileipzig.irpact.commons.util.data.MutableInt;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.BasicAgentManager;
import de.unileipzig.irpact.core.agent.consumer.*;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentProductRelatedGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentDoubleGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentProductRelatedGroupAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.InfoTag;
import de.unileipzig.irpact.core.need.BasicNeed;
import de.unileipzig.irpact.core.network.BasicSocialNetwork;
import de.unileipzig.irpact.core.network.SupportedGraphStructure;
import de.unileipzig.irpact.core.network.topology.GraphTopologyScheme;
import de.unileipzig.irpact.core.process.BasicProcessModelManager;
import de.unileipzig.irpact.core.process.FixProcessModelFindingScheme;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.product.*;
import de.unileipzig.irpact.core.product.interest.ProductInterestSupplyScheme;
import de.unileipzig.irpact.core.simulation.BasicSettings;
import de.unileipzig.irpact.core.simulation.BasicVersion;
import de.unileipzig.irpact.core.simulation.BinaryTaskManager;
import de.unileipzig.irpact.core.spatial.SpatialModel;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InIndependentConsumerAgentGroupAttribute;
import de.unileipzig.irpact.io.param.input.agent.population.InAgentPopulation;
import de.unileipzig.irpact.io.param.input.binary.VisibleBinaryData;
import de.unileipzig.irpact.io.param.input.network.InGraphTopologyScheme;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.param.input.product.InIndependentProductGroupAttribute;
import de.unileipzig.irpact.io.param.input.product.InProductGroup;
import de.unileipzig.irpact.io.param.input.product.InFixProduct;
import de.unileipzig.irpact.io.param.input.spatial.InSpatialModel;
import de.unileipzig.irpact.io.param.input.time.InTimeModel;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.time.JadexTimeModel;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static de.unileipzig.irpact.core.process.ra.RAConstants.*;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("SameParameterValue")
public class JadexInputParser implements IRPactInputParser {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(JadexInputParser.class);

    private final Map<Holder, Object> CACHE = new LinkedHashMap<>();
    private ResourceLoader resourceLoader;

    private final MutableInt simulationYear = MutableInt.empty();
    private BasicJadexSimulationEnvironment environment;
    private InRoot root;

    public JadexInputParser() {
    }

    private void validate() {
        if(resourceLoader == null) {
            throw new NoSuchElementException("no resource loader");
        }
        if(simulationYear.isEmpty()) {
            throw new NoSuchElementException("missing simulation year");
        }
    }

    private void reset() {
        CACHE.clear();

        environment = new BasicJadexSimulationEnvironment();
        environment.setName("Initial_Environment");
        environment.initDefault();
    }

    @Override
    public JadexSimulationEnvironment getEnvironment() {
        return environment;
    }

    @Override
    public boolean isRestored() {
        return environment.isRestored();
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void cache(InIRPactEntity key, Object value) {
        Holder holder = new Holder(key);
        if(!CACHE.containsKey(holder)) {
            CACHE.put(holder, value);
        }
    }

    public void setSimulationYear(int simulationYear) {
        this.simulationYear.set(simulationYear);
    }

    @Override
    public int getSimulationYear() {
        return simulationYear.get();
    }

    @Override
    public boolean isCached(InIRPactEntity key) {
        Holder holder = new Holder(key);
        return CACHE.containsKey(holder);
    }

    @Override
    public Object getCached(InIRPactEntity key) {
        Holder holder = new Holder(key);
        return CACHE.get(holder);
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    @Override
    public Rnd deriveRnd() {
        return environment.getSimulationRandom().deriveInstance();
    }

    @Override
    public InRoot getRoot() {
        return root;
    }

    @Override
    public JadexSimulationEnvironment parseRoot(InRoot root) throws ParsingException {
        validate();
        reset();
        this.root = root;
        doParse(root);
        return environment;
    }

    @Override
    public void initLoggingOnly(InRoot root) {
        reset();
        this.root = root;
        initLogging(root);
        reset();
    }

    @Override
    public Object parseEntity(InIRPactEntity input) throws ParsingException {
        Holder holder = new Holder(input);
        if(CACHE.containsKey(holder)) {
            return CACHE.get(holder);
        } else {
            Object parsed = input.parse(this);
            if(parsed == null) {
                throw new ParsingException("parse result is null for entity '" + input.getName() + "' (" + input.getClass().getName() + ")");
            }
            CACHE.put(holder, parsed);
            return parsed;
        }
    }

    @Override
    public void dispose() {
        CACHE.clear();
        environment = null;
        resourceLoader = null;
    }

    //=========================
    //util
    //=========================

    private static <T> T getInstance(T[] arr, Class<T> c, String errMsg) throws ParsingException {
        if(arr == null || arr.length == 0) {
            throw new ParsingException(errMsg);
        }
        if(arr.length > 1) {
            LOGGER.warn("{} elements for {} found, only the first one is used.", arr.length, c.getSimpleName());
        }
        return arr[0];
    }

    private static int len(Object[] arr) {
        return arr == null
                ? -1
                : arr.length;
    }

    private static void debug(String msg) {
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, msg);
    }

    private static void debug(String format, Object arg) {
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, format, arg);
    }

    private static void debug(String format, Object arg1, Object arg2) {
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, format, arg1, arg2);
    }

    private static void debug(String format, Object... args) {
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, format, args);
    }

    //=========================
    //parsing
    //=========================

    private void doParse(InRoot root) throws ParsingException {
        checkVersion(root);
        setupGeneral(root);

        parseSpatialModel(root);

        parseConsumerAgentGroups(root);
        parseConsumerAgentGroupAttributes(root);
        parseConsumerAgentGroupAffinityMapping(root);
        parseAgentPopulation(root);

        parseProducts(root);
        parseProductGroupAttributes(root);
        parseFixProducts(root);
        parseNetwork(root);
        parseSocialGraph(root);

        parseProcessModel(root);
        parseTimeModel(root);

        setupBinaryTaskManager(root);

        runSpecialOperations(root);
    }

    private void initLogging(InRoot root) {
        root.general.parseLoggingSetup(this);
    }

    private void checkVersion(InRoot root) throws ParsingException {
        InScenarioVersion inVersion = getInstance(root.version, InScenarioVersion.class, "missing Version");
        BasicVersion inputVersion = inVersion.parse(this);
        if(!IRPact.VERSION.supportsInput(inputVersion)) {
            throw new ParsingException("version mismatch! IRPact version: '" + IRPact.VERSION + "', input version: '" + inputVersion + "'");
        }
    }

    private void setupGeneral(InRoot root) throws ParsingException {
        root.general.setup(this);
    }

    private void parseProducts(InRoot root) throws ParsingException {
        int len = len(root.productGroups);
        debug("InProductGroups: {}", len);
        if(len == -1) {
            return;
        }
        BasicProductManager productManager = (BasicProductManager) environment.getProducts();
        for(InProductGroup inPg: root.productGroups) {
            ProductGroup pg = parseEntityTo(inPg);
            productManager.add(pg);
            debug("added ProductGroup '{}'", pg.getName());
        }
    }

    private void parseProductGroupAttributes(InRoot root) throws ParsingException {
        int len = len(root.independentProductGroupAttributes);
        debug("InConsumerAgentGroupAttribute: {}", len);
        if(len == -1) {
            return;
        }

        for(InIndependentProductGroupAttribute inPgAttr: root.getIndependentProductGroupAttributes()) {
            inPgAttr.setup(this, null);
        }
    }

    private void parseFixProducts(InRoot root) throws ParsingException {
        int len = len(root.fixProducts);
        debug("InFixProducts: {}", len);
        if(len == -1) {
            return;
        }

        for(InFixProduct inFix: root.fixProducts) {
            Product fp = parseEntityTo(inFix);
            fp.getGroup().addProduct(fp);
            debug("added product '{}' to group '{}'", fp.getName(), fp.getGroup().getName());
        }
    }

    private void parseConsumerAgentGroups(InRoot root) throws ParsingException {
        int len = len(root.consumerAgentGroups);
        debug("InConsumerAgentGroups: {}", len);
        if(len == -1) {
            return;
        }

        BasicAgentManager agentManager = (BasicAgentManager) environment.getAgents();

        for(InConsumerAgentGroup inCag: root.consumerAgentGroups) {
            ConsumerAgentGroup cag = parseEntityTo(inCag);
            debug("added ConsumerAgentGroup '{}'", cag.getName());
            agentManager.addConsumerAgentGroup(cag);
        }
    }

    private void parseAgentPopulation(InRoot root) throws ParsingException {
        int len = len(root.agentPopulationSizes);
        debug("InPopulationSize: {}", len);
        if(len == -1) {
            return;
        }

        BasicSettings initData = (BasicSettings) environment.getSettings();

        for(InAgentPopulation popSize: root.getAgentPopulationSizes()) {
            popSize.setup(this, initData);
        }
    }

    private void parseConsumerAgentGroupAttributes(InRoot root) throws ParsingException {
        int len = len(root.independentConsumerAgentGroupAttributes);
        debug("InConsumerAgentGroupAttribute: {}", len);
        if(len == -1) {
            return;
        }

        for(InIndependentConsumerAgentGroupAttribute inGrpAttr: root.getIndependentConsumerAgentGroupAttributes()) {
            inGrpAttr.setup(this, null);
        }
    }

    private void parseConsumerAgentGroupAffinityMapping(InRoot root) throws ParsingException {
        BasicConsumerAgentGroupAffinityMapping mapping = root.getAffinities().parse(this);
        environment.getAgents().setConsumerAgentGroupAffinityMapping(mapping);
        LOGGER.trace("set affinity mapping '{}'", mapping.getName());
    }

    private void parseNetwork(InRoot root) throws ParsingException {
        InGraphTopologyScheme topo = getInstance(root.graphTopologySchemes, InGraphTopologyScheme.class, "missing GraphTopologyScheme");
        GraphTopologyScheme scheme = parseEntityTo(topo);
        environment.getNetwork().setGraphTopologyScheme(scheme);
        debug("set graph topology scheme '{}'", scheme.getName());
    }

    private void parseSocialGraph(@SuppressWarnings("unused") InRoot root) {
        debug("[Fixed] use {}", SupportedGraphStructure.CONCURRENT_CACHED_DIRECTED_MULTI_GRAPH);
        BasicSocialNetwork network = (BasicSocialNetwork) environment.getNetwork();
        network.initDefaultGraph();
    }

    private void parseSpatialModel(InRoot root) throws ParsingException {
        InSpatialModel inSm = getInstance(root.spatialModel, InSpatialModel.class, "missing spatial model");
        SpatialModel sm = parseEntityTo(inSm);
        environment.setSpatialModel(sm);
        debug("set spatial model '{}'", sm.getName());
    }

    private void parseProcessModel(InRoot root) throws ParsingException {
        InProcessModel inPm = getInstance(root.processModels, InProcessModel.class, "missing process model");
        ProcessModel pm = parseEntityTo(inPm);
        BasicProcessModelManager processModelManager = (BasicProcessModelManager) environment.getProcessModels();
        processModelManager.addProcessModel(pm);
        debug("added process model '{}'", pm.getName());
    }

    private void parseTimeModel(InRoot root) throws ParsingException {
        InTimeModel inTm = getInstance(root.timeModel, InTimeModel.class, "missing time model");
        JadexTimeModel tm = parseEntityTo(inTm);
        environment.setTimeModel(tm);
        debug("set time model '{}'", tm.getName());
    }

    private void setupBinaryTaskManager(InRoot root) {
        int len = len(root.visibleBinaryData);
        debug("VisibleBinaryDatas: {}", len);
        if(len == -1) {
            return;
        }

        BinaryTaskManager taskManager = environment.getTaskManager();
        for(VisibleBinaryData data: root.visibleBinaryData) {
            taskManager.handle(data.asBinary());
        }
    }

    private void runSpecialOperations(InRoot root) throws ParsingException {
        if(root.general.runPVAct) {
            initPVact();
        }
    }

    //=========================
    //PVact
    //=========================

    private void initPVact() throws ParsingException {
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "{} initalize", InfoTag.PVACT);

        createPVProductGroup();
        addProcessModelFindingScheme();
        addInitialPVNeed();
    }

    private void createPVProductGroup() throws ParsingException {
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "{} create pv product group", InfoTag.PVACT);

        BasicProductManager productManager = (BasicProductManager) environment.getProducts();
        if(productManager.getNumberOfProductGroups() != 0) {
            throw new ParsingException(InfoTag.PVACT + " requires no ProductGroup, current: " + productManager.getNumberOfProductGroups());
        }

        BasicProductGroup pvGroup = new BasicProductGroup();
        pvGroup.setEnvironment(environment);
        pvGroup.setName("PV");
        //Attribute?

        productManager.add(pvGroup);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "{} add ProductGroup '{}'", InfoTag.PVACT, pvGroup.getName());

        createFixPVProductAndFindingScheme(pvGroup);
    }

    private void createFixPVProductAndFindingScheme(ProductGroup pvGroup) throws ParsingException {
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "{} create finding scheme for '{}'", InfoTag.PVACT, pvGroup.getName());

        BasicProduct pvFix = new BasicProduct();
        pvFix.setEnvironment(environment);
        pvFix.setName(pvGroup.getName() + "_" + "fix");
        pvFix.setGroup(pvGroup);
        pvFix.setFixed(true);
        //Attribute?

        pvGroup.addProduct(pvFix);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "{} add fix product '{}' to group '{}'",InfoTag.PVACT, pvFix.getName(), pvGroup.getName());

        FixProductFindingScheme findingScheme = new FixProductFindingScheme();
        findingScheme.setName(pvFix.getName() + "_" + "FindingScheme");
        findingScheme.setProduct(pvFix);

        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            if(cag.getProductFindingScheme() != null) {
                throw new ParsingException(InfoTag.PVACT + " cag '" + cag.getName() + "' already has a ProductFindingScheme");
            }
            ((JadexConsumerAgentGroup) cag).setProductFindingScheme(findingScheme);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "{} set ProductFindingScheme '{}' to '{}'", InfoTag.PVACT, findingScheme.getName(), cag.getName());
        }

        initProductGroupRelatedAttributes(pvGroup);
        initProductInterest(pvGroup);
    }

    private void initProductGroupRelatedAttributes(ProductGroup pvGroup) {
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "{} init product related attributes for cag '{}' and product group '{}'", InfoTag.PVACT, cag.getName(), pvGroup.getName());

            JadexConsumerAgentGroup jcag = (JadexConsumerAgentGroup) cag;
            mapToProductRelatedAttribute(jcag, pvGroup, INITIAL_PRODUCT_AWARENESS);
            mapToProductRelatedAttribute(jcag, pvGroup, INITIAL_PRODUCT_INTEREST);
            mapToProductRelatedAttribute(jcag, pvGroup, INTEREST_THRESHOLD);
            mapToProductRelatedAttribute(jcag, pvGroup, FINANCIAL_THRESHOLD);
            mapToProductRelatedAttribute(jcag, pvGroup, ADOPTION_THRESHOLD);
            mapToProductRelatedAttribute(jcag, pvGroup, INITIAL_ADOPTER);
        }
    }

    private void mapToProductRelatedAttribute(
            JadexConsumerAgentGroup jcag,
            ProductGroup productGroup,
            String groupAttributeName) {
        ConsumerAgentGroupAttribute groupAttribute = jcag.getGroupAttribute(groupAttributeName);
        if(groupAttribute == null) {
            throw ExceptionUtil.create(NoSuchElementException::new, "cag '{}' has no '{}'", jcag.getName(), groupAttributeName);
        }
        mapToProductRelatedAttribute(jcag, productGroup, groupAttribute);
    }

    private void mapToProductRelatedAttribute(
            JadexConsumerAgentGroup jcag,
            ProductGroup productGroup,
            ConsumerAgentGroupAttribute groupAttribute) {
        jcag.removeGroupAttribute(groupAttribute);
        if(!jcag.hasProductRelatedGroupAttribute(groupAttribute.getName())) {
            BasicConsumerAgentProductRelatedGroupAttribute relatedGroupAttribute = new BasicConsumerAgentProductRelatedGroupAttribute();
            relatedGroupAttribute.setName(groupAttribute.getName());
            relatedGroupAttribute.setArtificial(false);
            jcag.addProductRelatedGroupAttribute(relatedGroupAttribute);
        }
        BasicConsumerAgentProductRelatedGroupAttribute relatedAttribute = (BasicConsumerAgentProductRelatedGroupAttribute) jcag.getProductRelatedGroupAttribute(groupAttribute.getName());
        relatedAttribute.put(productGroup, groupAttribute);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "{} add attribute '{}' in cag '{}' for product group '{}'", InfoTag.PVACT, groupAttribute.getName(), jcag.getName(), productGroup.getName());
    }

    private void initProductInterest(ProductGroup pvGroup) {
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "{} init product interest for cag '{}' and product group '{}'", InfoTag.PVACT, cag.getName(), pvGroup.getName());

            ConsumerAgentProductRelatedGroupAttribute pgrAttr = cag.getProductRelatedGroupAttribute(INTEREST_THRESHOLD);
            ConsumerAgentGroupAttribute cagAttr = pgrAttr.getAttribute(pvGroup);
            ConsumerAgentDoubleGroupAttribute cagAttrD = cagAttr.asDoubleGroupAttribute();

            ProductInterestSupplyScheme scheme = cag.getInterestSupplyScheme();
            scheme.setThresholdDistribution(pvGroup, cagAttrD.getDistribution());
        }
    }

    private void addProcessModelFindingScheme() throws ParsingException {
        Collection<ProcessModel> processModels = environment.getProcessModels().getProcessModels();
        if(processModels.size() != 1) {
            throw ExceptionUtil.create(ParsingException::new, "{} requires exactly 1 ProcessModel (current: {})", InfoTag.PVACT, processModels.size());
        }

        ProcessModel processModel = CollectionUtil.get(processModels, 0);
        FixProcessModelFindingScheme findingScheme = new FixProcessModelFindingScheme();
        findingScheme.setName("PVact_ProcessFindingScheme");
        findingScheme.setModel(processModel);

        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            if(cag.getProcessFindingScheme() != null) {
                throw new ParsingException(InfoTag.PVACT + " cag '" + cag.getName() + "' already has a ProcessFindingScheme");
            }
            ((JadexConsumerAgentGroup) cag).setProcessFindingScheme(findingScheme);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "{} set ProcessFindingScheme '{}' to '{}'", InfoTag.PVACT, findingScheme.getName(), cag.getName());
        }
    }

    private void addInitialPVNeed() {
        BasicNeed pvNeed = new BasicNeed("PV");
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "{} create initial pv need: '{}'", InfoTag.PVACT, pvNeed.getName());
        AgentManager agentManager = environment.getAgents();
        for(ConsumerAgentGroup cag: agentManager.getConsumerAgentGroups()) {
            JadexConsumerAgentGroup jcag = (JadexConsumerAgentGroup) cag;
            jcag.addInitialNeed(pvNeed);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "{} add initial pv need '{}' to cag '{}'", InfoTag.PVACT, pvNeed.getName(), jcag.getName());
        }
    }

    //=========================
    //holder
    //=========================

    /**
     * @author Daniel Abitz
     */
    private static final class Holder {

        private final InIRPactEntity entity;

        private Holder(InIRPactEntity entity) {
            this.entity = entity;
        }

        public InIRPactEntity getEntity() {
            return entity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Holder)) return false;
            Holder holder = (Holder) o;
            return entity == holder.entity;
        }

        @Override
        public int hashCode() {
            return System.identityHashCode(entity);
        }
    }
}
