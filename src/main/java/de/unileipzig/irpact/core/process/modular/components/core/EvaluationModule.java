package de.unileipzig.irpact.core.process.modular.components.core;

import de.unileipzig.irpact.core.process.modular.EvaluationResult;

/**
 * @author Daniel Abitz
 */
public interface EvaluationModule<I> extends Module {

    EvaluationResult evaluate(I input) throws Throwable;
}
