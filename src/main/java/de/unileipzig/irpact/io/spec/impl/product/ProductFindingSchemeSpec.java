package de.unileipzig.irpact.io.spec.impl.product;

import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.product.InProductFindingScheme;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSubSpec;
import de.unileipzig.irpact.io.spec.impl.AbstractSuperSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Collection;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class ProductFindingSchemeSpec extends AbstractSuperSpec<InProductFindingScheme> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ProductFindingSchemeSpec.class);

    public static final ProductFindingSchemeSpec INSTANCE = new ProductFindingSchemeSpec();

    private static final List<AbstractSubSpec<? extends InProductFindingScheme>> MODELS = CollectionUtil.arrayListOf(
            FixProductFindingSchemeSpec.INSTANCE
    );

    @Override
    protected InProductFindingScheme[] newArray(int len) {
        return new InProductFindingScheme[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    protected Collection<? extends AbstractSubSpec<? extends InProductFindingScheme>> getModels() {
        return MODELS;
    }

    @Override
    public InProductFindingScheme[] toParamArray(SpecificationJob job) throws ParsingException {
        return toParamArray(job.getData().getConsumerAgentGroups(), job);
    }

    @Override
    public Class<InProductFindingScheme> getParamType() {
        return InProductFindingScheme.class;
    }
}
