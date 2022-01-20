package de.unileipzig.irpact.io.param.input.product.initial;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.handler.DependentInterestScaler;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.getInstance;
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PRODUCTS_INITADOPT_PVACTDEPENTINTER;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(PRODUCTS_INITADOPT_PVACTDEPENTINTER)
public class InPVactDependentInterestScaler implements InNewProductHandler {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(LocalizedUiResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(LocalizedUiResource res) {
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            intDefault = 0
    )
    public int priority = 0;
    public void setPriority(int priority) {
        this.priority = priority;
    }
    public int getPriority() {
        return priority;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InUnivariateDoubleDistribution[] distribution = new InUnivariateDoubleDistribution[0];
    public InUnivariateDoubleDistribution getDistribution() throws ParsingException {
        return getInstance(distribution, "distribution");
    }
    public void setDistribution(InUnivariateDoubleDistribution distribution) {
        this.distribution = new InUnivariateDoubleDistribution[]{distribution};
    }

    public InPVactDependentInterestScaler() {
    }

    @Override
    public InPVactDependentInterestScaler copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InPVactDependentInterestScaler newCopy(CopyCache cache) {
        InPVactDependentInterestScaler copy = new InPVactDependentInterestScaler();
        copy.name = name;
        return copy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public DependentInterestScaler parse(IRPactInputParser parser) throws ParsingException {
        DependentInterestScaler handler = new DependentInterestScaler();
        handler.setName(getName());
        handler.setInterestAttributeName(RAConstants.INITIAL_PRODUCT_INTEREST);
        handler.setReferenceAttributeName(RAConstants.NOVELTY_SEEKING);
        handler.setDistribution(parser.parseEntityTo(getDistribution()));
        handler.setPriority(getPriority());

        return handler;
    }
}
