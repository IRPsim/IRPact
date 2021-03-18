package de.unileipzig.irpact.io.param.input.spatial.dist;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.core.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.core.spatial.distribution.DiscreteSpatialDistribution;
import de.unileipzig.irpact.core.spatial.distribution.SpatialDistribution;
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

/**
 * @author Daniel Abitz
 */
@Todo("vllt schauen, dass die namen angepasst werden -> damit die datei-abhaengigen direkt erkennbar sind")
@Todo("trennen in eine variante mit datei und eine ohne")
@Definition
public class InSelectedSpatialDistribution2D implements InSpatialDistribution {

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
                InSelectedSpatialDistribution2D.class,
                res.getCachedElement("Räumliche Modell"),
                res.getCachedElement("SpatialDist"),
                res.getCachedElement("CustomPos"),
                res.getCachedElement("InCustomSpatialDistribution2D")
        );
        TreeResourceApplier.callAllSubApplyResSilently(thisClass(), res);
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InSelectedSpatialDistribution2D.class);

    public String _name;

    public static void initRes0(TreeAnnotationResource res) {
    }
    public static void applyRes0(TreeAnnotationResource res) {
        res.newEntryBuilder()
                .setGamsIdentifier("X-Position")
                .setGamsDescription("X-Position")
                .store(InSelectedSpatialDistribution2D.class, "xPosSupplier");
    }
    @FieldDefinition
    public InAttributeName[] xPositionKey;

    public static void initRes1(TreeAnnotationResource res) {
    }
    public static void applyRes1(TreeAnnotationResource res) {
        res.newEntryBuilder()
                .setGamsIdentifier("Y-Position")
                .setGamsDescription("Y-Position")
                .store(InSelectedSpatialDistribution2D.class, "yPosSupplier");
    }
    @FieldDefinition
    public InAttributeName[] yPositionKey;

    public static void initRes2(TreeAnnotationResource res) {
    }
    public static void applyRes2(TreeAnnotationResource res) {
        res.newEntryBuilder()
                .setGamsIdentifier("Tabellendaten")
                .setGamsDescription("Zu nutzende Tabelle für weitere Informationen")
                .store(InSelectedSpatialDistribution2D.class, "attrFile");
    }
    @FieldDefinition
    public InSpatialTableFile[] attrFile;

    public InSelectedSpatialDistribution2D() {
    }

    public InSelectedSpatialDistribution2D(
            String name,
            InAttributeName xPositionKey,
            InAttributeName yPositionKey,
            InSpatialTableFile attrFile) {
        this._name = name;
        setXPositionKey(xPositionKey);
        setYPositionKey(yPositionKey);
        setAttributeFile(attrFile);
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setXPositionKey(InAttributeName xPositionKey) {
        this.xPositionKey = new InAttributeName[]{xPositionKey};
    }

    public InAttributeName getXPositionKey() throws ParsingException {
        return ParamUtil.getInstance(xPositionKey, "XPositionKey");
    }

    public void setYPositionKey(InAttributeName yPositionKey) {
        this.yPositionKey = new InAttributeName[]{yPositionKey};
    }

    public InAttributeName getYPositionKey() throws ParsingException {
        return ParamUtil.getInstance(yPositionKey, "YPositionKey");
    }

    public void setAttributeFile(InSpatialTableFile attrFile) {
        this.attrFile = new InSpatialTableFile[]{attrFile};
    }

    public InSpatialTableFile getAttributeFile() throws ParsingException {
        return ParamUtil.getInstance(attrFile, "AttributeFile");
    }

    @Override
    public void setup(InputParser parser, Object input) throws ParsingException {
        JadexConsumerAgentGroup jCag = (JadexConsumerAgentGroup) input;

        if(parser.isCached(this)) {
            SpatialDistribution dist = (SpatialDistribution) parser.getCached(this);
            jCag.setSpatialDistribution(dist);
            return;
        }

        String xKey = getXPositionKey().getName();
        String yKey = getYPositionKey().getName();

        List<List<SpatialAttribute<?>>> attrList = parser.parseEntityTo(getAttributeFile());
        List<SpatialInformation> infos = SpatialUtil.mapToPoint2D(attrList, xKey, yKey);

        DiscreteSpatialDistribution dist = new DiscreteSpatialDistribution();
        dist.setName(getName());
        Rnd rnd = parser.deriveRnd();
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "InCustomSpatialDistribution2D '{}' uses seed: {}", getName(), rnd.getInitialSeed());
        dist.setRandom(rnd);
        dist.addAll(infos);
        parser.cache(this, dist);
        jCag.setSpatialDistribution(dist);
    }
}
