package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.simulation.BasicVersion;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.setHidden;

/**
 * Stores the current Version of IRPact.
 *
 * @author Daniel Abitz
 */
@Definition
public class InVersion implements InIRPactEntity {

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
        setHidden(res, thisClass());
    }

    public String _name;

    @FieldDefinition
    public int placeholderVersion;

    public InVersion() {
    }

    @Override
    public InVersion copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InVersion newCopy(CopyCache cache) {
        InVersion copy = new InVersion();
        copy._name = _name;
        return copy;
    }

    public static InVersion currentVersion() {
        InVersion current = new InVersion();
        current.setVersion(IRPact.VERSION_STRING);
        return current;
    }

    public static InVersion[] currentVersionAsArray() {
        return new InVersion[] {currentVersion()};
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setVersion(String version) {
        this._name = version.startsWith("v")
                ? version
                : "v" + version;
    }

    public String getVersion() {
        if(_name == null) {
            return null;
        } else {
            if(_name.startsWith("v")) {
                return _name.substring(1);
            } else {
                return _name;
            }
        }
    }

    @Override
    public BasicVersion parse(IRPactInputParser parser) throws ParsingException {
        BasicVersion version = new BasicVersion();
        version.set(getVersion());
        return version;
    }
}
