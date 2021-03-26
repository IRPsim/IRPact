package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.simulation.BasicVersion;
import de.unileipzig.irpact.start.IRPact;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;

/**
 * Stores the current Version of IRPact.
 *
 * @author Daniel Abitz
 */
@Definition
public class InVersion implements InEntity {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        addEntry(res, thisClass());
        addEntry(res, thisClass(), "placeholderVersion");
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
        return new InVersion(IRPact.VERSION_STRING);
    }

    public static InVersion[] currentVersionAsArray() {
        return new InVersion[] {currentVersion()};
    }

    @Override
    public String getName() {
        return _name;
    }

    public String getVersion() {
        return _name;
    }

    @Override
    public BasicVersion parse(InputParser parser) throws ParsingException {
        return new BasicVersion(getVersion());
    }
}
