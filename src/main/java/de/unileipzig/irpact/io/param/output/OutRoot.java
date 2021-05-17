package de.unileipzig.irpact.io.param.output;

import de.unileipzig.irpact.io.param.IOResources;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.SimpleCopyCache;
import de.unileipzig.irpact.io.param.inout.persist.binary.BinaryPersistData;
import de.unileipzig.irpact.io.param.output.agent.OutConsumerAgentGroup;
import de.unileipzig.irpact.io.param.output.agent.OutGeneralConsumerAgentGroup;
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

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static de.unileipzig.irpact.io.param.IOConstants.ROOT;
import static de.unileipzig.irpact.io.param.IOConstants.SPECIAL_SETTINGS;
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
    public OutGeneral general = new OutGeneral();

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
        return CLASSES;
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
                    OutConsumerAgentGroup.class,
                    OutGeneralConsumerAgentGroup.class,

                    OutAdoptionResult.class,
                    OutAnnualAdoptionData.class,
                    OutEntity.class,
                    OutGeneral.class,
                    //===
                    BinaryPersistData.class
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
            //getInRootAsReference(),
            ParserInput.listOf(Type.OUTPUT,
                    OutRoot.class
            )
    );

//    //Alles rausnehmen, was inout ist und hier bereits definiert wurde und die root/global Komponenten.
//    private static boolean filterInput(ParserInput pi) {
//        return pi.getClazz() != BinaryPersistData.class
//                && pi.getClazz() != InGeneral.class
//                && pi.getClazz() != Ii.class
//                && pi.getClazz() != InRoot.class
//                && pi.getClazz() != SideCustom.class
//                && pi.getClazz() != GraphvizGlobal.class;
//    }
//
//    private static List<ParserInput> getInRootAsReference() {
//        return InRoot.CLASSES_WITH_GRAPHVIZ.stream()
//                .filter(OutRoot::filterInput)
//                .map(pi -> ParserInput.newInstance(Type.REFERENCE, pi.getClazz()))
//                .collect(Collectors.toList());
//    }

    //=========================
    //UI
    //=========================

    public static void initRes(TreeAnnotationResource res) {
        addPathElement(res, OutAdoptionResult.thisName(), ROOT);
        addPathElement(res, OutAnnualAdoptionData.thisName(), SPECIAL_SETTINGS);
    }

    public static void applyRes(TreeAnnotationResource res) {
    }
}
