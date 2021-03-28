package de.unileipzig.irpact.io.param.input.spatial.dist;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.core.spatial.distribution.WeightedDiscreteSpatialDistribution;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.IOConstants.SPATIAL_MODEL_DIST_FILE_CUSTOMPOS;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InCustomFileSelectedGroupedSpatialDistribution2D implements InSpatialDistribution {

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
        addEntry(res, thisClass(), "groupKey");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] xPosSupplier;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] yPosSupplier;

    @FieldDefinition
    public InSpatialTableFile[] file;

    @FieldDefinition
    public InAttributeName[] selectKey;

    @FieldDefinition
    public InAttributeName[] groupKey;

    public InCustomFileSelectedGroupedSpatialDistribution2D() {
    }

    public InCustomFileSelectedGroupedSpatialDistribution2D(
            String name,
            InUnivariateDoubleDistribution xPosSupplier,
            InUnivariateDoubleDistribution yPosSupplier,
            InSpatialTableFile file,
            InAttributeName selectKey,
            InAttributeName groupKey) {
        this._name = name;
        setXPosSupplier(xPosSupplier);
        setYPosSupplier(yPosSupplier);
        setFile(file);
        setSelectKey(selectKey);
        setGroupKey(groupKey);
    }

    public void setName(String name) {
        this._name = name;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setXPosSupplier(InUnivariateDoubleDistribution xPosSupplier) {
        this.xPosSupplier = new InUnivariateDoubleDistribution[]{xPosSupplier};
    }

    public void setYPosSupplier(InUnivariateDoubleDistribution yPosSupplier) {
        this.yPosSupplier = new InUnivariateDoubleDistribution[]{yPosSupplier};
    }

    public void setFile(InSpatialTableFile file) {
        this.file = new InSpatialTableFile[]{file};
    }

    public void setSelectKey(InAttributeName selectKey) {
        this.selectKey = new InAttributeName[]{selectKey};
    }

    public void setGroupKey(InAttributeName groupKey) {
        this.groupKey = new InAttributeName[]{groupKey};
    }

    public static WeightedDiscreteSpatialDistribution createInstance(
            String name,
            List<List<SpatialAttribute>> attrList,
            UnivariateDoubleDistribution xSupplier,
            UnivariateDoubleDistribution ySupplier,
            String selectKey,
            String selectValue,
            String groupingKey,
            Rnd rnd) {
        List<List<SpatialAttribute>> selectedList = SpatialUtil.filter(attrList, selectKey, selectValue);
        List<SpatialInformation> infos = SpatialUtil.mapToPoint2D(selectedList, xSupplier, ySupplier);
        Map<String, List<SpatialInformation>> groupedInfos = SpatialUtil.groupingBy(infos, groupingKey);

        WeightedDiscreteSpatialDistribution dist = new WeightedDiscreteSpatialDistribution();
        dist.setName(name + "_" + selectValue);
        dist.setRandom(rnd);
        for(Map.Entry<String, List<SpatialInformation>> entry: groupedInfos.entrySet()) {
            dist.add(entry.getKey(), entry.getValue());
        }
        dist.initalize();
        return dist;
    }

    @Override
    public void setup(InputParser parser, Object input) throws ParsingException {
        JadexConsumerAgentGroup jCag = (JadexConsumerAgentGroup) input;

        UnivariateDoubleDistribution xSupplier = parser.parseEntityTo(getXPosSupplier());
        UnivariateDoubleDistribution ySupplier = parser.parseEntityTo(getYPosSupplier());
        String selectKey = getSelectKey().getName();
        String groupingKey = getGroupKey().getName();
        List<List<SpatialAttribute>> attrList = parser.parseEntityTo(getFile());
        Rnd rnd = parser.deriveRnd();
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "InCustomSelectedSpatialDistribution2D '{}' uses seed: {}", getName(), rnd.getInitialSeed());

        WeightedDiscreteSpatialDistribution dist = createInstance(
                getName(),
                attrList,
                xSupplier,
                ySupplier,
                selectKey,
                jCag.getName(),
                groupingKey,
                rnd
        );
        jCag.setSpatialDistribution(dist);
    }

    public InUnivariateDoubleDistribution getXPosSupplier() throws ParsingException {
        return ParamUtil.getInstance(xPosSupplier, "XPosSupplier");
    }

    public InUnivariateDoubleDistribution getYPosSupplier() throws ParsingException {
        return ParamUtil.getInstance(yPosSupplier, "YPosSupplier");
    }

    public InSpatialTableFile getFile() throws ParsingException {
        return ParamUtil.getInstance(file, "File");
    }

    public InAttributeName getSelectKey() throws ParsingException {
        return ParamUtil.getInstance(selectKey, "SelectKey");
    }

    public InAttributeName getGroupKey() throws ParsingException {
        return ParamUtil.getInstance(groupKey, "GroupKey");
    }
}
