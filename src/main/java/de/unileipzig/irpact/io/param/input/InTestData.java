package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.EdnParameter;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.function.Consumer;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.DEV_TEST_DATA;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(DEV_TEST_DATA)
@LocalizedUiResource.XorWithoutUnselectRule(InTestData.GRP0)
@LocalizedUiResource.XorWithDefaultRule(InTestData.GRP1)
@LocalizedUiResource.XorWithoutUnselectRule(InTestData.GRP2)
public class InTestData implements InIRPactEntity {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    static final String GRP0 = "GRP0";
    static final String GRP1 = "GRP1";
    static final String GRP2 = "GRP2";

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InTestData.class);

//    protected static final String[] value0grp = {"value01", "value02"};
//    protected static final String[] value1grp = {"value11", "value12", "value13", "value14"};
//    protected static final String[] value2grp = {"value21", "value22", "value23", "value24", "value25", "value26", "value27"};
//
//    protected static final Xor2RuleBuilder builder0 = new Xor2RuleBuilder()
//            .withKeyModifier(buildDefaultParameterNameOperator(thisClass()))
//            .withKeys(value0grp[0], value0grp[1]);
//    protected static final XorWithDefaultRuleBuilder builder1 = new XorWithDefaultRuleBuilder()
//            .withKeyModifier(buildDefaultParameterNameOperator(thisClass()))
//            .withKeys(value1grp)
//            .withDefaultKey(value1grp[0]);
//    protected static final XorWithoutUnselectRuleBuilder builder2 = new XorWithoutUnselectRuleBuilder()
//            .withKeyModifier(buildDefaultParameterNameOperator(thisClass()))
//            .withKeys(value2grp);

    @TreeAnnotationResource.Init
    public static void initRes(LocalizedUiResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(LocalizedUiResource res) {
//        setRules(res, thisClass(), value0grp, builder0);
//        setRules(res, thisClass(), value1grp, builder1);
//        setRules(res, thisClass(), value2grp, builder2);
    }

    @DefinitionName
    public String name;

    @FieldDefinition(
            edn = @EdnParameter(
                    delta = Constants.TRUE1
            )
    )
    @LocalizedUiResource.AddEntry
    public double testValue1;

    @FieldDefinition(
            edn = @EdnParameter(
                    delta = Constants.TRUE1
            )
    )
    @LocalizedUiResource.AddEntry
    public double testValue2;


    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    @LocalizedUiResource.XorWithoutUnselectRuleEntry(GRP0)
    public boolean value01;
    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    @LocalizedUiResource.XorWithoutUnselectRuleEntry(GRP0)
    public boolean value02;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    @LocalizedUiResource.XorWithDefaultRuleEntry(
            value = GRP1,
            isDefault = true
    )
    public boolean value11;
    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    @LocalizedUiResource.XorWithDefaultRuleEntry(GRP1)
    public boolean value12;
    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    @LocalizedUiResource.XorWithDefaultRuleEntry(GRP1)
    public boolean value13;
    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    @LocalizedUiResource.XorWithDefaultRuleEntry(GRP1)
    public boolean value14;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    @LocalizedUiResource.XorWithoutUnselectRuleEntry(GRP2)
    public boolean value21;
    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    @LocalizedUiResource.XorWithoutUnselectRuleEntry(GRP2)
    public boolean value22;
    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    @LocalizedUiResource.XorWithoutUnselectRuleEntry(GRP2)
    public boolean value23;
    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    @LocalizedUiResource.XorWithoutUnselectRuleEntry(GRP2)
    public boolean value24;
    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    @LocalizedUiResource.XorWithoutUnselectRuleEntry(GRP2)
    public boolean value25;
    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    @LocalizedUiResource.XorWithoutUnselectRuleEntry(GRP2)
    public boolean value26;
    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    @LocalizedUiResource.XorWithoutUnselectRuleEntry(GRP2)
    public boolean value27;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double sensi1;
    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double sensi2;
    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double sensi3;

    public InTestData() {
    }

    public InTestData peek(Consumer<? super InTestData> consumer) {
        consumer.accept(this);
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTestValue1() {
        return testValue1;
    }

    public void setTestValue1(double testValue1) {
        this.testValue1 = testValue1;
    }

    public double getTestValue2() {
        return testValue2;
    }

    public void setTestValue2(double testValue2) {
        this.testValue2 = testValue2;
    }

    @Override
    public InTestData copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InTestData newCopy(CopyCache cache) {
        InTestData copy = new InTestData();
        copy.name = name;
        copy.testValue1 = testValue1;
        copy.testValue2 = testValue2;
        return copy;
    }

    @Override
    public Object parse(IRPactInputParser parser) throws ParsingException {
        LOGGER.trace(IRPSection.DEBUG, "TestData '{}' -> sensitivitaet#1: {}", name, sensi1);
        LOGGER.trace(IRPSection.DEBUG, "TestData '{}' -> sensitivitaet#2: {}", name, sensi2);
        LOGGER.trace(IRPSection.DEBUG, "TestData '{}' -> sensitivitaet#3: {}", name, sensi3);
        return null;
    }
}
