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
        name = "optinitial",
        gams = @Gams(
                description = "Einlesen des Initialisierungszeitpunkt des Optimierungshorizonts",
                identifier = "Initialisierungszeitpunkt des Optimierungshorizonts",
                hidden = Constants.TRUE1
        )
)
public class OptInitial extends OptStore {

        @TreeAnnotationResource.Init
        public static void initRes(TreeAnnotationResource res) {
        }
        @TreeAnnotationResource.Apply
        public static void applyRes(TreeAnnotationResource res) {
        }

        public OptInitial() {
        }

        @Override
        public OptInitial copy(CopyCache cache) {
                return cache.copyIfAbsent(this, this::newCopy);
        }

        public OptInitial newCopy(CopyCache cache) {
                return new OptInitial();
        }
}
