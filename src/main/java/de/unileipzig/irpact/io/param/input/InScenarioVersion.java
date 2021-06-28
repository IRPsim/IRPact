package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.simulation.BasicVersion;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.experimental.eval.Constant;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * Stores the version of IRPact for which this scenario was created.
 *
 * @author Daniel Abitz
 */
@Definition
public class InScenarioVersion implements InIRPactEntity {

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
        putClassPath(res, thisClass(), INFORMATIONS, thisName());
        addEntry(res, thisClass());
        addEntry(res, thisClass(), "placeholder");
    }

    public static String deriveSetName() {
        return Constants.SET + ParamUtil.getClassNameWithoutClassSuffix(thisClass());
    }

    public String _name;

    @FieldDefinition
    public int placeholder;

    public InScenarioVersion() {
    }

    @Override
    public InScenarioVersion copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InScenarioVersion newCopy(CopyCache cache) {
        InScenarioVersion copy = new InScenarioVersion();
        copy._name = _name;
        return copy;
    }

    public static InScenarioVersion currentVersion() {
        InScenarioVersion current = new InScenarioVersion();
        current.setVersion(IRPact.VERSION_STRING);
        return current;
    }

    public static InScenarioVersion[] currentVersionAsArray() {
        return new InScenarioVersion[] {currentVersion()};
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
