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
        name = "jj",
        gams = @Gams(
                description = "Einlesen der Monate",
                identifier = "Monate",
                hidden = Constants.TRUE1
        )
)
public class Jj implements InIRPactEntity {

        @TreeAnnotationResource.Init
        public static void initRes(TreeAnnotationResource res) {
        }
        @TreeAnnotationResource.Apply
        public static void applyRes(TreeAnnotationResource res) {
        }

        public String _name;

        public Jj() {
        }

        @Override
        public String getName() {
                throw new UnsupportedOperationException();
        }

        @Override
        public Jj copy(CopyCache cache) {
                return cache.copyIfAbsent(this, this::newCopy);
        }

        public Jj newCopy(CopyCache cache) {
                return new Jj();
        }
}
