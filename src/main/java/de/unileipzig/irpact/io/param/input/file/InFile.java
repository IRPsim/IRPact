package de.unileipzig.irpact.io.param.input.file;

import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InFile extends InIRPactEntity {

    @TreeAnnotationResource.Init
    static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    static void applyRes(TreeAnnotationResource res) {
    }

    String getFileNameWithoutExtension();

    default boolean isEquals(InFile other) {
        return Objects.equals(getFileNameWithoutExtension(), other.getFileNameWithoutExtension());
    }
}
