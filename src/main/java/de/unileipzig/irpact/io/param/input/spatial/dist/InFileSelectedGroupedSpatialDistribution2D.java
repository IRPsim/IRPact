package de.unileipzig.irpact.io.param.input.spatial.dist;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.core.spatial.distribution.WeightedDiscreteSpatialDistribution;
import de.unileipzig.irpact.core.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.input.InputParser;
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
import static de.unileipzig.irpact.io.param.IOConstants.SPATIAL_MODEL_DIST_FILE_FILEPOS;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InFileSelectedGroupedSpatialDistribution2D implements InSpatialDistribution {

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
        putClassPath(res, thisClass(), SPATIAL, SPATIAL_MODEL_DIST, SPATIAL_MODEL_DIST_FILE, SPATIAL_MODEL_DIST_FILE_FILEPOS, thisName());
        addEntry(res, thisClass(), "attrFile");
        addEntry(res, thisClass(), "xPositionKey");
        addEntry(res, thisClass(), "yPositionKey");
        addEntry(res, thisClass(), "selectKey");
        addEntry(res, thisClass(), "groupKey");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public InAttributeName[] xPositionKey;

    @FieldDefinition
    public InAttributeName[] yPositionKey;

    @FieldDefinition
    public InSpatialTableFile[] file;

    @FieldDefinition
    public InAttributeName[] selectKey;

    @FieldDefinition
    public InAttributeName[] groupKey;

    public InFileSelectedGroupedSpatialDistribution2D() {
    }

    public InFileSelectedGroupedSpatialDistribution2D(
            String name,
            InAttributeName xPositionKey,
            InAttributeName yPositionKey,
            InSpatialTableFile file,
            InAttributeName selectKey,
            InAttributeName groupKey) {
        this._name = name;
        setXPositionKey(xPositionKey);
        setYPositionKey(yPositionKey);
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

    public void setXPositionKey(InAttributeName xPositionKey) {
        this.xPositionKey = new InAttributeName[]{xPositionKey};
    }

    public void setYPositionKey(InAttributeName yPositionKey) {
        this.yPositionKey = new InAttributeName[]{yPositionKey};
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
            String xKey,
            String yKey,
            String selectKey,
            String selectValue,
            String groupingKey,
            Rnd rnd) {
        List<List<SpatialAttribute>> selectedList = SpatialUtil.filter(attrList, selectKey, selectValue);
        List<SpatialInformation> infos = SpatialUtil.mapToPoint2D(selectedList, xKey, yKey);
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

        String xKey = getXPositionKey().getName();
        String yKey = getYPositionKey().getName();
        String selectKey = getSelectKey().getName();
        String groupingKey = getGroupKey().getName();
        List<List<SpatialAttribute>> attrList = parser.parseEntityTo(getFile());
        Rnd rnd = parser.deriveRnd();
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "InCustomSelectedSpatialDistribution2D '{}' uses seed: {}", getName(), rnd.getInitialSeed());

        WeightedDiscreteSpatialDistribution dist = createInstance(
                getName(),
                attrList,
                xKey,
                yKey,
                selectKey,
                jCag.getName(),
                groupingKey,
                rnd
        );
        jCag.setSpatialDistribution(dist);
    }

    public InAttributeName getXPositionKey() throws ParsingException {
        return ParamUtil.getInstance(xPositionKey, "XPositionKey");
    }

    public InAttributeName getYPositionKey() throws ParsingException {
        return ParamUtil.getInstance(yPositionKey, "YPositionKey");
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
