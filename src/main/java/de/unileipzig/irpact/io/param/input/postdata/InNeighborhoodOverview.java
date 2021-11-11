package de.unileipzig.irpact.io.param.input.postdata;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.process.ra.InNodeFilterDistanceScheme;
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
public class InNeighborhoodOverview implements InPostDataAnalysis {

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
        putClassPath(res, thisClass(), InRootUI.SETT_RESULT2_NEIGHBORS);
        addEntryWithDefaultAndDomain(res, thisClass(), "enabled", VALUE_TRUE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "storeCsv", VALUE_FALSE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "storeXlsx", VALUE_TRUE, DOMAIN_BOOLEAN);
        addEntry(res, thisClass(), "nodeFilterScheme");
    }

    public String _name;

    @FieldDefinition
    public boolean enabled = true;

    @FieldDefinition
    public boolean storeCsv = false;

    @FieldDefinition
    public boolean storeXlsx = true;

    @FieldDefinition
    public InNodeFilterDistanceScheme[] nodeFilterScheme;

    public InNeighborhoodOverview() {
    }

    public InNeighborhoodOverview(String name) {
        setName(name);
    }

    @Override
    public InNeighborhoodOverview copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InNeighborhoodOverview newCopy(CopyCache cache) {
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

    @Override
    public boolean hasSomethingToStore() {
        return isStoreCsv() || isStoreXlsx();
    }

    public InNodeFilterDistanceScheme getNodeFilterScheme() throws ParsingException {
        return ParamUtil.getInstance(nodeFilterScheme, "nodeFilterScheme");
    }

    public void setNodeFilterScheme(InNodeFilterDistanceScheme nodeFilterScheme) {
        if(nodeFilterScheme == null) {
            this.nodeFilterScheme = new InNodeFilterDistanceScheme[0];
        } else {
            this.nodeFilterScheme = new InNodeFilterDistanceScheme[]{nodeFilterScheme};
        }
    }
}
