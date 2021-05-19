package de.unileipzig.irpact.io.spec.impl.product;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.interest.InProductInterestSupplyScheme;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSubSpec;
import de.unileipzig.irpact.io.spec.impl.AbstractSuperSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Collection;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class ProductInterestSupplySchemeSpec extends AbstractSuperSpec<InProductInterestSupplyScheme> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ProductInterestSupplySchemeSpec.class);

    public static final ProductInterestSupplySchemeSpec INSTANCE = new ProductInterestSupplySchemeSpec();

    private static final List<AbstractSubSpec<? extends InProductInterestSupplyScheme>> MODELS = createModels(
            ProductThresholdInterestSupplySchemeSpec.INSTANCE
    );

    @Override
    protected InProductInterestSupplyScheme[] newArray(int len) {
        return new InProductInterestSupplyScheme[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    protected Collection<? extends AbstractSubSpec<? extends InProductInterestSupplyScheme>> getModels() {
        return MODELS;
    }

    @Override
    public InProductInterestSupplyScheme[] toParamArray(SpecificationJob job) throws ParsingException {
        return toParamArray(job.getData().getConsumerAgentGroups(), job);
    }

    @Override
    public Class<InProductInterestSupplyScheme> getParamType() {
        return InProductInterestSupplyScheme.class;
    }
}
