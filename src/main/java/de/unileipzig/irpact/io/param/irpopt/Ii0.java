package de.unileipzig.irpact.io.param.irpopt;

import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "ii_0",
        gams = @Gams(
                description = "Einlesen des Optimierungshorizonts",
                identifier = "Simulationshorizont",
                hidden = Constants.TRUE1,
                type = Constants.GAMS_TIMESERIES
        )
)
public class Ii0 implements InIRPactEntity {

        public static void initRes(TreeAnnotationResource res) {
        }
        public static void applyRes(TreeAnnotationResource res) {
        }

        public String _name;

        public Ii0() {
        }

        @Override
        public String getName() {
                throw new UnsupportedOperationException();
        }

        @Override
        public Ii0 copy(CopyCache cache) {
                return cache.copyIfAbsent(this, this::newCopy);
        }

        public Ii0 newCopy(CopyCache cache) {
                return new Ii0();
        }
}
