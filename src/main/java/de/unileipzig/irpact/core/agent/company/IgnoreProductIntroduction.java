package de.unileipzig.irpact.core.agent.company;

import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public class IgnoreProductIntroduction implements ProductIntroductionScheme {

    public static final String NAME = IgnoreProductIntroduction.class.getSimpleName();
    public static final IgnoreProductIntroduction INSTANCE = new IgnoreProductIntroduction();

    @Override
    public void handle(CompanyAgent agent, Product product) {
    }
}
