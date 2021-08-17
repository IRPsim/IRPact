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

    public static final int INDEX_YEAR = 0;
    public static final int INDEX_ZIP = 1;
    public static final int INDEX_DATA = 2;

    protected String zipKey = RAConstants.ZIP;

    public AnnualAdoptionsZip2() {
    }

    public static Integer getYear(Object[] entry) {
        return (Integer) entry[INDEX_YEAR];
    }

    public static String getZip(Object[] entry) {
        return (String) entry[INDEX_ZIP];
    }

    public static AdoptionResultInfo2 getData(Object[] entry) {
        return (AdoptionResultInfo2) entry[INDEX_DATA];
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
                toDoubleAttribute(entry[INDEX_YEAR], IF_NULL_DOUBLE),
                toStringAttribute(entry[INDEX_ZIP], IF_NULL_STR),
                toDoubleAttribute(getData(entry).getValue(), IF_NULL_DOUBLE),
                toDoubleAttribute(getData(entry).getCumulativeValue(), IF_NULL_DOUBLE)
        };
    }

    @Override
    protected Function<? super Object[], ? extends String[]> getStringMappingFunction() {
        return entry -> new String[] {
                print(entry[INDEX_YEAR], IF_NULL_DOUBLE_STR),
                print(entry[INDEX_ZIP], IF_NULL_STR),
                printValue(getData(entry), IF_NULL_DOUBLE_STR),
                printCumulativeValue(getData(entry), IF_NULL_DOUBLE_STR)
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
                AdoptionResultInfo2.getAdder(year == getYearOrFirstYear(info)),
                year,
                zip,
                AdoptionResultInfo2.ONE
        );
    }
}
