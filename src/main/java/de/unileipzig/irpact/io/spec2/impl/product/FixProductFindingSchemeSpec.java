package de.unileipzig.irpact.io.spec2.impl.product;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.product.InFixProductFindingScheme;
import de.unileipzig.irpact.io.spec2.SpecificationHelper2;
import de.unileipzig.irpact.io.spec2.SpecificationJob2;
import de.unileipzig.irpact.io.spec2.impl.AbstractSubSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class FixProductFindingSchemeSpec extends AbstractSubSpec<InFixProductFindingScheme> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(FixProductFindingSchemeSpec.class);

    public static final FixProductFindingSchemeSpec INSTANCE = new FixProductFindingSchemeSpec();
    public static final String TYPE = "FixProductFindingScheme";

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
        return input instanceof InFixProductFindingScheme;
    }

    @Override
    protected InFixProductFindingScheme[] newArray(int len) {
        return new InFixProductFindingScheme[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InFixProductFindingScheme[] toParamArray(SpecificationJob2 job) throws ParsingException {
        return toParamArray(job.getData().getSpatialModel(), job);
    }

    @Override
    public InFixProductFindingScheme toParam(SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
        String name = rootSpec.getText(TAG_name);
        if(job.isCached(name)) {
            return job.getCached(name);
        }

        InFixProductFindingScheme scheme = new InFixProductFindingScheme();
        scheme.setName(rootSpec.getText(TAG_name));
        scheme.setFixProduct(job.findFixProduct(rootSpec.getText(TAG_fixProduct)));

        job.cache(name, scheme);
        return scheme;
    }

    @Override
    public Class<InFixProductFindingScheme> getParamType() {
        return InFixProductFindingScheme.class;
    }

    @Override
    public void toSpec(InFixProductFindingScheme input, SpecificationJob2 job) throws ParsingException {
        create(input, job.getData().getSpatialModel(), job);
    }

    @Override
    protected void create(InFixProductFindingScheme input, SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_type, TYPE);
        rootSpec.set(TAG_fixProducts, input.getFixProduct().getName());
    }
}
