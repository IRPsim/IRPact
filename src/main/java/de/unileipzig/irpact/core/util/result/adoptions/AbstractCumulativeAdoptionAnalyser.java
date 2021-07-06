package de.unileipzig.irpact.core.util.result.adoptions;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.data.VarCollection;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractCumulativeAdoptionAnalyser extends AbstractAdoptionAnalyser {

    protected Collection<? extends Integer> years;
    protected boolean printBoth = false;

    protected AbstractCumulativeAdoptionAnalyser() {
        super();
    }

    public AbstractCumulativeAdoptionAnalyser(VarCollection data) {
        super(data);
    }

    public void setYears(Collection<? extends Integer> years) {
        this.years = years;
    }

    public void setPrintBoth(boolean printBoth) {
        this.printBoth = printBoth;
    }

    public boolean isPrintBoth() {
        return printBoth;
    }

    @Override
    protected List<Object> arrayToList(Object[] arr) {
        if(printBoth) {
            List<Object> list = CollectionUtil.arrayListOf(arr);
            list.add(arr[arr.length - 1]);
            return list;
        } else {
            return Arrays.asList(arr);
        }
    }

    @Override
    public boolean add(AdoptionEntry info) {
        if(info.getAdoptedProduct().isInitial()) {
            return false;
        }

        for(Integer year: years) {
            if(year >= info.getYear()) {
                add(year == info.getYear(), year, info);
            }
        }
        return true;
    }

    protected abstract void add(boolean currentYear, int year, AdoptionEntry info);
}
