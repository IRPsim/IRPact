package de.unileipzig.irpact.core.agent.policy;

import de.unileipzig.irpact.core.Scheme;
import de.unileipzig.irpact.core.currency.Price;

/**
 * @author Daniel Abitz
 */
public interface TaxesScheme extends Scheme {

    Price levyTaxes(Price price);
}
