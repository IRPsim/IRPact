package de.unileipzig.irpact.io.param.input.spatial.dist;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialTableFileContent;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.core.spatial.distribution.DiscreteSpatialDistribution;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.List;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.IOConstants.SPATIAL_MODEL_DIST_FILE_FILEPOS;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InFileSelectedSpatialDistribution2D implements InSpatialDistribution {

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
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public InAttributeName[] xPositionKey;

    @FieldDefinition
    public InAttributeName[] yPositionKey;

    @FieldDefinition
    public InSpatialTableFile[] attrFile;

    @FieldDefinition
    public InAttributeName[] selectKey;

    public InFileSelectedSpatialDistribution2D() {
    }

    public InFileSelectedSpatialDistribution2D(
            String name,
            InAttributeName xPositionKey,
            InAttributeName yPositionKey,
            InSpatialTableFile file,
            InAttributeName selectKey) {
        setName(name);
        setXPositionKey(xPositionKey);
        setYPositionKey(yPositionKey);
        setAttributeFile(file);
        setSelectKey(selectKey);
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

    public InSpatialTableFile getAttributeFile() throws ParsingException {
        return ParamUtil.getInstance(attrFile, "AttributeFile");
    }

    public void setAttributeFile(InSpatialTableFile attrFile) {
        this.attrFile = new InSpatialTableFile[]{attrFile};
    }

    public InAttributeName getSelectKey() throws ParsingException {
        return ParamUtil.getInstance(selectKey, "SelectKey");
    }

    public void setSelectKey(InAttributeName selectKey) {
        this.selectKey = new InAttributeName[]{selectKey};
    }

    public void setToAll(InConsumerAgentGroup... cags) {
        for(InConsumerAgentGroup cag: cags) {
            cag.setSpatialDistribution(this);
        }
    }

    @Override
    public void setup(InputParser parser, Object input) throws ParsingException {
        JadexConsumerAgentGroup jCag = (JadexConsumerAgentGroup) input;

        String xKey = getXPositionKey().getName();
        String yKey = getYPositionKey().getName();
        String selectKey = getSelectKey().getName();
        SpatialTableFileContent attrList = parser.parseEntityTo(getAttributeFile());
        List<List<SpatialAttribute>> selectedList = SpatialUtil.filter(attrList.content().listTable(), selectKey, jCag.getName());
        List<SpatialInformation> infos = SpatialUtil.mapToPoint2D(selectedList, xKey, yKey);


        DiscreteSpatialDistribution dist = new DiscreteSpatialDistribution();
        dist.setName(ParamUtil.concData(jCag.getName(), getName()));
        Rnd rnd = parser.deriveRnd();
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "InFileSelectedSpatialDistribution2D '{}' uses seed: {}", dist.getName(), rnd.getInitialSeed());
        dist.setRandom(rnd);
        dist.addAll(infos);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "InFileSelectedSpatialDistribution2D '{}' has '{}' entries", dist.getName(), infos.size());
        jCag.setSpatialDistribution(dist);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "InFileSelectedSpatialDistribution2D '{}' is linked to cag '{}'", dist.getName(), jCag.getName());
    }
}
