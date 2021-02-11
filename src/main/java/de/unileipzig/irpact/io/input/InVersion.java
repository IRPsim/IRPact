package de.unileipzig.irpact.io.input;

import de.unileipzig.irpact.start.IRPact;
import de.unileipzig.irpact.start.Preloader;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * Stores the current Version of IRPact.
 *
 * @author Daniel Abitz
 */
@Definition
public class InVersion {

    public static void initRes(TreeAnnotationResource res) {
//        res.newElementBuilder()
//                .setEdnLabel("Version")
//                .setEdnPriority(0)
//                .setGamsHidden(true)
//                .putCache("Version");
    }
    public static void applyRes(TreeAnnotationResource res) {
//        res.putPath(
//                InVersion.class,
//                res.getCachedElement("Allgemeine Einstellungen"),
//                res.getCachedElement("Version")
//        );
        res.newEntryBuilder()
                .setGamsDescription("Version von IRPact für dieses Szenario.")
                .setGamsIdentifier("InVersion")
                .setGamsHidden(true)
                .store(InVersion.class);
    }

    public String _name;

    @FieldDefinition
    public int placeholderVersion;

    public InVersion() {
    }

    public InVersion(String verion) {
        this._name = verion;
    }

    public static InVersion currentVersion() {
        return new InVersion(IRPact.VERSION);
    }

    public String getVersion() {
        return _name;
    }
}
