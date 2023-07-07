package de.unileipzig.irpact.io.param.input.postdata;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.process.ra.InNodeDistanceFilterScheme;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.SETT_RESULT2_NEIGHBORS;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(SETT_RESULT2_NEIGHBORS)
public class InNeighbourhoodOverview implements InPostDataAnalysis {

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

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            defaultValue = TRUE1,
            boolDomain = true
    )
    public boolean enabled = true;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            defaultValue = FALSE0,
            boolDomain = true
    )
    public boolean storeCsv = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            defaultValue = TRUE1,
            boolDomain = true
    )
    public boolean storeXlsx = true;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InNodeDistanceFilterScheme[] nodeFilterScheme;

    public InNeighbourhoodOverview() {
    }

    public InNeighbourhoodOverview(String name) {
        setName(name);
    }

    @Override
    public InNeighbourhoodOverview copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InNeighbourhoodOverview newCopy(CopyCache cache) {
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

    @Override
    public boolean hasSomethingToStore() {
        return isStoreCsv() || isStoreXlsx();
    }

    public InNodeDistanceFilterScheme getNodeFilterScheme() throws ParsingException {
        return ParamUtil.getInstance(nodeFilterScheme, "nodeFilterScheme");
    }

    public void setNodeFilterScheme(InNodeDistanceFilterScheme nodeFilterScheme) {
        if(nodeFilterScheme == null) {
            this.nodeFilterScheme = new InNodeDistanceFilterScheme[0];
        } else {
            this.nodeFilterScheme = new InNodeDistanceFilterScheme[]{nodeFilterScheme};
        }
    }
}
