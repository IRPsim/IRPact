package de.unileipzig.irpact.io.param.output;

import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irptools.defstructure.annotation.Definition;

/**
 * @author Daniel Abitz
 */
@Definition(copy = InIRPactEntity.class)
public interface OutIRPactEntity extends OutEntity {
}
