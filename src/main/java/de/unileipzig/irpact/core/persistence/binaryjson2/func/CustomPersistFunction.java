package de.unileipzig.irpact.core.persistence.binaryjson2.func;

import de.unileipzig.irpact.core.persistence.binaryjson2.IterableArrayNode;
import de.unileipzig.irpact.core.persistence.binaryjson2.persist.PersistHelper;
import de.unileipzig.irpact.core.persistence.binaryjson2.restore.RestoreHelper;

/**
 * @author Daniel Abitz
 */
public interface CustomPersistFunction<I> {

    void persist(I input, PersistHelper helper, IterableArrayNode arr) throws Throwable;

    I restore(RestoreHelper helper, IterableArrayNode arr) throws Throwable;
}
