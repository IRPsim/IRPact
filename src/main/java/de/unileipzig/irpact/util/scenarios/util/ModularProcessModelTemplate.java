package de.unileipzig.irpact.util.scenarios.util;

import de.unileipzig.irpact.io.param.input.postdata.InPostDataAnalysis;
import de.unileipzig.irpact.io.param.input.process2.modular.InModularProcessModel2;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InOutputImage2;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface ModularProcessModelTemplate {

    InModularProcessModel2 getModel();

    boolean addPostData(Collection<? super InPostDataAnalysis> c);

    boolean addImages(Collection<? super InOutputImage2> c);
}
