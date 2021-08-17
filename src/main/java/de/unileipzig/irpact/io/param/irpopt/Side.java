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
        name = "side",
        gams = @Gams(
                description = "Marktteilnehmer",
                identifier = "MT",
                hidden = Constants.TRUE1
        )
)
public class Side implements InIRPactEntity {

        public static void initRes(TreeAnnotationResource res) {
        }
        public static void applyRes(TreeAnnotationResource res) {
        }

        public String _name;

        public Side() {
        }

        @Override
        public String getName() {
                return _name;
        }

        @Override
        public Side copy(CopyCache cache) {
                return cache.copyIfAbsent(this, this::newCopy);
        }

        public Side newCopy(CopyCache cache) {
                return new Side();
        }
}
