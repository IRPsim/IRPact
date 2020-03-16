package de.unileipzig.irpact.jadex.examples.twoapps;

import jadex.commons.future.IFuture;

/**
 * @author Daniel Abitz
 */
public interface Service0 {

    IFuture<String> hello(String input);
}
