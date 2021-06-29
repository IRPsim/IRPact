package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.EdnParameter;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

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

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), DEV, TEST, thisName());
        addEntry(res, thisClass(), "testValue1");
        addEntry(res, thisClass(), "testValue2");
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
