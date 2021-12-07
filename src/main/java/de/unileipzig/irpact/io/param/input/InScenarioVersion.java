package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.simulation.BasicVersion;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.INFO_ABOUTSCENARIO;

/**
 * Stores the version of IRPact for which this scenario was created.
 *
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(INFO_ABOUTSCENARIO)
public class InScenarioVersion implements InIRPactEntity {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(LocalizedUiResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(LocalizedUiResource res) {
    }

    public static String deriveSetName() {
        return Constants.SET + ParamUtil.getClassNameWithoutClassSuffix(thisClass());
    }

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            intDefault = 0
    )
    public int placeholder = 0;

    public InScenarioVersion() {
    }

    @Override
    public InScenarioVersion copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InScenarioVersion newCopy(CopyCache cache) {
        InScenarioVersion copy = new InScenarioVersion();
        copy.name = name;
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
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.name = version.startsWith("v")
                ? version
                : "v" + version;
    }

    public String getVersion() {
        if(name == null) {
            return null;
        } else {
            if(name.startsWith("v")) {
                return name.substring(1);
            } else {
                return name;
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
