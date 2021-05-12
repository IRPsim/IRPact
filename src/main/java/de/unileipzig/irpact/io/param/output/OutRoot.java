package de.unileipzig.irpact.io.param.output;

import de.unileipzig.irpact.commons.util.MultiCounter;
import de.unileipzig.irpact.io.param.IOResources;
import de.unileipzig.irpact.io.param.LocData;
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

/**
 * @author Daniel Abitz
 */
@Definition(root = true)
public class OutRoot implements RootClass {

    public static final List<ParserInput> LIST = Util.mergedArrayListOf(
            ParserInput.listOf(Type.OUTPUT,
                    OutAdoptionResult.class,
                    OutAnnualData.class,
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
            Util.arrayListOf(
                    ParserInput.newInstance(Type.OUTPUT, OutRoot.class)
            )
    );

    //=========================
    //IRPact
    //=========================

    @FieldDefinition
    public OutAdoptionResult[] adoptionResults = new OutAdoptionResult[0];

    @FieldDefinition
    public BinaryPersistData[] binaryPersistData = new BinaryPersistData[0];

    //=========================
    //OptAct
    //=========================

    @FieldDefinition
    public OutCustom[] outGrps = new OutCustom[0];

    //=========================
    //OptAct
    //=========================

    public OutRoot() {
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

    //=========================
    //UI
    //=========================

    public static void initRes(TreeAnnotationResource res) {
        IOResources.Data userData = res.getUserDataAs();
        MultiCounter counter = userData.getCounter();
        LocData loc = userData.getData();


    }
}
