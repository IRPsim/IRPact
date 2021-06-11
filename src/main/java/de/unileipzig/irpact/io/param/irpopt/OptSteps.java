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
        name = "optsteps",
        gams = @Gams(
                description = "Einlesen des Optimierungszeitpunkte",
                identifier = "Speicherhorizont",
                hidden = Constants.TRUE1
        )
)
public class OptSteps extends Ii {

        public static void initRes(TreeAnnotationResource res) {
        }
        public static void applyRes(TreeAnnotationResource res) {
        }

        public OptSteps() {
        }

        @Override
        public OptSteps copy(CopyCache cache) {
                return cache.copyIfAbsent(this, this::newCopy);
        }

        public OptSteps newCopy(CopyCache cache) {
                return new OptSteps();
        }
}