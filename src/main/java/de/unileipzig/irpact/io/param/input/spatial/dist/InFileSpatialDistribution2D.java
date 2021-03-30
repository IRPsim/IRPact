package de.unileipzig.irpact.io.param.input.spatial.dist;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialTableFileContent;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.core.spatial.distribution.DiscreteSpatialDistribution;
import de.unileipzig.irpact.core.spatial.distribution.SpatialDistribution;
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

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InFileSpatialDistribution2D implements InSpatialDistribution {

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
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public InAttributeName[] xPositionKey;

    @FieldDefinition
    public InAttributeName[] yPositionKey;

    @FieldDefinition
    public InSpatialTableFile[] attrFile;

    public InFileSpatialDistribution2D() {
    }

    public InFileSpatialDistribution2D(
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

        SpatialTableFileContent attrList = parser.parseEntityTo(getAttributeFile());
        List<SpatialInformation> infos = SpatialUtil.mapToPoint2D(attrList.data(), xKey, yKey);

        DiscreteSpatialDistribution dist = new DiscreteSpatialDistribution();
        dist.setName(getName());
        Rnd rnd = parser.deriveRnd();
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "InCustomSpatialDistribution2D '{}' uses seed: {}", getName(), rnd.getInitialSeed());
        dist.setRandom(rnd);
        dist.addAll(infos);
        parser.cache(this, dist);
        jCag.setSpatialDistribution(dist);
    }
}
