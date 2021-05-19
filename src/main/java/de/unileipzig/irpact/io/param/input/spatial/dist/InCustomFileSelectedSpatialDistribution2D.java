package de.unileipzig.irpact.io.param.input.spatial.dist;

import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.spatial.SpatialTableFileContent;
import de.unileipzig.irpact.core.spatial.distribution.DiscreteSpatialDistribution;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.develop.TodoException;
import de.unileipzig.irpact.io.param.input.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.Copyable;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.List;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.IOConstants.SPATIAL_MODEL_DIST_FILE_CUSTOMPOS;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InCustomFileSelectedSpatialDistribution2D implements InSpatialDistribution {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), SPATIAL, SPATIAL_MODEL_DIST, SPATIAL_MODEL_DIST_FILE, SPATIAL_MODEL_DIST_FILE_CUSTOMPOS, thisName());
        addEntry(res, thisClass(), "xPosSupplier");
        addEntry(res, thisClass(), "yPosSupplier");
        addEntry(res, thisClass(), "attrFile");
        addEntry(res, thisClass(), "selectKey");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InCustomFileSelectedSpatialDistribution2D.class);

    public String _name;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] xPosSupplier;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] yPosSupplier;

    @FieldDefinition
    public InSpatialTableFile[] attrFile;

    @FieldDefinition
    public InAttributeName[] selectKey;

    public InCustomFileSelectedSpatialDistribution2D() {
    }

    @Override
    public Copyable copy(CopyCache copyCache) {
        throw new TodoException();
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public void setup(IRPactInputParser parser, Object input) throws ParsingException {
        JadexConsumerAgentGroup jCag = (JadexConsumerAgentGroup) input;

        UnivariateDoubleDistribution xSupplier = parser.parseEntityTo(getXPosSupplier());
        UnivariateDoubleDistribution ySupplier = parser.parseEntityTo(getYPosSupplier());
        String selectKey = getSelectKey().getName();
        SpatialTableFileContent attrList = parser.parseEntityTo(getAttributeFile());
        List<List<SpatialAttribute>> selectedList = SpatialUtil.filter(attrList.content().listTable(), selectKey, jCag.getName());
        List<SpatialInformation> infos = SpatialUtil.mapToPoint2D(selectedList, xSupplier, ySupplier, "");

        DiscreteSpatialDistribution dist = new DiscreteSpatialDistribution();
        dist.setName(getName());
        Rnd rnd = parser.deriveRnd();
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "InCustomSelectedSpatialDistribution2D '{}' uses seed: {}", getName(), rnd.getInitialSeed());
        dist.setRandom(rnd);
        dist.addAll(infos);
        jCag.setSpatialDistribution(dist);
    }

    public InUnivariateDoubleDistribution getXPosSupplier() throws ParsingException {
        return ParamUtil.getInstance(xPosSupplier, "XPosSupplier");
    }

    public InUnivariateDoubleDistribution getYPosSupplier() throws ParsingException {
        return ParamUtil.getInstance(yPosSupplier, "YPosSupplier");
    }

    public InSpatialTableFile getAttributeFile() throws ParsingException {
        return ParamUtil.getInstance(attrFile, "AttributeFile");
    }

    public InAttributeName getSelectKey() throws ParsingException {
        return ParamUtil.getInstance(selectKey, "SelectKey");
    }
}
