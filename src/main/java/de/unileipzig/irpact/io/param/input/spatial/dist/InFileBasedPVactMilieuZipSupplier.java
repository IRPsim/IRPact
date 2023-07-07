package de.unileipzig.irpact.io.param.input.spatial.dist;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.SpatialTableFileContent;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.core.spatial.data.SpatialDataCollection;
import de.unileipzig.irpact.core.spatial.distribution.SpatialInformationSupplier;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.SPATIAL_DIST_FILE_FILEPOS_PVMILIEUZIP;

/**
 * @author Daniel Abitz
 */
@Definition(ignore = true)
@LocalizedUiResource.Ignore
@LocalizedUiResource.PutClassPath(SPATIAL_DIST_FILE_FILEPOS_PVMILIEUZIP)
public class InFileBasedPVactMilieuZipSupplier implements InSpatialDistributionWithCollection {

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
    public InSpatialTableFile[] file = new InSpatialTableFile[0];

    public InFileBasedPVactMilieuZipSupplier() {
    }

    @Override
    public InFileBasedPVactMilieuZipSupplier copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InFileBasedPVactMilieuZipSupplier newCopy(CopyCache cache) {
        InFileBasedPVactMilieuZipSupplier copy = new InFileBasedPVactMilieuZipSupplier();
        copy.name = name;
        copy.file = cache.copyArray(file);
        return copy;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setFile(InSpatialTableFile file) {
        this.file = new InSpatialTableFile[]{file};
    }

    @Override
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
        Rnd rnd = parser.deriveRnd();

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "{} '{}' uses seed: {}", thisName(), getName(), rnd.getInitialSeed());

        //next step
        SpatialDataCollection dataColl = parseCollection(parser);

        SpatialInformationSupplier supplier = InFileBasedSelectGroupSpatialInformationSupplier.createForSelectValue(
                getName(),
                dataColl,
                RAConstants.DOM_MILIEU,
                jCag.getName(),
                RAConstants.ZIP,
                rnd
        );

        jCag.setSpatialDistribution(supplier);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "set '{}' to cag '{}'", supplier.getName(), jCag.getName());
    }

    @Override
    public SpatialDataCollection parseCollection(IRPactInputParser parser) throws ParsingException {
        SpatialTableFileContent fileContent = parser.parseEntityTo(getFile());
        return SpatialUtil.mapToPoint2DIfAbsent_2(
                fileContent.getName(),
                parser.getEnvironment().getSpatialModel(),
                fileContent.content(),
                RAConstants.X_CENT,
                RAConstants.Y_CENT,
                RAConstants.ID
        );
    }
}
