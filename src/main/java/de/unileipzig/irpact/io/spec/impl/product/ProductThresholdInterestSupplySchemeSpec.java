package de.unileipzig.irpact.io.spec.impl.product;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.interest.InProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSubSpec;
import de.unileipzig.irpact.util.Todo;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
@Todo("KAPUTT")
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
    public InProductThresholdInterestSupplyScheme[] toParamArray(SpecificationJob job) throws ParsingException {
        return toParamArray(job.getData().getSpatialModel(), job);
    }

    @Override
    public InProductThresholdInterestSupplyScheme toParam(SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        String name = rootSpec.getText(TAG_name);
        if(job.isCached(name)) {
            return job.getCached(name);
        }

        InProductThresholdInterestSupplyScheme scheme = new InProductThresholdInterestSupplyScheme();
        scheme.setName(rootSpec.getText(TAG_name));
        //TODO
        //scheme.setInterestDistribution(job.parseInlinedDistribution(rootSpec.getNode(TAG_distribution)));

        job.cache(name, scheme);
        return scheme;
    }

    @Override
    public Class<InProductThresholdInterestSupplyScheme> getParamType() {
        return InProductThresholdInterestSupplyScheme.class;
    }

    @Override
    public void toSpec(InProductThresholdInterestSupplyScheme input, SpecificationJob job) throws ParsingException {
        create(input, job.getData().getSpatialModel(), job);
    }

    @Override
    protected void create(InProductThresholdInterestSupplyScheme input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_type, TYPE);
        //TODO
        //rootSpec.set(TAG_distribution, job.inlineEntity(input.getInterestDistribution(), false));
    }
}
