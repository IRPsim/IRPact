package de.unileipzig.irpact.core.process.modular.ca.model;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.modular.ca.AdoptionResult;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.Stage;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentEvaluationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.base.AbstractConsumerAgentModuleWithNGenericSubModules;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.AdoptionPhase;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class ConsumerAgentMPMWithAdoptionHandler extends AbstractConsumerAgentMPMWithUpdater {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ConsumerAgentMPMWithAdoptionHandler.class);

    private final AdoptionHandlerModule MASTER = new AdoptionHandlerModule(this);
    protected Rnd rnd;

    public ConsumerAgentMPMWithAdoptionHandler() {
    }

    @Override
    public void setEnvironment(SimulationEnvironment environment) {
        super.setEnvironment(environment);
        MASTER.setEnvironment(environment);
    }

    @Override
    public void setStartModule(ConsumerAgentEvaluationModule startModule) {
        MASTER.setStartModule(startModule);
        MASTER.handleMissingParameters(this);
    }

    @Override
    public ConsumerAgentEvaluationModule getStartModule() {
        return MASTER;
    }

    public void setRnd(Rnd rnd) {
        this.rnd = rnd;
    }

    public Rnd getRnd() {
        return rnd;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                getName(),
                getRnd()
        );
    }

    /**
     * @author Daniel Abitz
     */
    public static class AdoptionHandlerModule
            extends AbstractConsumerAgentModuleWithNGenericSubModules<ConsumerAgentEvaluationModule>
            implements ConsumerAgentEvaluationModule {

        private static final IRPLogger LOGGER = IRPLogging.getLogger(AdoptionHandlerModule.class);

        private static final int NUMBER_OF_SUBMODULES = 1;
        private static final int INDEX_START_MODULE = 0;

        private final ConsumerAgentMPMWithAdoptionHandler HANDLER;

        private AdoptionHandlerModule(ConsumerAgentMPMWithAdoptionHandler handler) {
            super(NUMBER_OF_SUBMODULES);
            HANDLER = handler;
            setEnvironment(handler.getEnvironment());
        }

        @Override
        protected void handleNewProduct(Product product, Rnd rnd) {
            super.handleNewProduct(product, rnd);
        }

        private void setStartModule(ConsumerAgentEvaluationModule startModule) {
            setSubModule(INDEX_START_MODULE, startModule);
        }

        public ConsumerAgentEvaluationModule getStartModule() {
            return getSubModule(INDEX_START_MODULE);
        }

        @Override
        public AdoptionResult evaluate(ConsumerAgentData data) throws Throwable {
            AdoptionResult result = getStartModule().evaluate(data);

            switch(result) {
                case ADOPTED:
                    handleAdopted(data);
                    break;

                case IMPEDED:
                    handleImpeded(data);
                    break;

                case IN_PROCESS:
                    handleInProcess(data);
                    break;

                default:
                    LOGGER.warn("[{}] unsupported AdoptionResult: {}", HANDLER.getName(), result);
            }

            return result;
        }

        private void handleAdopted(ConsumerAgentData data) {
            Timestamp now = now();
            data.getAgent().adopt(data.getNeed(), data.getProduct(), now, determinePhase(now));
            data.updateStage(Stage.ADOPTED);
        }

        private void handleImpeded(ConsumerAgentData data) {
            data.updateStage(Stage.IMPEDED);
        }

        private void handleInProcess(ConsumerAgentData data) {
            //nothing
        }

        private AdoptionPhase determinePhase(Timestamp ts) {
            return HANDLER.determine(ts);
        }

        @Override
        public IRPLogger getDefaultLogger() {
            return LOGGER;
        }

        @Override
        public IRPSection getDefaultResultSection() {
            return IRPSection.SIMULATION_PROCESS;
        }

        @Override
        public int getChecksum() {
            return ChecksumComparable.DEFAULT_NONNULL_CHECKSUM;
        }
    }
}
