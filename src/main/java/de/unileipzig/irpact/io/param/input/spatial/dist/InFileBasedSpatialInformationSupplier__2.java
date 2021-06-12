package de.unileipzig.irpact.io.param.input.spatial.dist;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.spatial.SpatialTableFileContent;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.core.spatial.data.SpatialDataCollection;
import de.unileipzig.irpact.core.spatial.distribution2.SpatialInformationSupplier;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.develop.TodoException;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.Copyable;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InFileBasedSpatialInformationSupplier__2 implements InSpatialDistribution {

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
        addEntry(res, thisClass(), "xPositionKey");
        addEntry(res, thisClass(), "yPositionKey");
        addEntry(res, thisClass(), "idKey");
        addEntry(res, thisClass(), "attrFile");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public InAttributeName[] xPositionKey;

    @FieldDefinition
    public InAttributeName[] yPositionKey;

    @FieldDefinition
    public InAttributeName[] idKey;

    @FieldDefinition
    public InSpatialTableFile[] file;

    public InFileBasedSpatialInformationSupplier__2() {
    }

    @Override
    public Copyable copy(CopyCache copyCache) {
        throw new TodoException();
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

    public InAttributeName getXPositionKey() throws ParsingException {
        return ParamUtil.getInstance(xPositionKey, "XPositionKey");
    }

    public void setYPositionKey(InAttributeName yPositionKey) {
        this.yPositionKey = new InAttributeName[]{yPositionKey};
    }

    public InAttributeName getYPositionKey() throws ParsingException {
        return ParamUtil.getInstance(yPositionKey, "YPositionKey");
    }

    public void setIdKey(InAttributeName idKey) {
        this.idKey = new InAttributeName[]{idKey};
    }

    public InAttributeName getIdKey() throws ParsingException {
        return ParamUtil.getInstanceOr(idKey, null, "idKey");
    }
    protected String getIdKeyName() throws ParsingException {
        InAttributeName attrName = getIdKey();
        return attrName == null ? null : attrName.getName();
    }

    public void setFile(InSpatialTableFile file) {
        this.file = new InSpatialTableFile[]{file};
    }

    public InSpatialTableFile getFile() throws ParsingException {
        return ParamUtil.getInstance(file, "File");
    }

    @Override
    public void setup(IRPactInputParser parser, Object input) throws ParsingException {
        JadexConsumerAgentGroup jCag = (JadexConsumerAgentGroup) input;

        if(jCag.hasSpatialDistribution()) {
            if(parser.isRestored()) {
                if(Objects.equals(getName(), jCag.getSpatialDistribution().getName())) {
                    return;
                } else {
                    throw new ParsingException(
                            "restored cag '{}' already has spatial distribution '{}' (this: '{}')",
                            jCag.getName(),
                            jCag.getSpatialDistribution().getName(),
                            getName()
                    );
                }
            } else {
                throw new ParsingException(
                        "cag '{}' already has spatial distribution '{}' (this: '{}')",
                        jCag.getName(),
                        jCag.getSpatialDistribution().getName(),
                        getName()
                );
            }
        }

        //raw data
        String xKey = getXPositionKey().getName();
        String yKey = getYPositionKey().getName();
        String idKey = getIdKeyName();
        SpatialTableFileContent fileContent = parser.parseEntityTo(getFile());
        Rnd rnd = parser.deriveRnd();

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "{} '{}' uses seed: {}", thisName(), getName(), rnd.getInitialSeed());

        //next step
        SpatialDataCollection dataColl = SpatialUtil.mapToPoint2DIfAbsent_2(
                fileContent.getName(),
                parser.getEnvironment().getSpatialModel(),
                fileContent.content(),
                xKey,
                yKey,
                idKey
        );

        SpatialInformationSupplier supplier = create(
                getName(),
                dataColl,
                rnd
        );

        //jCag.setSpatialDistribution(supplier);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "set '{}' to cag '{}'", supplier.getName(), jCag.getName());
        Dev.throwException();
    }

    //=========================
    //util
    //=========================

    public static SpatialInformationSupplier create(
            String name,
            SpatialDataCollection data,
            Rnd rnd) {
        SpatialInformationSupplier supplier = new SpatialInformationSupplier();
        supplier.setName(name);
        supplier.setSpatialData(data);
        supplier.setRandom(rnd);
        supplier.addUnfiltered();
        return supplier;
    }
}
