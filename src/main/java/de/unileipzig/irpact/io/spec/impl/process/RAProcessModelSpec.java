package de.unileipzig.irpact.io.spec.impl.process;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessModel;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSubSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class RAProcessModelSpec extends AbstractSubSpec<InRAProcessModel> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RAProcessModelSpec.class);

    public static final RAProcessModelSpec INSTANCE = new RAProcessModelSpec();
    public static final String TYPE = "RAProcessModel";

    @Override
    public boolean isType(String type) {
        return Objects.equals(type, TYPE);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean isInstance(Object input) {
        return input instanceof InRAProcessModel;
    }

    @Override
    protected InRAProcessModel[] newArray(int len) {
        return new InRAProcessModel[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InRAProcessModel[] toParamArray(SpecificationJob job) throws ParsingException {
        return toParamArray(job.getData().getSpatialModel(), job);
    }

    @Override
    public InRAProcessModel toParam(SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        String name = rootSpec.getText(TAG_name);
        if(job.isCached(name)) {
            return job.getCached(name);
        }

        InRAProcessModel topo = new InRAProcessModel();
        topo.setName(name);

        topo.setA(rootSpec.getDouble(TAG_parameters, TAG_a));
        topo.setB(rootSpec.getDouble(TAG_parameters, TAG_b));
        topo.setC(rootSpec.getDouble(TAG_parameters, TAG_c));
        topo.setD(rootSpec.getDouble(TAG_parameters, TAG_d));
        topo.setAdopterPoints(rootSpec.getInt(TAG_parameters, TAG_adopterPoints));
        topo.setInterestedPoints(rootSpec.getInt(TAG_parameters, TAG_interestedPoints));
        topo.setAwarePoints(rootSpec.getInt(TAG_parameters, TAG_awarePoints));
        topo.setUnknownPoints(rootSpec.getInt(TAG_parameters, TAG_unknownPoints));
        topo.setPvFile(job.findFile(rootSpec.getText(TAG_parameters, TAG_file)));
        topo.setLogisticFactor(rootSpec.getDouble(TAG_parameters, TAG_logisticFactor));

//        InUncertaintyGroupAttribute[] uncertAttrs = job.parseInlinedUncertaintyGroupAttributes(rootSpec.getNode(TAG_parameters, TAG_uncertainties));
//        topo.setUncertaintyGroupAttributes(uncertAttrs);

        job.cache(name, topo);
        return topo;
    }

    @Override
    public Class<InRAProcessModel> getParamType() {
        return InRAProcessModel.class;
    }

    @Override
    public void toSpec(InRAProcessModel input, SpecificationJob job) throws ParsingException {
        create(input, job.getData().getSpatialModel(), job);
    }

    @Override
    protected void create(InRAProcessModel input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_type, TYPE);

        rootSpec.set(TAG_parameters, TAG_a, input.getA());
        rootSpec.set(TAG_parameters, TAG_b, input.getB());
        rootSpec.set(TAG_parameters, TAG_c, input.getC());
        rootSpec.set(TAG_parameters, TAG_d, input.getD());
        rootSpec.set(TAG_parameters, TAG_adopterPoints, input.getAdopterPoints());
        rootSpec.set(TAG_parameters, TAG_interestedPoints, input.getInterestedPoints());
        rootSpec.set(TAG_parameters, TAG_awarePoints, input.getAwarePoints());
        rootSpec.set(TAG_parameters, TAG_unknownPoints, input.getUnknownPoints());
        rootSpec.set(TAG_parameters, TAG_file, input.getPvFile().getName());
        rootSpec.set(TAG_parameters, TAG_logisticFactor, input.getLogisticFactor());

//        rootSpec.set(TAG_parameters, TAG_uncertainties, job.inlineEntitiyArray(input.getUncertaintyGroupAttributes(), true));
    }
}
