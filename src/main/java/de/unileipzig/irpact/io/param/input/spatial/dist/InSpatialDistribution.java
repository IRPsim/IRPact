package de.unileipzig.irpact.io.param.input.spatial.dist;

import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irpact.util.Todo;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
/*
Pattern:
    Custom -> x+y via distribution
    FileRelated (?) -> braucht eingabedatei
    Mapped -> Mapping-Key fuer Agentengruppen
    Weighted -> Gruppierung fuer Gewichtungen

    //custom x+y
    CustomFileRelatedMappedWeightedSpatialDistribution2D -> benoetigt x+y, Mapping und Wichtung
    CustomFileRelatedMappedSpatialDistribution2D -> benoetigt x+y, fuer Mapping nach Milieu
    CustomFileRelatedSpatialDistribution2D -> benoetigt x+y, alle Agenten
    CustomSpatialDistribution2D -> benoetigt x+y, hier muessen alle Attribute beim Agenten sein

    //alles aus datei
    FileRelatedMappedWeightedSpatialDistribution2D -> benoetigt x+y, Mapping und Wichtung
    FileRelatedMappedSpatialDistribution2D -> benoetigt x+y, fuer Mapping nach Milieu
    FileRelatedSpatialDistribution2D -> benoetigt x+y, alle Agenten
 */
@Todo("PATTERN ANWENDEN")
@Definition
public interface InSpatialDistribution extends InEntity {

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }
}
