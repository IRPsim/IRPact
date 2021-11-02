package de.unileipzig.irpact.io.param.input.postdata;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.logging.InConsumerAgentCalculationLoggingModule2;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition
public class InBucketAnalyser implements InPostDataAnalysis {

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
        putClassPath(res, thisClass(), InRootUI.SETT_RESULT2_BUCKET);
        addEntryWithDefaultAndDomain(res, thisClass(), "enabled", VALUE_TRUE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "storeCsv", VALUE_FALSE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "storeXlsx", VALUE_TRUE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "bucketRange", asValue(0.1), DOMAIN_G0);
        addEntry(res, thisClass(), "loggingModule");
    }

    public String _name;

    @FieldDefinition
    public boolean enabled = true;

    @FieldDefinition
    public boolean storeCsv = false;

    @FieldDefinition
    public boolean storeXlsx = true;

    @FieldDefinition
    public double bucketRange = 0.1;

    @FieldDefinition
    public InConsumerAgentCalculationLoggingModule2[] loggingModule = new InConsumerAgentCalculationLoggingModule2[0];

    public InBucketAnalyser() {
    }

    public InBucketAnalyser(String name) {
        setName(name);
    }

    @Override
    public InBucketAnalyser copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InBucketAnalyser newCopy(CopyCache cache) {
        return Dev.throwException();
    }

    public void setName(String name) {
        this._name = name;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setStoreCsv(boolean storeCsv) {
        this.storeCsv = storeCsv;
    }

    public boolean isStoreCsv() {
        return storeCsv;
    }

    public void setStoreXlsx(boolean storeXlsx) {
        this.storeXlsx = storeXlsx;
    }

    public boolean isStoreXlsx() {
        return storeXlsx;
    }

    public void setBucketRange(double bucketRange) {
        this.bucketRange = bucketRange;
    }

    public double getBucketRange() {
        return bucketRange;
    }

    @Override
    public boolean hasSomethingToStore() {
        return isStoreCsv() || isStoreXlsx();
    }

    public InConsumerAgentCalculationLoggingModule2 getLoggingModule() throws ParsingException {
        return getInstance(loggingModule, "loggingModule");
    }

    public void setLoggingModule(InConsumerAgentCalculationLoggingModule2 loggingModule) {
        this.loggingModule = new InConsumerAgentCalculationLoggingModule2[]{loggingModule};
    }
}
