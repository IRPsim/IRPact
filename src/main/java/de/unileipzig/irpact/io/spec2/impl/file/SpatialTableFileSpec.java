package de.unileipzig.irpact.io.spec2.impl.file;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.spec2.SpecificationHelper2;
import de.unileipzig.irpact.io.spec2.SpecificationJob2;
import de.unileipzig.irpact.io.spec2.impl.AbstractSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import static de.unileipzig.irpact.io.spec2.SpecificationConstants.TAG_spatialTableFile;

/**
 * @author Daniel Abitz
 */
public class SpatialTableFileSpec extends AbstractSpec<InSpatialTableFile> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SpatialTableFileSpec.class);

    public static final SpatialTableFileSpec INSTANCE = new SpatialTableFileSpec();

    @Override
    protected InSpatialTableFile[] newArray(int len) {
        return new InSpatialTableFile[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InSpatialTableFile[] toParamArray(SpecificationJob2 job) throws ParsingException {
        SpecificationHelper2 spec = toHelper(job.getData().getFiles());
        if(spec.hasNot(TAG_spatialTableFile)) {
            return new InSpatialTableFile[0];
        }
        SpecificationHelper2 arr = spec.getArray(TAG_spatialTableFile);
        InSpatialTableFile[] out = newArray(arr.size());
        int i = 0;
        for(SpecificationHelper2 entry: arr.iterateElements()) {
            String name = entry.getText();
            if(job.isCached(name)) {
                out[i++] = job.getCached(name);
            } else {
                InSpatialTableFile file = new InSpatialTableFile(name);
                job.cache(name, file);;
                out[i++] = file;
            }
        }
        return out;
    }

    @Override
    public InSpatialTableFile toParam(SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<InSpatialTableFile> getParamType() {
        return InSpatialTableFile.class;
    }

    @Override
    public void toSpec(InSpatialTableFile input, SpecificationJob2 job) throws ParsingException {
        create(input, job.getData().getFiles(), job);
    }

    @Override
    protected void create(InSpatialTableFile input, SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
        SpecificationHelper2 pvSpec = rootSpec.getOrCreateArray(TAG_spatialTableFile);
        pvSpec.addIfNotExists(input.getName());
    }
}
