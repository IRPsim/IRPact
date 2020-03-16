package de.unileipzig.irpact.core.agent.policy;

/**
 * @author Daniel Abitz
 */
public class DoNothingProductPolicyScheme implements ProductPolicyScheme {

    public static final String NAME = DoNothingProductPolicyScheme.class.getSimpleName();
    public static final DoNothingProductPolicyScheme INSTANCE = new DoNothingProductPolicyScheme();
}
