package de.unileipzig.irpact.io.spec2.impl.product;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.interest.InProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.io.spec2.SpecificationHelper2;
import de.unileipzig.irpact.io.spec2.SpecificationJob2;
import de.unileipzig.irpact.io.spec2.impl.AbstractSubSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class ProductThresholdInterestSupplySchemeSpec extends AbstractSubSpec<InProductThresholdInterestSupplyScheme> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ProductThresholdInterestSupplySchemeSpec.class);

    public static final ProductThresholdInterestSupplySchemeSpec INSTANCE = new ProductThresholdInterestSupplySchemeSpec();
    public static final String TYPE = "ProductThresholdInterestSupplyScheme";

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
        return input instanceof InProductThresholdInterestSupplyScheme;
    }

    @Override
    protected InProductThresholdInterestSupplyScheme[] newArray(int len) {
        return new InProductThresholdInterestSupplyScheme[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InProductThresholdInterestSupplyScheme[] toParamArray(SpecificationJob2 job) throws ParsingException {
        return toParamArray(job.getData().getSpatialModel(), job);
    }

    @Override
    public InProductThresholdInterestSupplyScheme toParam(SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
        String name = rootSpec.getText(TAG_name);
        if(job.isCached(name)) {
            return job.getCached(name);
        }

        InProductThresholdInterestSupplyScheme scheme = new InProductThresholdInterestSupplyScheme();
        scheme.setName(rootSpec.getText(TAG_name));
        scheme.setInterestDistribution(job.parseInlinedDistribution(rootSpec.getNode(TAG_distribution)));

        job.cache(name, scheme);
        return scheme;
    }

    @Override
    public Class<InProductThresholdInterestSupplyScheme> getParamType() {
        return InProductThresholdInterestSupplyScheme.class;
    }

    @Override
    public void toSpec(InProductThresholdInterestSupplyScheme input, SpecificationJob2 job) throws ParsingException {
        create(input, job.getData().getSpatialModel(), job);
    }

    @Override
    protected void create(InProductThresholdInterestSupplyScheme input, SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_type, TYPE);
        rootSpec.set(TAG_distribution, job.inlineEntity(input.getInterestDistribution(), false));
    }
}
