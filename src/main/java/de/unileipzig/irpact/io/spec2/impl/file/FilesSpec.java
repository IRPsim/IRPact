package de.unileipzig.irpact.io.spec2.impl.file;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.file.InFile;
import de.unileipzig.irpact.io.spec2.SpecificationHelper2;
import de.unileipzig.irpact.io.spec2.SpecificationJob2;
import de.unileipzig.irpact.io.spec2.impl.AbstractSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_pvFiles;

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
    public InFile[] toParamArray(SpecificationJob2 job) throws ParsingException {
        List<InFile> files = new ArrayList<>();
        Collections.addAll(files, PVFileSpec.INSTANCE.toParamArray(job));
        Collections.addAll(files, SpatialTableFileSpec.INSTANCE.toParamArray(job));
        return files.toArray(new InFile[0]);
    }

    @Override
    public InFile toParam(SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<InFile> getParamType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void toSpec(InFile input, SpecificationJob2 job) throws ParsingException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void create(InFile input, SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
        throw new UnsupportedOperationException();
    }
}
