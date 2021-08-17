package de.unileipzig.irpact.io.spec.impl.file;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.io.param.input.file.InFile;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class FilesSpec extends AbstractSpec<InFile> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(FilesSpec.class);

    public static final FilesSpec INSTANCE = new FilesSpec();

    @Override
    protected InFile[] newArray(int len) {
        return new InFile[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InFile[] toParamArray(SpecificationJob job) throws ParsingException {
        List<InFile> files = new ArrayList<>();
        Collections.addAll(files, PVFileSpec.INSTANCE.toParamArray(job));
        Collections.addAll(files, SpatialTableFileSpec.INSTANCE.toParamArray(job));
        return files.toArray(new InFile[0]);
    }

    @Override
    public InFile toParam(SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<InFile> getParamType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void toSpec(InFile input, SpecificationJob job) throws ParsingException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void create(InFile input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        throw new UnsupportedOperationException();
    }
}
