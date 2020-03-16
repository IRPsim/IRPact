package de.unileipzig.irpact.jadex.examples.deprecated.old.adoptProductWithService;

import jadex.commons.future.IFuture;

public interface OfferProductService {

    String getOfferName();

    IFuture<Boolean> hasProduct(String product);

    IFuture<Boolean> adoptProduct(String product);
}
