package de.unileipzig.irpact.io.param.input.spatial.dist;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.spatial.data.SpatialDataCollection;
import de.unileipzig.irpact.develop.Todo;
import de.unileipzig.irpact.io.param.input.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Todo("vllt ist es sinnvoller dieses interface nicht als Definition zu deklarieren")
@Definition
public interface InSpatialDistributionWithCollection extends InSpatialDistribution {

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }

    InSpatialTableFile getFile() throws ParsingException;

    SpatialDataCollection parseCollection(IRPactInputParser parser) throws ParsingException;
}
