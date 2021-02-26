package de.unileipzig.irpact.io.param.input.spatial.dist;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.core.spatial.WeightedDiscreteSpatialDistribution;
import de.unileipzig.irpact.core.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.input.InUtil;
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

/**
 * @author Daniel Abitz
 */
@Definition
public class InCustomSelectedGroupedSpatialDistribution2D implements InSpatialDistribution {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                thisClass(),
                res.getCachedElement("Räumliche Modell"),
                res.getCachedElement("SpatialDist"),
                res.getCachedElement("CustomPos"),
                res.getCachedElement("InCustomSelectedGroupedSpatialDistribution2D")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("X-Position1")
                .setGamsDescription("X-Position")
                .store(thisClass(), "xPosSupplier");

        res.newEntryBuilder()
                .setGamsIdentifier("Y-Position1")
                .setGamsDescription("Y-Position")
                .store(thisClass(), "yPosSupplier");

        res.newEntryBuilder()
                .setGamsIdentifier("Tabellendaten1")
                .setGamsDescription("Zu nutzende Tabelle für weitere Informationen")
                .store(thisClass(), "file");

        res.newEntryBuilder()
                .setGamsIdentifier("Filterschlüssel1")
                .setGamsDescription("Dieser Schlüssel wird verwendet, um die Daten zu filtern (z.B. Milieu).")
                .store(thisClass(), "selectKey");

        res.newEntryBuilder()
                .setGamsIdentifier("Gruppierungsschlüssel1")
                .setGamsDescription("Dieser Schlüssel wird verwendet, um die Daten zu für die Wichtung zu gruppieren.")
                .store(thisClass(), "groupKey");
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

    public InCustomSelectedGroupedSpatialDistribution2D() {
    }

    public InCustomSelectedGroupedSpatialDistribution2D(
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
            List<List<SpatialAttribute<?>>> attrList,
            UnivariateDoubleDistribution xSupplier,
            UnivariateDoubleDistribution ySupplier,
            String selectKey,
            String selectValue,
            String groupingKey,
            Rnd rnd) {
        List<List<SpatialAttribute<?>>> selectedList = SpatialUtil.filter(attrList, selectKey, selectValue);
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
        List<List<SpatialAttribute<?>>> attrList = parser.parseEntityTo(getFile());
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
        return InUtil.getInstance(xPosSupplier, "XPosSupplier");
    }

    public InUnivariateDoubleDistribution getYPosSupplier() throws ParsingException {
        return InUtil.getInstance(yPosSupplier, "YPosSupplier");
    }

    public InSpatialTableFile getFile() throws ParsingException {
        return InUtil.getInstance(file, "File");
    }

    public InAttributeName getSelectKey() throws ParsingException {
        return InUtil.getInstance(selectKey, "SelectKey");
    }

    public InAttributeName getGroupKey() throws ParsingException {
        return InUtil.getInstance(groupKey, "GroupKey");
    }
}
