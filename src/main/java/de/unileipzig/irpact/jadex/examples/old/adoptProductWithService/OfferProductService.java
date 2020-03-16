package de.unileipzig.irpact.jadex.examples.old.adoptProductWithService;

import jadex.commons.future.IFuture;

public interface OfferProductService {

    String getOfferName();

    IFuture<Boolean> hasProduct(String product);

    IFuture<Boolean> adoptProduct(String product);
}
