package de.unileipzig.irpact.io.spec2.impl.file;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.spec2.SpecificationHelper2;
import de.unileipzig.irpact.io.spec2.SpecificationJob2;
import de.unileipzig.irpact.io.spec2.impl.AbstractSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class PVFileSpec extends AbstractSpec<InPVFile> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(PVFileSpec.class);

    public static final PVFileSpec INSTANCE = new PVFileSpec();

    @Override
    protected InPVFile[] newArray(int len) {
        return new InPVFile[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InPVFile[] toParamArray(SpecificationJob2 job) throws ParsingException {
        SpecificationHelper2 spec = toHelper(job.getData().getFiles());
        if(spec.hasNot(TAG_pvFiles)) {
            return new InPVFile[0];
        }
        SpecificationHelper2 arr = spec.getArray(TAG_pvFiles);
        InPVFile[] out = newArray(arr.size());
        int i = 0;
        for(SpecificationHelper2 entry: arr.iterateElements()) {
            String name = entry.getText();
            if(job.isCached(name)) {
                out[i++] = job.getCached(name);
            } else {
                InPVFile file = new InPVFile(name);
                job.cache(name, file);;
                out[i++] = file;
            }
        }
        return out;
    }

    @Override
    public InPVFile toParam(SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<InPVFile> getParamType() {
        return InPVFile.class;
    }

    @Override
    public void toSpec(InPVFile input, SpecificationJob2 job) throws ParsingException {
        create(input, job.getData().getFiles(), job);
    }

    @Override
    protected void create(InPVFile input, SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
        SpecificationHelper2 pvSpec = rootSpec.getOrCreateArray(TAG_pvFiles);
        pvSpec.addIfNotExists(input.getName());
    }
}
