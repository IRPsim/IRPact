package de.unileipzig.irpact.core.postprocessing.data.adoptions2.impl;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.AbstractAnnualAdoptionsX1;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.AdoptionEntry2;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.AdoptionResultInfo2;
import de.unileipzig.irpact.core.util.AdoptionPhase;

import java.util.Objects;
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
                toDoubleAttribute(entry[0]),
                toStringAttribute(((AdoptionPhase) entry[1]).name()),
                toDoubleAttribute(((AdoptionResultInfo2) entry[2]).getValue()),
                toDoubleAttribute(((AdoptionResultInfo2) entry[2]).getCumulativeValue())
        };
    }

    @Override
    protected Function<? super Object[], ? extends String[]> getStringMappingFunction() {
        return entry -> new String[] {
                Objects.toString(entry[0]),
                ((AdoptionPhase) entry[1]).name(),
                ((AdoptionResultInfo2) entry[2]).printValue(),
                ((AdoptionResultInfo2) entry[2]).printCumulativeValue()
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
