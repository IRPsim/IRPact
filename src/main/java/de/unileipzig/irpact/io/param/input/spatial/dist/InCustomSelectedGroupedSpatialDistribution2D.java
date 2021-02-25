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
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
@Definition
public class InCustomSelectedGroupedSpatialDistribution2D implements InSpatialDistribution {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InCustomSelectedGroupedSpatialDistribution2D.class,
                res.getCachedElement("Räumliche Modell"),
                res.getCachedElement("Verteilungsfunktionen"),
                res.getCachedElement("CustomPos"),
                res.getCachedElement("InCustomSelectedGroupedSpatialDistribution2D")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("X-Position1")
                .setGamsDescription("X-Position")
                .store(InCustomSelectedGroupedSpatialDistribution2D.class, "xPosSupplier");

        res.newEntryBuilder()
                .setGamsIdentifier("Y-Position1")
                .setGamsDescription("Y-Position")
                .store(InCustomSelectedGroupedSpatialDistribution2D.class, "yPosSupplier");

        res.newEntryBuilder()
                .setGamsIdentifier("Tabellendaten1")
                .setGamsDescription("Zu nutzende Tabelle für weitere Informationen")
                .store(InCustomSelectedGroupedSpatialDistribution2D.class, "file");

        res.newEntryBuilder()
                .setGamsIdentifier("Filterschlüssel1")
                .setGamsDescription("Dieser Schlüssel wird verwendet, um die Daten zu filtern (z.B. Milieu).")
                .store(InCustomSelectedGroupedSpatialDistribution2D.class, "selectKey");

        res.newEntryBuilder()
                .setGamsIdentifier("Gruppierungsschlüssel1")
                .setGamsDescription("Dieser Schlüssel wird verwendet, um die Daten zu für die Wichtung zu gruppieren.")
                .store(InCustomSelectedGroupedSpatialDistribution2D.class, "groupKey");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InCustomSelectedGroupedSpatialDistribution2D.class);

    public String _name;

    @FieldDefinition
    public InUnivariateDoubleDistribution xPosSupplier;

    @FieldDefinition
    public InUnivariateDoubleDistribution yPosSupplier;

    @FieldDefinition
    public InSpatialTableFile file;

    @FieldDefinition
    public InAttributeName selectKey;

    @FieldDefinition
    public InAttributeName groupKey;

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
        this.xPosSupplier = xPosSupplier;
        this.yPosSupplier = yPosSupplier;
        this.file = file;
        this.selectKey = selectKey;
        this.groupKey = groupKey;
    }

    public void setName(String name) {
        this._name = name;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setXPosSupplier(InUnivariateDoubleDistribution xPosSupplier) {
        this.xPosSupplier = xPosSupplier;
    }

    public void setYPosSupplier(InUnivariateDoubleDistribution yPosSupplier) {
        this.yPosSupplier = yPosSupplier;
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

    public InUnivariateDoubleDistribution getXPosSupplier() {
        return xPosSupplier;
    }

    public InUnivariateDoubleDistribution getYPosSupplier() {
        return yPosSupplier;
    }

    public InSpatialTableFile getFile() {
        return file;
    }

    public InAttributeName getSelectKey() {
        return selectKey;
    }

    public InAttributeName getGroupKey() {
        return groupKey;
    }
}
