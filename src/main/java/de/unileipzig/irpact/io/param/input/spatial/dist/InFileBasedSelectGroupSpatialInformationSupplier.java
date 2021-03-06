package de.unileipzig.irpact.io.param.input.spatial.dist;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.spatial.SpatialTableFileContent;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.core.spatial.data.SpatialDataCollection;
import de.unileipzig.irpact.core.spatial.data.SpatialDataFilter;
import de.unileipzig.irpact.core.spatial.distribution.SpatialInformationSupplier;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Objects;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.SPATIAL_DIST_FILE_FILEPOS_SELECTGROUP;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(SPATIAL_DIST_FILE_FILEPOS_SELECTGROUP)
public class InFileBasedSelectGroupSpatialInformationSupplier implements InSpatialDistributionWithCollection {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(LocalizedUiResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(LocalizedUiResource res) {
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InAttributeName[] xPositionKey = new InAttributeName[0];

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InAttributeName[] yPositionKey = new InAttributeName[0];

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InAttributeName[] idKey = new InAttributeName[0];

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InSpatialTableFile[] file = new InSpatialTableFile[0];

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InAttributeName[] selectKey = new InAttributeName[0];

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InAttributeName[] groupKey = new InAttributeName[0];

    public InFileBasedSelectGroupSpatialInformationSupplier() {
    }

    @Override
    public InFileBasedSelectGroupSpatialInformationSupplier copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InFileBasedSelectGroupSpatialInformationSupplier newCopy(CopyCache cache) {
        InFileBasedSelectGroupSpatialInformationSupplier copy = new InFileBasedSelectGroupSpatialInformationSupplier();
        copy.name = name;
        copy.xPositionKey = cache.copyArray(xPositionKey);
        copy.yPositionKey = cache.copyArray(yPositionKey);
        copy.idKey = cache.copyArray(idKey);
        copy.file = cache.copyArray(file);
        copy.selectKey = cache.copyArray(selectKey);
        copy.groupKey = cache.copyArray(groupKey);
        return copy;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
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

    @Override
    public InSpatialTableFile getFile() throws ParsingException {
        return ParamUtil.getInstance(file, "File");
    }

    public void setSelectKey(InAttributeName selectKey) {
        this.selectKey = new InAttributeName[]{selectKey};
    }

    public InAttributeName getSelectKey() throws ParsingException {
        return ParamUtil.getInstance(selectKey, "SelectKey");
    }

    public void setGroupKey(InAttributeName groupKey) {
        this.groupKey = new InAttributeName[]{groupKey};
    }

    public InAttributeName getGroupKey() throws ParsingException {
        return ParamUtil.getInstance(groupKey, "GroupKey");
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
        String selectKey = getSelectKey().getName();
        String groupingKey = getGroupKey().getName();
        Rnd rnd = parser.deriveRnd();

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "{} '{}' uses seed: {}", thisName(), getName(), rnd.getInitialSeed());

        //next step
        SpatialDataCollection dataColl = parseCollection(parser);

        SpatialInformationSupplier supplier = createForSelectValue(
                getName(),
                dataColl,
                selectKey,
                jCag.getName(),
                groupingKey,
                rnd
        );

        jCag.setSpatialDistribution(supplier);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "set '{}' to cag '{}'", supplier.getName(), jCag.getName());
    }

    @Override
    public SpatialDataCollection parseCollection(IRPactInputParser parser) throws ParsingException {
        SpatialTableFileContent fileContent = parser.parseEntityTo(getFile());
        String xKey = getXPositionKey().getName();
        String yKey = getYPositionKey().getName();
        String idKey = getIdKeyName();
        return SpatialUtil.mapToPoint2DIfAbsent_2(
                fileContent.getName(),
                parser.getEnvironment().getSpatialModel(),
                fileContent.content(),
                xKey,
                yKey,
                idKey
        );
    }

    //=========================
    //util
    //=========================

    //creates instance for special selectValue
    public static SpatialInformationSupplier createForSelectValue(
            String name,
            SpatialDataCollection data,
            String selectKey,
            String selectValue,
            String groupingKey,
            Rnd rnd) {
        List<SpatialDataFilter> filters = SpatialUtil.createFilters(
                data,
                selectKey,
                selectValue,
                groupingKey
        );
        SpatialInformationSupplier supplier = new SpatialInformationSupplier();
        supplier.setName(name);
        supplier.setSpatialData(data);
        supplier.setRandom(rnd);
        supplier.addFilters(filters);
        return supplier;
    }
}
