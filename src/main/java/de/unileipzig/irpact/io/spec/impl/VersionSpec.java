package de.unileipzig.irpact.io.spec.impl;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.io.param.input.InScenarioVersion;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irptools.util.log.IRPLogger;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_version;

/**
 * @author Daniel Abitz
 */
public class VersionSpec extends AbstractSpec<InScenarioVersion> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(VersionSpec.class);

    public static final VersionSpec INSTANCE = new VersionSpec();

    @Override
    protected InScenarioVersion[] newArray(int len) {
        return new InScenarioVersion[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InScenarioVersion[] toParamArray(SpecificationJob job) throws ParsingException {
        return new InScenarioVersion[]{
                toParam(job.getData().getGeneral().get(), job)
        };
    }

    @Override
    public InScenarioVersion toParam(SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        InScenarioVersion version = new InScenarioVersion();
        version.setVersion(rootSpec.getText(TAG_version));
        return version;
    }

    @Override
    public Class<InScenarioVersion> getParamType() {
        return InScenarioVersion.class;
    }

    @Override
    public void toSpec(InScenarioVersion input, SpecificationJob job) throws ParsingException {
        create(input, job.getData().getGeneral(), job);
    }

    @Override
    protected void create(InScenarioVersion input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        rootSpec.set(TAG_version, input.getVersion());
    }
}
