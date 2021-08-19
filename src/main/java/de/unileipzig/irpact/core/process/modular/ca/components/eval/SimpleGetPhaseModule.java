package de.unileipzig.irpact.core.process.modular.ca.components.eval;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.process.modular.ca.AdoptionResult;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentEvaluationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.base.AbstractConsumerAgentModule;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public class SimpleGetPhaseModule extends AbstractConsumerAgentModule implements ConsumerAgentEvaluationModule {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SimpleGetPhaseModule.class);

    public SimpleGetPhaseModule() {
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                getName(),
                getAdoptionResult()
        );
    }
            
    @Override
    public void preAgentCreationValidation() throws ValidationException {
        if(adoptionResult == null) {
            throw new ValidationException("missing AdoptionResult");
        }
    }

    protected AdoptionResult adoptionResult;
    public void setAdoptionResult(AdoptionResult adoptionResult) {
        this.adoptionResult = adoptionResult;
    }
    public AdoptionResult getAdoptionResult() {
        return adoptionResult;
    }
    public AdoptionResult getValidAdoptionResult() {
        if(adoptionResult == null) {
            throw new NoSuchElementException("missing AdoptionResult");
        }
        return adoptionResult;
    }

    @Override
    public AdoptionResult evaluate(ConsumerAgentData data) throws Throwable {
        return getValidAdoptionResult();
    }
}
