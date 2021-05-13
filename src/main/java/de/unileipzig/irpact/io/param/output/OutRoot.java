package de.unileipzig.irpact.io.param.output;

import de.unileipzig.irpact.io.param.IOResources;
import de.unileipzig.irpact.io.param.SimpleCopyCache;
import de.unileipzig.irpact.io.param.inout.persist.binary.BinaryPersistData;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.start.optact.in.Ii;
import de.unileipzig.irpact.start.optact.out.OutCustom;
import de.unileipzig.irptools.defstructure.AnnotationResource;
import de.unileipzig.irptools.defstructure.ParserInput;
import de.unileipzig.irptools.defstructure.RootClass;
import de.unileipzig.irptools.defstructure.Type;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.Util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.addPathElement;

/**
 * @author Daniel Abitz
 */
@Definition(root = true)
public class OutRoot implements RootClass {

    //=========================
    //IRPact
    //=========================

    @FieldDefinition
    public OutInformation[] informations = new OutInformation[0];

    @FieldDefinition
    public OutAdoptionResult[] adoptionResults = new OutAdoptionResult[0];

    @FieldDefinition
    public OutAnnualAdoptionData[] annualAdoptionData = new OutAnnualAdoptionData[0];

    @FieldDefinition
    public BinaryPersistData[] binaryPersistData = new BinaryPersistData[0];

    //=========================
    //OptAct
    //=========================

    @FieldDefinition
    public OutCustom[] outGrps = new OutCustom[0];

    //==================================================

    public OutRoot() {
    }

    public void setInformation(String text) {
        setInformation(new OutInformation(text));
    }

    public void setInformation(OutInformation information) {
        this.informations = new OutInformation[]{information};
    }

    public void addHiddenBinaryData(Collection<? extends BinaryPersistData> coll) {
        int pos;
        if(binaryPersistData == null) {
            pos = 0;
            binaryPersistData = new BinaryPersistData[coll.size()];
        } else {
            pos = binaryPersistData.length;
            binaryPersistData = Arrays.copyOf(binaryPersistData, binaryPersistData.length + coll.size());
        }
        for(BinaryPersistData hbd: coll) {
            binaryPersistData[pos++] = hbd;
        }
    }

    public BinaryPersistData[] getHiddenBinaryData() {
        return binaryPersistData;
    }

    public int getHiddenBinaryDataLength() {
        return ParamUtil.len(binaryPersistData);
    }

    @Override
    public Collection<? extends ParserInput> getInput() {
        return CLASSES;
    }

    @Override
    public AnnotationResource getResources() {
        return new IOResources();
    }

    public OutRoot copy() {
        SimpleCopyCache cache = new SimpleCopyCache();
        OutRoot copy = new OutRoot();
        //act
        copy.informations = cache.copyArray(informations);
        copy.adoptionResults = cache.copyArray(adoptionResults);
        copy.annualAdoptionData = cache.copyArray(annualAdoptionData);
        copy.binaryPersistData = cache.copyArray(binaryPersistData);
        //optact
        copy.outGrps = outGrps;

        return copy;
    }

    //=========================
    //CLASSES
    //=========================

    public static final List<ParserInput> LIST = Util.mergedArrayListOf(
            ParserInput.listOf(Type.OUTPUT,
                    OutAdoptionResult.class,
                    OutAnnualAdoptionData.class,
                    OutInformation.class,
                    BinaryPersistData.class
            ),
            ParserInput.listOf(Type.REFERENCE,
                    InConsumerAgentGroup.class
            )
    );

    public static final List<ParserInput> WITH_OPTACT = Util.mergedArrayListOf(
            LIST,
            ParserInput.listOf(Type.REFERENCE,
                    Ii.class,
                    OutCustom.class
            )
    );

    public static final List<ParserInput> CLASSES = Util.mergedArrayListOf(
            WITH_OPTACT,
            ParserInput.listOf(Type.OUTPUT,
                    OutRoot.class
            )
    );

    //=========================
    //UI
    //=========================

    public static void initRes(TreeAnnotationResource res) {
        addPathElement(res, OutInformation.thisName(), ROOT);
        addPathElement(res, OutAdoptionResult.thisName(), ROOT);
        addPathElement(res, OutAnnualAdoptionData.thisName(), SPECIAL_SETTINGS);
    }

    public static void applyRes(TreeAnnotationResource res) {
    }
}
