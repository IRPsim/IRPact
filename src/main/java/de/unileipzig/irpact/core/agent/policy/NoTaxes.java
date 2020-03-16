package de.unileipzig.irpact.core.agent.policy;

import de.unileipzig.irpact.core.currency.Price;

/**
 * @author Daniel Abitz
 */
public class NoTaxes implements TaxesScheme {

    public static final String NAME = NoTaxes.class.getSimpleName();
    public static final NoTaxes INSTANCE = new NoTaxes();

    @Override
    public Price levyTaxes(Price price) {
        return price;
    }
}
