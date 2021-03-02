package de.unileipzig.irpact.io.param.input.spatial.dist;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.spatial.DiscreteSpatialDistribution;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.core.spatial.attribute.SpatialAttribute;
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

/**
 * @author Daniel Abitz
 */
@Definition
public class InCustomSelectedSpatialDistribution2D implements InSpatialDistribution {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InCustomSelectedSpatialDistribution2D.class,
                res.getCachedElement("Räumliche Modell"),
                res.getCachedElement("SpatialDist"),
                res.getCachedElement("CustomPos"),
                res.getCachedElement("InCustomSelectedSpatialDistribution2D")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("X-Position0")
                .setGamsDescription("X-Position")
                .store(InCustomSelectedSpatialDistribution2D.class, "xPosSupplier");

        res.newEntryBuilder()
                .setGamsIdentifier("Y-Position0")
                .setGamsDescription("Y-Position")
                .store(InCustomSelectedSpatialDistribution2D.class, "yPosSupplier");

        res.newEntryBuilder()
                .setGamsIdentifier("Tabellendaten0")
                .setGamsDescription("Zu nutzende Tabelle für weitere Informationen")
                .store(InCustomSelectedSpatialDistribution2D.class, "attrFile");

        res.newEntryBuilder()
                .setGamsIdentifier("Filterschlüssel0")
                .setGamsDescription("Dieser Schlüssel wird verwendet, um die Daten zu filtern (z.B. Milieu).")
                .store(InCustomSelectedSpatialDistribution2D.class, "selectKey");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InCustomSelectedSpatialDistribution2D.class);

    public String _name;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] xPosSupplier;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] yPosSupplier;

    @FieldDefinition
    public InSpatialTableFile[] attrFile;

    @FieldDefinition
    public InAttributeName[] selectKey;

    public InCustomSelectedSpatialDistribution2D() {
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public void setup(InputParser parser, Object input) throws ParsingException {
        JadexConsumerAgentGroup jCag = (JadexConsumerAgentGroup) input;

        UnivariateDoubleDistribution xSupplier = parser.parseEntityTo(getXPosSupplier());
        UnivariateDoubleDistribution ySupplier = parser.parseEntityTo(getYPosSupplier());
        String selectKey = getSelectKey().getName();
        List<List<SpatialAttribute<?>>> attrList = parser.parseEntityTo(getAttributeFile());
        List<List<SpatialAttribute<?>>> selectedList = SpatialUtil.filter(attrList, selectKey, jCag.getName());
        List<SpatialInformation> infos = SpatialUtil.mapToPoint2D(selectedList, xSupplier, ySupplier);

        DiscreteSpatialDistribution dist = new DiscreteSpatialDistribution();
        dist.setName(getName());
        Rnd rnd = parser.deriveRnd();
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "InCustomSelectedSpatialDistribution2D '{}' uses seed: {}", getName(), rnd.getInitialSeed());
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
