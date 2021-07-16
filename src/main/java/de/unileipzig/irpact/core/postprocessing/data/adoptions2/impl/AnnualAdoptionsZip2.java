package de.unileipzig.irpact.core.postprocessing.data.adoptions2.impl;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.AbstractAnnualAdoptionsX1;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.AdoptionEntry2;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.AdoptionResultInfo2;
import de.unileipzig.irpact.core.process.ra.RAConstants;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public class AnnualAdoptionsZip2 extends AbstractAnnualAdoptionsX1<String> {

    protected String zipKey = RAConstants.ZIP;

    public AnnualAdoptionsZip2() {
    }

    public void setZipKey(String zipKey) {
        this.zipKey = zipKey;
    }

    public String getZipKey() {
        return zipKey;
    }

    @Override
    protected Class<String> getFirstType() {
        return String.class;
    }

    @Override
    protected Function<? super Object[], ? extends Attribute[]> getMappingFunction() {
        return entry -> new Attribute[] {
                toDoubleAttribute(entry[0]),
                toStringAttribute(entry[1]),
                toDoubleAttribute(((AdoptionResultInfo2) entry[2]).getValue()),
                toDoubleAttribute(((AdoptionResultInfo2) entry[2]).getCumulativeValue())
        };
    }

    @Override
    protected Function<? super Object[], ? extends String[]> getStringMappingFunction() {
        return entry -> new String[] {
                Objects.toString(entry[0]),
                Objects.toString(entry[1]),
                ((AdoptionResultInfo2) entry[2]).printValue(),
                ((AdoptionResultInfo2) entry[2]).printCumulativeValue()
        };
    }

    @Override
    public String getTypeForLocalization() {
        return "zip";
    }

    @Override
    protected void add(int year, AdoptionEntry2 info) {
        String zip = findAgentStringAttributeValue(info, getZipKey());
        if(zip == null) {
            return;
        }

        getData().varUpdate(
                AdoptionResultInfo2.ZERO,
                AdoptionResultInfo2.getAdder(year == info.getYear()),
                year,
                zip,
                AdoptionResultInfo2.ONE
        );
    }
}
