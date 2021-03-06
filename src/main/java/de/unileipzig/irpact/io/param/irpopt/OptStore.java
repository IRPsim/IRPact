package de.unileipzig.irpact.io.param.irpopt;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "optstore",
        gams = @Gams(
                description = "Einlesen des Speicherhorizonts",
                identifier = "Speicherhorizont",
                hidden = Constants.TRUE1,
                type = Constants.GAMS_TIMESERIES
        )
)
public class OptStore extends Ii {

        @TreeAnnotationResource.Init
        public static void initRes(TreeAnnotationResource res) {
        }
        @TreeAnnotationResource.Apply
        public static void applyRes(TreeAnnotationResource res) {
        }

        public OptStore() {
        }

        @Override
        public OptStore copy(CopyCache cache) {
                return cache.copyIfAbsent(this, this::newCopy);
        }

        public OptStore newCopy(CopyCache cache) {
                return new OptStore();
        }
}
