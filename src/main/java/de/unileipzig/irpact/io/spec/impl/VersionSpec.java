package de.unileipzig.irpact.io.spec.impl;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.InVersion;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irptools.util.log.IRPLogger;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_version;

/**
 * @author Daniel Abitz
 */
public class VersionSpec extends AbstractSpec<InVersion> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(VersionSpec.class);

    public static final VersionSpec INSTANCE = new VersionSpec();

    @Override
    protected InVersion[] newArray(int len) {
        return new InVersion[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InVersion[] toParamArray(SpecificationJob job) throws ParsingException {
        return new InVersion[]{
                toParam(job.getData().getGeneral().get(), job)
        };
    }

    @Override
    public InVersion toParam(SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        return new InVersion(rootSpec.getText(TAG_version));
    }

    @Override
    public Class<InVersion> getParamType() {
        return InVersion.class;
    }

    @Override
    public void toSpec(InVersion input, SpecificationJob job) throws ParsingException {
        create(input, job.getData().getGeneral(), job);
    }

    @Override
    protected void create(InVersion input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        rootSpec.set(TAG_version, input.getVersion());
    }
}
