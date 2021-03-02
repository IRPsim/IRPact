package de.unileipzig.irpact.io.param.inout.persist.param;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition
public class UIDUncertaintyGroupAttributeSupplierEntry {

    public String _name;

    @FieldDefinition
    public InConsumerAgentGroup[] cag;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] uncert;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] conv;

    public UIDUncertaintyGroupAttributeSupplierEntry() {
    }

    public InConsumerAgentGroup getCag() throws ParsingException {
        return ParamUtil.getInstance(cag, "cag");
    }

    public InUnivariateDoubleDistribution getUncert() throws ParsingException {
        return ParamUtil.getInstance(uncert, "uncert");
    }

    public InUnivariateDoubleDistribution getConv() throws ParsingException {
        return ParamUtil.getInstance(conv, "conv");
    }
}
