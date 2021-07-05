package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.EdnParameter;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.*;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.DEV;
import static de.unileipzig.irpact.io.param.IOConstants.TEST;
import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition
public class InTestData implements InIRPactEntity {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    protected static final String[] value0grp = {"value01", "value02"};
    protected static final String[] value1grp = {"value11", "value12", "value13", "value14"};
    protected static final String[] value2grp = {"value21", "value22", "value23", "value24", "value25", "value26", "value27"};

    protected static final Xor2RuleBuilder builder0 = new Xor2RuleBuilder()
            .withKeyModifier(buildDefaultParameterNameOperator(thisClass()))
            .withKeys(value0grp[0], value0grp[1]);
    protected static final XorWithDefaultRuleBuilder builder1 = new XorWithDefaultRuleBuilder()
            .withKeyModifier(buildDefaultParameterNameOperator(thisClass()))
            .withKeys(value1grp)
            .withDefaultKey(value1grp[0]);
    protected static final XorWithoutUnselectRuleBuilder builder2 = new XorWithoutUnselectRuleBuilder()
            .withKeyModifier(buildDefaultParameterNameOperator(thisClass()))
            .withKeys(value2grp);

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), DEV, TEST, thisName());
        addEntry(res, thisClass(), "testValue1");
        addEntry(res, thisClass(), "testValue2");

        addEntry(res, thisClass(), "value01");
        addEntry(res, thisClass(), "value02");

        addEntry(res, thisClass(), "value11");
        addEntry(res, thisClass(), "value12");
        addEntry(res, thisClass(), "value13");
        addEntry(res, thisClass(), "value14");

        addEntry(res, thisClass(), "value21");
        addEntry(res, thisClass(), "value22");
        addEntry(res, thisClass(), "value23");
        addEntry(res, thisClass(), "value24");
        addEntry(res, thisClass(), "value25");
        addEntry(res, thisClass(), "value26");
        addEntry(res, thisClass(), "value27");

        setDefault(res, thisClass(), "value01", VALUE_TRUE);
        setDefault(res, thisClass(), "value11", VALUE_TRUE);
        setDefault(res, thisClass(), "value21", VALUE_TRUE);

        setRules(res, thisClass(), value0grp, builder0);
        setRules(res, thisClass(), value1grp, builder1);
        setRules(res, thisClass(), value2grp, builder2);
    }

    public String _name;

    @FieldDefinition(
            edn = @EdnParameter(
                    delta = Constants.TRUE1
            )
    )
    public double testValue1;

    @FieldDefinition(
            edn = @EdnParameter(
                    delta = Constants.TRUE1
            )
    )
    public double testValue2;


    @FieldDefinition
    public boolean value01;
    @FieldDefinition
    public boolean value02;

    @FieldDefinition
    public boolean value11;
    @FieldDefinition
    public boolean value12;
    @FieldDefinition
    public boolean value13;
    @FieldDefinition
    public boolean value14;

    @FieldDefinition
    public boolean value21;
    @FieldDefinition
    public boolean value22;
    @FieldDefinition
    public boolean value23;
    @FieldDefinition
    public boolean value24;
    @FieldDefinition
    public boolean value25;
    @FieldDefinition
    public boolean value26;
    @FieldDefinition
    public boolean value27;

    public InTestData() {
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
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
        copy._name = _name;
        copy.testValue1 = testValue1;
        copy.testValue2 = testValue2;
        return copy;
    }
}
