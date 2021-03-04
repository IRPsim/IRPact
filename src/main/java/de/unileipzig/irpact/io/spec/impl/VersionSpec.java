package de.unileipzig.irpact.io.spec.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.InVersion;
import de.unileipzig.irpact.io.spec.*;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_version;

/**
 * @author Daniel Abitz
 */
public class VersionSpec implements ToSpecConverter<InVersion>, ToParamConverter<InVersion> {

    public static final VersionSpec INSTANCE = new VersionSpec();

    @Override
    public Class<InVersion> getParamType() {
        return InVersion.class;
    }

    @Override
    public void toSpec(InVersion input, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        create(input, manager.getGeneral().get(), manager, converter, inline);
    }

    @Override
    public void create(InVersion input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        SpecificationHelper spec = new SpecificationHelper(root);
        spec.set(TAG_version, input.getVersion());
    }

    @Override
    public InVersion[] toParam(SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) throws ParsingException {
        ObjectNode root = manager.getGeneral().get();
        return new InVersion[] {
                toParam(root, manager, converter, cache)
        };
    }

    @Override
    public InVersion toParam(ObjectNode root, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        SpecificationHelper spec = new SpecificationHelper(root);
        return new InVersion(spec.getText(TAG_version));
    }
}
