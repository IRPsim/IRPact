package de.unileipzig.irpact.misc.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;

/**
 * @author Daniel Abitz
 */
public class ToyModel1 extends AbstractToyModel {

    @Override
    public InRoot createRoot() throws Exception {
        return null;
    }

    public static void main(String[] args) throws Exception {
        ToyModel1 model = new ToyModel1();
        model.run(args);
    }
}
