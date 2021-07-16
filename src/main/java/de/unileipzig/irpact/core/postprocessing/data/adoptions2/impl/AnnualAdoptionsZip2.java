package de.unileipzig.irpact.core.postprocessing.data.adoptions2.impl;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.AbstractAnnualAdoptionsX1;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.AdoptionEntry2;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.AdoptionResultInfo2;
import de.unileipzig.irpact.core.process.ra.RAConstants;

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
                toDoubleAttribute(entry[0], IF_NULL_DOUBLE),
                toStringAttribute(entry[1], IF_NULL_STR),
                toDoubleAttribute(((AdoptionResultInfo2) entry[2]).getValue(), IF_NULL_DOUBLE),
                toDoubleAttribute(((AdoptionResultInfo2) entry[2]).getCumulativeValue(), IF_NULL_DOUBLE)
        };
    }

    @Override
    protected Function<? super Object[], ? extends String[]> getStringMappingFunction() {
        return entry -> new String[] {
                print(entry[0], IF_NULL_DOUBLE_STR),
                print(entry[1], IF_NULL_STR),
                printValue((AdoptionResultInfo2) entry[2], IF_NULL_DOUBLE_STR),
                printCumulativeValue((AdoptionResultInfo2) entry[2], IF_NULL_DOUBLE_STR)
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
