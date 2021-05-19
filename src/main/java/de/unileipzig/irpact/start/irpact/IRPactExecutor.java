package de.unileipzig.irpact.start.irpact;

/**
 * Allows to run IRPact in different ways without multiple if-then-else checks.
 *
 * @author Daniel Abitz
 */
public interface IRPactExecutor {

    int id();

    void execute(IRPact irpact) throws Exception;
}
