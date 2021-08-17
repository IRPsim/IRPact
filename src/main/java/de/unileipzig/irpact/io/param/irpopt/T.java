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
        name = "t",
        gams = @Gams(
                description = "Einlesen des Optimierungshorizonts",
                identifier = "Simulationshorizont",
                hidden = Constants.TRUE1,
                type = Constants.GAMS_TIMESERIES
        )
)
public class T extends Ii {

        public static void initRes(TreeAnnotationResource res) {
        }
        public static void applyRes(TreeAnnotationResource res) {
        }

        public T() {
        }

        @Override
        public T copy(CopyCache cache) {
                return cache.copyIfAbsent(this, this::newCopy);
        }

        public T newCopy(CopyCache cache) {
                return new T();
        }
}
