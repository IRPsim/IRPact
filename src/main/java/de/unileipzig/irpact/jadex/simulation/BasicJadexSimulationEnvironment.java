package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.exception.InitializationException;
import de.unileipzig.irpact.commons.exception.VersionMismatchException;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.res.ResourceLoader;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.BasicAgentManager;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.network.BasicSocialNetwork;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.SocialNetwork;
import de.unileipzig.irpact.core.persistence.PersistenceModul;
import de.unileipzig.irpact.core.process.BasicProcessModelManager;
import de.unileipzig.irpact.core.process.ProcessModelManager;
import de.unileipzig.irpact.core.product.BasicProductManager;
import de.unileipzig.irpact.core.product.ProductManager;
import de.unileipzig.irpact.core.simulation.*;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialModel;
import de.unileipzig.irpact.jadex.persistance.JadexPersistenceModul;
import de.unileipzig.irpact.jadex.time.JadexTimeModel;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("DefaultAnnotationParam")
@Reference(local = true, remote = true)
public class BasicJadexSimulationEnvironment extends NameableBase implements JadexSimulationEnvironment {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicJadexSimulationEnvironment.class);

    protected Settings settings;
    protected AgentManager agentManager;
    protected SocialNetwork socialNetwork;
    protected ProcessModelManager processModelManager;
    protected ProductManager productManager;
    protected SpatialModel spatialModel;
    protected JadexTimeModel timeModel;
    protected JadexLifeCycleControl lifeCycleControl;
    protected ResourceLoader resourceLoader;
    protected BinaryTaskManager taskManager;
    protected PersistenceModul persistenceModul;
    protected Rnd rnd;

    public BasicJadexSimulationEnvironment() {
    }

    @Override
    public int getChecksum() {
//        return Objects.hash(
//                agentManager.getChecksum(),
//                socialNetwork.getChecksum(),
//                processModelManager.getChecksum(),
//                productManager.getChecksum(),
//                spatialModel.getChecksum(),
//                timeModel.getChecksum(),
//                lifeCycleControl.getChecksum(),
//                rnd.getChecksum()
//        );
        return ChecksumComparable.getChecksum(
                agentManager,
                socialNetwork,
                processModelManager,
                productManager,
                spatialModel,
                timeModel,
                lifeCycleControl,
                rnd
        );
    }

    @Override
    public void logChecksum() {
        ChecksumComparable.logChecksums(
                agentManager,
                socialNetwork,
                processModelManager,
                productManager,
                spatialModel,
                timeModel,
                lifeCycleControl,
                rnd
        );
    }

    public void initDefault() {
        BasicSettings initData = new BasicSettings();
        BasicAgentManager agentManager = new BasicAgentManager();
        BasicSocialNetwork socialNetwork = new BasicSocialNetwork();
        BasicProcessModelManager processModelManager = new BasicProcessModelManager();
        BasicProductManager productManager = new BasicProductManager();
        BasicJadexLifeCycleControl lifeCycleControl = new BasicJadexLifeCycleControl();
        BasicBinaryTaskManager taskManager = new BasicBinaryTaskManager();
        JadexPersistenceModul persistenceModul = new JadexPersistenceModul();

        setSettings(initData);
        setResourceLoader(resourceLoader);

        setAgentManager(agentManager);
        agentManager.setEnvironment(this);

        setSocialNetwork(socialNetwork);
        socialNetwork.setEnvironment(this);

        setProductManager(productManager);
        productManager.setEnvironment(this);

        setProcessModels(processModelManager);
        processModelManager.setEnvironment(this);

        setLifeCycleControl(lifeCycleControl);
        lifeCycleControl.setEnvironment(this);

        setTaskManager(taskManager);
        taskManager.setEnvironment(this);

        setPersistenceModul(persistenceModul);
        persistenceModul.setEnvironment(this);
    }

    @Override
    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void setAgentManager(AgentManager agentManager) {
        this.agentManager = agentManager;
    }

    @Override
    public AgentManager getAgents() {
        return agentManager;
    }

    public void setSocialNetwork(SocialNetwork socialNetwork) {
        this.socialNetwork = socialNetwork;
    }

    @Override
    public SocialNetwork getNetwork() {
        return socialNetwork;
    }

    public void setProcessModels(ProcessModelManager processModelManager) {
        this.processModelManager = processModelManager;
    }

    @Override
    public ProcessModelManager getProcessModels() {
        return processModelManager;
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    @Override
    public ProductManager getProducts() {
        return productManager;
    }

    public void setSpatialModel(SpatialModel spatialModel) {
        this.spatialModel = spatialModel;
    }

    @Override
    public SpatialModel getSpatialModel() {
        return spatialModel;
    }

    public void setTimeModel(JadexTimeModel timeModel) {
        this.timeModel = timeModel;
    }

    @Override
    public JadexTimeModel getTimeModel() {
        return timeModel;
    }

    public void setLifeCycleControl(JadexLifeCycleControl lifeCycleControl) {
        this.lifeCycleControl = lifeCycleControl;
    }

    @Override
    public JadexLifeCycleControl getLiveCycleControl() {
        return lifeCycleControl;
    }

    @Override
    public Version getVersion() {
        return IRPact.VERSION;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public void setSimulationRandom(Rnd rnd) {
        this.rnd = rnd;
    }

    @Override
    public BinaryTaskManager getTaskManager() {
        return taskManager;
    }

    public void setTaskManager(BinaryTaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public PersistenceModul getPersistenceModul() {
        return persistenceModul;
    }

    public void setPersistenceModul(PersistenceModul persistenceModul) {
        this.persistenceModul = persistenceModul;
    }

    @Override
    public Rnd getSimulationRandom() {
        return rnd;
    }

    @Override
    public void preAgentCreation() throws MissingDataException {
        agentManager.preAgentCreation();
        socialNetwork.preAgentCreation();
        productManager.preAgentCreation();
        processModelManager.preAgentCreation();
        spatialModel.preAgentCreation();
        timeModel.preAgentCreation();
        lifeCycleControl.preAgentCreation();
    }

    @Override
    public void createAgents() throws InitializationException {
        SocialGraph graph = getNetwork().getGraph();
        for(ConsumerAgentGroup cag: agentManager.getConsumerAgentGroups()) {
            int numberOfAgents = settings.getInitialNumberOfConsumerAgents(cag);
            int currentNumberOfAgents = cag.getNumberOfAgents();
            int agentsToCreate = Math.max(numberOfAgents - currentNumberOfAgents, 0);
            if(agentsToCreate > 0) {
                LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "create {} agents for group '{}'", agentsToCreate, cag.getName());
                for(int i = 0; i < agentsToCreate; i++) {
                    ConsumerAgent ca = cag.deriveAgent();

                    if(ca.getSocialGraphNode() != null) {
                        throw ExceptionUtil.create(InitializationException::new, "agent '{}' already has graph node", ca.getName());
                    }
                    if(graph.hasNode(ca)) {
                        throw ExceptionUtil.create(InitializationException::new, "graph node for agent '{}' already exists", ca.getName());
                    }
                    if(!cag.addAgent(ca)) {
                        throw ExceptionUtil.create(InitializationException::new, "adding agent '{}' failed, name already exists", ca.getName());
                    }

                    SocialGraph.Node node = graph.addAgentAndGetNode(ca);
                    ca.setSocialGraphNode(node);
                    LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "added agent '{}' (id:{}) to group '{}'", ca.getName(), SpatialInformation.tryGetId(ca.getSpatialInformation()), cag.getName());
                }
            } else {
                LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "create no agents for group '{}'", cag.getName());
            }
        }
    }

    @Override
    public void preAgentCreationValidation() throws ValidationException {
        agentManager.preAgentCreationValidation();
        socialNetwork.preAgentCreationValidation();
        productManager.preAgentCreationValidation();
        processModelManager.preAgentCreationValidation();
        spatialModel.preAgentCreationValidation();
        timeModel.preAgentCreationValidation();
        lifeCycleControl.preAgentCreationValidation();
    }

    @Override
    public void postAgentCreation() throws MissingDataException, InitializationException {
        agentManager.postAgentCreation();
        socialNetwork.postAgentCreation();
        productManager.postAgentCreation();
        processModelManager.postAgentCreation();
        spatialModel.postAgentCreation();
        timeModel.postAgentCreation();
        lifeCycleControl.postAgentCreation();
    }

    @Override
    public void postAgentCreationValidation() throws ValidationException {
        agentManager.postAgentCreationValidation();
        socialNetwork.postAgentCreationValidation();
        productManager.postAgentCreationValidation();
        processModelManager.postAgentCreationValidation();
        spatialModel.postAgentCreationValidation();
        timeModel.postAgentCreationValidation();
        lifeCycleControl.postAgentCreationValidation();
    }

    @Override
    public void preSimulationStart() throws MissingDataException {
        agentManager.preSimulationStart();
        socialNetwork.preSimulationStart();
        productManager.preSimulationStart();
        processModelManager.preSimulationStart();
        spatialModel.preSimulationStart();
        timeModel.preSimulationStart();
        lifeCycleControl.preSimulationStart();
        //==
        LOGGER.trace("enable sync mode for master rnd");
        rnd.enableSync();
    }
}
