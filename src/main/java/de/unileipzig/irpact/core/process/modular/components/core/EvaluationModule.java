package de.unileipzig.irpact.core.process.modular.components.core;

import de.unileipzig.irpact.core.process.PostAction;
import de.unileipzig.irpact.core.process.modular.EvaluationResult;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public interface EvaluationModule<I> extends Module {

    EvaluationResult evaluate(I input) throws Throwable;

    EvaluationResult evaluate(I input, List<PostAction> postActions) throws Throwable;
}
