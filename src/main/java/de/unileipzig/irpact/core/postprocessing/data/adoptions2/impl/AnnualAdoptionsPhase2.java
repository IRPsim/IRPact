package de.unileipzig.irpact.core.postprocessing.data.adoptions2.impl;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.AbstractAnnualAdoptionsX1;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.AdoptionEntry2;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.AdoptionResultInfo2;
import de.unileipzig.irpact.core.util.AdoptionPhase;

import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public class AnnualAdoptionsPhase2 extends AbstractAnnualAdoptionsX1<AdoptionPhase> {

    public static final int INDEX_YEAR = 0;
    public static final int INDEX_PHASE = 1;
    public static final int INDEX_DATA = 2;

    public AnnualAdoptionsPhase2() {
    }

    public static Integer getYear(Object[] entry) {
        return (Integer) entry[INDEX_YEAR];
    }

    public static AdoptionPhase getPhase(Object[] entry) {
        return (AdoptionPhase) entry[INDEX_PHASE];
    }

    public static AdoptionResultInfo2 getData(Object[] entry) {
        return (AdoptionResultInfo2) entry[INDEX_DATA];
    }

    @Override
    protected Class<AdoptionPhase> getFirstType() {
        return AdoptionPhase.class;
    }

    @Override
    public String getTypeForLocalization() {
        return "phase";
    }

    @Override
    protected Function<? super Object[], ? extends Attribute[]> getMappingFunction() {
        return entry -> new Attribute[] {
                toDoubleAttribute(entry[INDEX_YEAR], IF_NULL_DOUBLE),
                toStringAttribute(printPhase(getPhase(entry), IF_NULL_STR), IF_NULL_STR),
                toDoubleAttribute(getData(entry).getValue(), IF_NULL_DOUBLE),
                toDoubleAttribute(getData(entry).getCumulativeValue(), IF_NULL_DOUBLE)
        };
    }

    @Override
    protected Function<? super Object[], ? extends String[]> getStringMappingFunction() {
        return entry -> new String[] {
                print(entry[INDEX_YEAR], IF_NULL_DOUBLE_STR),
                printPhase(getPhase(entry), IF_NULL_STR),
                printValue(getData(entry), IF_NULL_DOUBLE_STR),
                printCumulativeValue(getData(entry), IF_NULL_DOUBLE_STR)
        };
    }

    @Override
    protected void add(int year, AdoptionEntry2 info) {
        getData().varUpdate(
                AdoptionResultInfo2.ZERO,
                AdoptionResultInfo2.getAdder(year == info.getYear()),
                year,
                info.getPhase(),
                AdoptionResultInfo2.ONE
        );
    }
}
