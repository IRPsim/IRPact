package de.unileipzig.irpact.io.param.input.spatial.dist;

import de.unileipzig.irpact.commons.Rnd;
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
import de.unileipzig.irpact.util.Todo;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.TreeResourceApplier;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
@Todo("einbinden - persi und spec")
@Todo("special PVact Variante einbauen -> nurnoch Datei auswählen")
@Definition
public class InSelectedGroupedSpatialDistribution2D implements InSpatialDistribution {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
        TreeResourceApplier.callAllSubInitResSilently(thisClass(), res);
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                thisClass(),
                res.getCachedElement("Räumliche Modell"),
                res.getCachedElement("SpatialDist"),
                res.getCachedElement("CustomPos"),
                res.getCachedElement("InCustomSelectedGroupedSpatialDistribution2D")
        );
        TreeResourceApplier.callAllSubApplyResSilently(thisClass(), res);
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    public static void initRes0(TreeAnnotationResource res) {
    }
    public static void applyRes0(TreeAnnotationResource res) {
        res.newEntryBuilder()
                .setGamsIdentifier("X-Position1")
                .setGamsDescription("X-Position")
                .store(thisClass(), "xPosSupplier");
    }
    @FieldDefinition
    public InAttributeName[] xPositionKey;

    public static void initRes1(TreeAnnotationResource res) {
    }
    public static void applyRes1(TreeAnnotationResource res) {
        res.newEntryBuilder()
                .setGamsIdentifier("Y-Position1")
                .setGamsDescription("Y-Position")
                .store(thisClass(), "yPosSupplier");
    }
    @FieldDefinition
    public InAttributeName[] yPositionKey;

    public static void initRes2(TreeAnnotationResource res) {
    }
    public static void applyRes2(TreeAnnotationResource res) {
        res.newEntryBuilder()
                .setGamsIdentifier("Tabellendaten1")
                .setGamsDescription("Zu nutzende Tabelle für weitere Informationen")
                .store(thisClass(), "file");
    }
    @FieldDefinition
    public InSpatialTableFile[] file;

    public static void initRes3(TreeAnnotationResource res) {
    }
    public static void applyRes3(TreeAnnotationResource res) {
        res.newEntryBuilder()
                .setGamsIdentifier("Filterschlüssel1")
                .setGamsDescription("Dieser Schlüssel wird verwendet, um die Daten zu filtern (z.B. Milieu).")
                .store(thisClass(), "selectKey");
    }
    @FieldDefinition
    public InAttributeName[] selectKey;

    public static void initRes4(TreeAnnotationResource res) {
    }
    public static void applyRes4(TreeAnnotationResource res) {
        res.newEntryBuilder()
                .setGamsIdentifier("Gruppierungsschlüssel1")
                .setGamsDescription("Dieser Schlüssel wird verwendet, um die Daten zu für die Wichtung zu gruppieren.")
                .store(thisClass(), "groupKey");
    }
    @FieldDefinition
    public InAttributeName[] groupKey;

    public InSelectedGroupedSpatialDistribution2D() {
    }

    public InSelectedGroupedSpatialDistribution2D(
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
            List<List<SpatialAttribute<?>>> attrList,
            String xKey,
            String yKey,
            String selectKey,
            String selectValue,
            String groupingKey,
            Rnd rnd) {
        List<List<SpatialAttribute<?>>> selectedList = SpatialUtil.filter(attrList, selectKey, selectValue);
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
        List<List<SpatialAttribute<?>>> attrList = parser.parseEntityTo(getFile());
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
