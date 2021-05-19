package de.unileipzig.irpact.io.param.output;

import de.unileipzig.irpact.io.param.IOResources;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.SimpleCopyCache;
import de.unileipzig.irpact.io.param.inout.persist.binary.BinaryPersistData;
import de.unileipzig.irpact.io.param.output.agent.OutConsumerAgentGroup;
import de.unileipzig.irpact.io.param.output.agent.OutGeneralConsumerAgentGroup;
import de.unileipzig.irpact.start.optact.out.OutCustom;
import de.unileipzig.irptools.defstructure.AnnotationResource;
import de.unileipzig.irptools.defstructure.DefinitionType;
import de.unileipzig.irptools.defstructure.ParserInput;
import de.unileipzig.irptools.defstructure.RootClass;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.Util;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static de.unileipzig.irpact.io.param.IOConstants.ROOT;
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
    public OutConsumerAgentGroup[] consumerAgentGroups = new OutConsumerAgentGroup[0];

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

    public OutAnnualAdoptionData[] getAnnualAdoptionData() {
        return annualAdoptionData;
    }

    public void addBinaryPersistData(Collection<? extends BinaryPersistData> coll) {
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

    public BinaryPersistData[] getBinaryPersistData() {
        return binaryPersistData;
    }

    public int getHiddenBinaryDataLength() {
        return ParamUtil.len(binaryPersistData);
    }

    @Override
    public Collection<? extends ParserInput> getInput() {
        return ALL_CLASSES;
    }

    @Override
    public AnnotationResource getResources() {
        return new IOResources();
    }

    @Override
    public AnnotationResource getResource(Path pathToFile) {
        return new IOResources(pathToFile);
    }

    @Override
    public AnnotationResource getResource(Locale locale) {
        return new IOResources(locale);
    }

    public OutRoot copy() {
        SimpleCopyCache cache = new SimpleCopyCache();
        OutRoot copy = new OutRoot();
        //act
        copy.consumerAgentGroups = cache.copyArray(consumerAgentGroups);
        copy.annualAdoptionData = cache.copyArray(annualAdoptionData);
        copy.binaryPersistData = cache.copyArray(binaryPersistData);
        //optact
        copy.outGrps = outGrps;

        return copy;
    }

    public void setConsumerAgentGroups(Collection<? extends OutConsumerAgentGroup> cags) {
        this.consumerAgentGroups = cags.toArray(new OutConsumerAgentGroup[0]);
    }

    public void setAnnualAdoptionData(Collection< ? extends OutAnnualAdoptionData> data) {
        this.annualAdoptionData = data.toArray(new OutAnnualAdoptionData[0]);
    }

    //=========================
    //CLASSES
    //=========================

    public static final List<ParserInput> INPUT_WITHOUT_ROOT = Util.mergedArrayListOf(
            ParserInput.listOf(DefinitionType.OUTPUT,
                    OutConsumerAgentGroup.class,
                    OutGeneralConsumerAgentGroup.class,

                    OutAnnualAdoptionData.class,
                    OutEntity.class,
                    //===
                    BinaryPersistData.class
            )
    );

    public static final List<ParserInput> INPUT_WITH_ROOT = Util.mergedArrayListOf(
            INPUT_WITHOUT_ROOT,
            ParserInput.listOf(DefinitionType.OUTPUT,
                    OutRoot.class
            )
    );

    public static final List<ParserInput> ALL_CLASSES = Util.mergedArrayListOf(
            INPUT_WITH_ROOT,
            de.unileipzig.irpact.start.optact.out.OutRoot.CLASSES_WITHOUT_ROOT
    );

    //=========================
    //UI
    //=========================

    public static void initRes(TreeAnnotationResource res) {
        addPathElement(res, OutGeneralConsumerAgentGroup.thisName(), ROOT);
        addPathElement(res, OutAnnualAdoptionData.thisName(), ROOT);
    }

    public static void applyRes(TreeAnnotationResource res) {
    }
}
