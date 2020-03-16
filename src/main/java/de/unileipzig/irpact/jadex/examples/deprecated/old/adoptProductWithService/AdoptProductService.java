package de.unileipzig.irpact.jadex.examples.deprecated.old.adoptProductWithService;

import jadex.commons.future.IFuture;

public interface AdoptProductService {

    String getAdopterName();

    IFuture<Void> anounced(String product);
}
