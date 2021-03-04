package de.unileipzig.irpact.io.param.input.spatial.dist;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.process.ra.RAModelData;
import de.unileipzig.irpact.core.process.ra.RAProcessPlan;
import de.unileipzig.irpact.core.spatial.DiscreteSpatialDistribution;
import de.unileipzig.irpact.core.spatial.SpatialDistribution;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.core.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.core.spatial.twodim.SuppliedSpatialDistribution2D;
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
public class InCustomSpatialDistribution2D implements InSpatialDistribution {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InCustomSpatialDistribution2D.class,
                res.getCachedElement("Räumliche Modell"),
                res.getCachedElement("SpatialDist"),
                res.getCachedElement("CustomPos"),
                res.getCachedElement("InCustomSpatialDistribution2D")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("X-Position")
                .setGamsDescription("X-Position")
                .store(InCustomSpatialDistribution2D.class, "xPosSupplier");

        res.newEntryBuilder()
                .setGamsIdentifier("Y-Position")
                .setGamsDescription("Y-Position")
                .store(InCustomSpatialDistribution2D.class, "yPosSupplier");

        res.newEntryBuilder()
                .setGamsIdentifier("Tabellendaten")
                .setGamsDescription("Zu nutzende Tabelle für weitere Informationen")
                .store(InCustomSpatialDistribution2D.class, "attrFile");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InCustomSpatialDistribution2D.class);

    public String _name;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] xPosSupplier;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] yPosSupplier;

    @FieldDefinition
    public InSpatialTableFile[] attrFile;

    public InCustomSpatialDistribution2D() {
    }

    public InCustomSpatialDistribution2D(
            String name,
            InUnivariateDoubleDistribution xPosSupplier,
            InUnivariateDoubleDistribution yPosSupplier,
            InSpatialTableFile attrFile) {
        this._name = name;
        this.xPosSupplier = new InUnivariateDoubleDistribution[] {xPosSupplier};
        this.yPosSupplier = new InUnivariateDoubleDistribution[] {yPosSupplier};
        this.attrFile = new InSpatialTableFile[] {attrFile};
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public InUnivariateDoubleDistribution getXPosSupplier() throws ParsingException {
        return ParamUtil.getInstance(xPosSupplier, "XPosSupplier");
    }

    public void setXPosSupplier(InUnivariateDoubleDistribution xPosSupplier) {
        this.xPosSupplier = new InUnivariateDoubleDistribution[]{xPosSupplier};
    }

    public InUnivariateDoubleDistribution getYPosSupplier() throws ParsingException {
        return ParamUtil.getInstance(yPosSupplier, "YPosSupplier");
    }

    public void setYPosSupplier(InUnivariateDoubleDistribution yPosSupplier) {
        this.yPosSupplier = new InUnivariateDoubleDistribution[]{yPosSupplier};
    }

    public InSpatialTableFile getAttributeFile() throws ParsingException {
        return ParamUtil.getInstance(attrFile, "AttributeFile");
    }

    public void setAttributeFile(InSpatialTableFile attrFile) {
        this.attrFile = new InSpatialTableFile[]{attrFile};
    }

    @Override
    public void setup(InputParser parser, Object input) throws ParsingException {
        JadexConsumerAgentGroup jCag = (JadexConsumerAgentGroup) input;

        if(parser.isCached(this)) {
            SpatialDistribution dist = (SpatialDistribution) parser.getCached(this);
            jCag.setSpatialDistribution(dist);
            return;
        }

        UnivariateDoubleDistribution xDist = parser.parseEntityTo(getXPosSupplier());
        UnivariateDoubleDistribution yDist = parser.parseEntityTo(getYPosSupplier());

        if(getAttributeFile() == null) {
            SuppliedSpatialDistribution2D dist = new SuppliedSpatialDistribution2D();
            dist.setName(getName());
            dist.setXSupplier(xDist);
            dist.setYSupplier(yDist);
            parser.cache(this, dist);
            jCag.setSpatialDistribution(dist);
        } else {
            List<List<SpatialAttribute<?>>> attrList = parser.parseEntityTo(getAttributeFile());
            List<SpatialInformation> infos = SpatialUtil.mapToPoint2D(attrList, xDist, yDist);

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
}
