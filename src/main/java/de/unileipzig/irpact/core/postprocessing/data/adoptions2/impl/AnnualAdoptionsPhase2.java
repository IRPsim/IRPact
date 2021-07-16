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

    public AnnualAdoptionsPhase2() {
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
                toDoubleAttribute(entry[0], IF_NULL_DOUBLE),
                toStringAttribute(printPhase((AdoptionPhase) entry[1], IF_NULL_STR), IF_NULL_STR),
                toDoubleAttribute(((AdoptionResultInfo2) entry[2]).getValue(), IF_NULL_DOUBLE),
                toDoubleAttribute(((AdoptionResultInfo2) entry[2]).getCumulativeValue(), IF_NULL_DOUBLE)
        };
    }

    @Override
    protected Function<? super Object[], ? extends String[]> getStringMappingFunction() {
        return entry -> new String[] {
                print(entry[0], IF_NULL_DOUBLE_STR),
                printPhase((AdoptionPhase) entry[1], IF_NULL_STR),
                printValue((AdoptionResultInfo2) entry[2], IF_NULL_DOUBLE_STR),
                printCumulativeValue((AdoptionResultInfo2) entry[2], IF_NULL_DOUBLE_STR)
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
