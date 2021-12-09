package de.unileipzig.irpact.io.param.input.postdata;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.logging.InConsumerAgentCalculationLoggingModule2;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GamsParameter;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.SETT_RESULT2_BUCKET;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(SETT_RESULT2_BUCKET)
public class InBucketAnalyser implements InPostDataAnalysis {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(TreeAnnotationResource res) {
    }

    @DefinitionName
    public String name;

    @FieldDefinition(
            gams = @GamsParameter(
                    defaultValue = TRUE1,
                    domain = DOMAIN_BOOLEAN
            )
    )
    @LocalizedUiResource.AddEntry
    public boolean enabled = true;

    @FieldDefinition(
            gams = @GamsParameter(
                    defaultValue = FALSE0,
                    domain = DOMAIN_BOOLEAN
            )
    )
    @LocalizedUiResource.AddEntry
    public boolean storeCsv = false;

    @FieldDefinition(
            gams = @GamsParameter(
                    defaultValue = TRUE1,
                    domain = DOMAIN_BOOLEAN
            )
    )
    @LocalizedUiResource.AddEntry
    public boolean storeXlsx = true;

    @FieldDefinition(
            gams = @GamsParameter(
                    defaultValue = "0.1",
                    domain = DOMAIN_G0
            )
    )
    @LocalizedUiResource.AddEntry
    public double bucketRange = 0.1;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
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
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
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
