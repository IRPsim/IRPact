package de.unileipzig.irpact.util.scenarios;

import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.param.input.InRoot;

import java.util.Collections;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class EmptyScenario extends AbstractScenario {

    public static final int REVISION = 1;

    public EmptyScenario(String name, String creator, String description) {
        super(name, creator, description);
        setRevision(REVISION);
    }

    @Override
    public List<InRoot> createInRoots() {
        InRoot root = createRootWithInformations();
        return Collections.singletonList(root);
    }
}
