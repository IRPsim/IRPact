package de.unileipzig.irpact.experimental.tests.timeModelWithController;

import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class Data<T> {

    public T data;

    public Data() {
    }

    public Data(T data) {
        this.data = data;
    }
}
