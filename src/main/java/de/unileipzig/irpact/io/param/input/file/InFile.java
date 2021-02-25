package de.unileipzig.irpact.io.param.input.file;

import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InFile extends InEntity {

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }

    String getFileNameWithoutExtension();

    default boolean isEquals(InFile other) {
        return Objects.equals(getFileNameWithoutExtension(), other.getFileNameWithoutExtension());
    }
}
