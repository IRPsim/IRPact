package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.graph.DirectedAdjacencyListMultiGraph;
import de.unileipzig.irpact.commons.graph.DirectedMultiGraph;
import de.unileipzig.irpact.commons.res.ResourceLoader;
import de.unileipzig.irpact.core.agent.BasicAgentManager;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.network.BasicSocialGraph;
import de.unileipzig.irpact.core.network.BasicSocialNetwork;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.topology.GraphTopologyScheme;
import de.unileipzig.irpact.core.process.BasicProcessModelManager;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.product.BasicProductManager;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.simulation.BasicInitializationData;
import de.unileipzig.irpact.core.simulation.BasicVersion;
import de.unileipzig.irpact.core.simulation.BinaryTaskManager;
import de.unileipzig.irpact.core.spatial.SpatialModel;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.binary.VisibleBinaryData;
import de.unileipzig.irpact.io.param.input.network.InGraphTopologyScheme;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.param.input.process.InUncertaintyGroupAttribute;
import de.unileipzig.irpact.io.param.input.product.InFixProduct;
import de.unileipzig.irpact.io.param.input.product.InProductGroup;
import de.unileipzig.irpact.io.param.input.spatial.InSpatialModel;
import de.unileipzig.irpact.io.param.input.time.InTimeModel;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.time.JadexTimeModel;
import de.unileipzig.irpact.start.IRPact;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("SameParameterValue")
public class JadexInputParser implements InputParser {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(JadexInputParser.class);

    private final Map<Holder, Object> CACHE = new HashMap<>();
    private ResourceLoader resourceLoader;

    private BasicJadexSimulationEnvironment environment;
    private InRoot root;

    public JadexInputParser() {
    }

    private void reset() {
        if(resourceLoader == null) {
            throw new NullPointerException("no resource loader");
        }

        CACHE.clear();

        environment = new BasicJadexSimulationEnvironment();
        environment.initDefault();
    }

    @Override
    public JadexSimulationEnvironment getEnvironment() {
        return environment;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void cache(InEntity key, Object value) {
        Holder holder = new Holder(key);
        if(!CACHE.containsKey(holder)) {
            CACHE.put(holder, value);
        }
    }

    @Override
    public boolean isCached(InEntity key) {
        Holder holder = new Holder(key);
        return CACHE.containsKey(holder);
    }

    @Override
    public Object getCached(InEntity key) {
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

    @SuppressWarnings("unchecked")
    @Override
    public <T> T parseRoot(InRoot root) throws ParsingException {
        reset();
        this.root = root;
        doParse(root);
        return (T) environment;
    }

    @Override
    public Object parseEntity(InEntity input) throws ParsingException {
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
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, msg);
    }

    private static void debug(String format, Object arg) {
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, format, arg);
    }

    private static void debug(String format, Object arg1, Object arg2) {
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, format, arg1, arg2);
    }

    private static void debug(String format, Object... args) {
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, format, args);
    }

    //=========================
    //parsing
    //=========================

    private void doParse(InRoot root) throws ParsingException {
        checkVersion(root);
        setupGeneral(root);

        parseProducts(root);
        parseFixProducts(root);

        parseConsumerAgentGroups(root);
        parseConsumerAgentGroupAffinityMapping(root);
        parseNetwork(root);
        parseSocialGraph(root);

        parseSpatialModel(root);
        parseProcessModel(root);
        parseTimeModel(root);

        setupBinaryTaskManager(root);
    }

    private void checkVersion(InRoot root) throws ParsingException {
        InVersion inVersion = getInstance(root.version, InVersion.class, "missing Version");
        BasicVersion inputVersion = inVersion.parse(this);
        if(IRPact.VERSION.isMismatch(inputVersion)) {
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
        BasicInitializationData initData = (BasicInitializationData) environment.getInitializationData();

        for(InConsumerAgentGroup inCag: root.consumerAgentGroups) {
            ConsumerAgentGroup cag = parseEntityTo(inCag);
            debug("added ConsumerAgentGroup '{}' with '{}' agents", cag.getName(), inCag.getNumberOfAgents());
            agentManager.addConsumerAgentGroup(cag);
            initData.setInitialNumberOfConsumerAgents(cag, inCag.getNumberOfAgents());
        }
    }

    private void parseConsumerAgentGroupAffinityMapping(InRoot root) throws ParsingException {
        BasicConsumerAgentGroupAffinityMapping mapping = new BasicConsumerAgentGroupAffinityMapping();
        for(InComplexAffinityEntry entry: root.affinityEntries) {
            ConsumerAgentGroup srcCag = parseEntityTo(entry.getSrcCag());
            ConsumerAgentGroup tarCag = parseEntityTo(entry.getTarCag());
            double value = entry.getAffinityValue();
            mapping.put(srcCag, tarCag, value);
            debug("added affinity '{}' -> '{}' with value '{}'", srcCag.getName(), tarCag.getName(), value);
        }
        environment.getAgents().setConsumerAgentGroupAffinityMapping(mapping);
    }

    private void parseNetwork(InRoot root) throws ParsingException {
        InGraphTopologyScheme topo = getInstance(root.graphTopologySchemes, InGraphTopologyScheme.class, "missing GraphTopologyScheme");
        GraphTopologyScheme scheme = parseEntityTo(topo);
        environment.getNetwork().setGraphTopologyScheme(scheme);
        debug("set graph topology scheme '{}'", scheme.getName());
    }

    private void parseSocialGraph(@SuppressWarnings("unused") InRoot root) {
        debug("[Fixed] use DirectedAdjacencyListMultiGraph");
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
        InProcessModel inPm = getInstance(root.processModel, InProcessModel.class, "missing process model");
        ProcessModel pm = parseEntityTo(inPm);
        BasicProcessModelManager processModelManager = (BasicProcessModelManager) environment.getProcessModels();
        processModelManager.addProcessModel(pm);
        debug("added process model '{}'", pm.getName());

        setupUncertaintyGroupAttributes(root, pm);
    }

    private void setupUncertaintyGroupAttributes(InRoot root, ProcessModel pm) throws ParsingException {
        int len = len(root.uncertaintyGroupAttributes);
        debug("InUncertaintyGroupAttributes: {}", len);
        if(len == -1) {
            return;
        }

        for(InUncertaintyGroupAttribute attr: root.uncertaintyGroupAttributes) {
            attr.setup(this, pm);
        }
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

    //=========================
    //holder
    //=========================

    /**
     * @author Daniel Abitz
     */
    private static final class Holder {

        private final InEntity entity;

        private Holder(InEntity entity) {
            this.entity = entity;
        }

        public InEntity getEntity() {
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
