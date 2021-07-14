package de.unileipzig.irpact.io.param.output;

import de.unileipzig.irpact.commons.util.MultiCounter;
import de.unileipzig.irpact.io.param.IOResources;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.SimpleCopyCache;
import de.unileipzig.irpact.io.param.inout.persist.binary.BinaryPersistData;
import de.unileipzig.irpact.io.param.output.agent.OutConsumerAgentGroup;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irpact.start.optact.out.OutCustom;
import de.unileipzig.irptools.defstructure.AnnotationResource;
import de.unileipzig.irptools.defstructure.DefinitionType;
import de.unileipzig.irptools.defstructure.ParserInput;
import de.unileipzig.irptools.defstructure.RootClass;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.uiedn.Section;
import de.unileipzig.irptools.uiedn.Sections;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.UiEdn;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static de.unileipzig.irpact.io.param.IOConstants.INFORMATIONS_OUT;
import static de.unileipzig.irpact.io.param.IOConstants.ROOT;
import static de.unileipzig.irpact.io.param.ParamUtil.*;

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
    public void setInformations(OutInformation[] informations) {
        this.informations = informations;
    }
    public void addInformations(OutInformation[] informations) {
        this.informations = addAll(this.informations, informations);
    }
    public OutInformation[] getInformations() {
        return informations;
    }

    @FieldDefinition
    public OutConsumerAgentGroup[] outConsumerAgentGroups = new OutConsumerAgentGroup[0];

    @FieldDefinition
    public BinaryPersistData[] binaryPersistData = new BinaryPersistData[0];
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

    //=========================
    //OptAct
    //=========================

    @FieldDefinition
    public OutCustom[] outGrps = new OutCustom[0];

    //==================================================

    public OutRoot() {
    }

    //=========================
    //
    //=========================

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
        copy.informations = cache.copyArray(informations);
        copy.outConsumerAgentGroups = cache.copyArray(outConsumerAgentGroups);
        copy.binaryPersistData = cache.copyArray(binaryPersistData);
        //optact
        copy.outGrps = outGrps;

        return copy;
    }

    //=========================
    //CLASSES
    //=========================

    public static final List<ParserInput> INPUT_WITHOUT_ROOT = ParserInput.merge(
            ParserInput.listOf(DefinitionType.OUTPUT,
                    OutInformation.class,
                    OutConsumerAgentGroup.class,
                    //===
                    BinaryPersistData.class
            )
    );

    public static final List<ParserInput> INPUT_WITH_ROOT = ParserInput.merge(
            INPUT_WITHOUT_ROOT,
            ParserInput.listOf(DefinitionType.OUTPUT,
                    OutRoot.class
            )
    );

    public static final List<ParserInput> ALL_CLASSES = ParserInput.merge(
            INPUT_WITH_ROOT,
            de.unileipzig.irpact.start.optact.out.OutRoot.CLASSES_WITHOUT_ROOT
    );

    //=========================
    //UI
    //=========================

    @SuppressWarnings("unused")
    public static void initRes(TreeAnnotationResource res) {
        IOResources.Data userData = res.getUserDataAs();
        MultiCounter counter = userData.getCounter();

        addPathElement(res, INFORMATIONS_OUT, ROOT);
                addPathElement(res, OutInformation.thisName(), INFORMATIONS_OUT);

        addPathElement(res, OutConsumerAgentGroup.thisName(), ROOT);
    }

    public static void applyRes(TreeAnnotationResource res) {
    }

    @Override
    public void peekEdn(Sections sections, UiEdn ednType) {
        if(ednType == UiEdn.OUTPUT) {
            sections.add(buildNetworkImageSection(-2));
            sections.add(buildImageSection(-1));
            handleOptAct(sections);
        }
    }

    @SuppressWarnings("SameParameterValue")
    protected Section buildImageSection(int priority) {
        Section imageSection = new Section();
        imageSection.setPriority(priority);
        imageSection.setLabel("Bilder");
        imageSection.setDescription("Test für Bildanzeige");
        imageSection.setIcon("fa fa-spinner");

        Sections imageSections = imageSection.getSections();
        Section image1 = new Section();
        image1.setPriority(1);
        image1.setLabel("Bild 1");
        image1.setIcon("fa fa-spinner");
        image1.setImage("Bild1.png");
        image1.setDescription("Test Bild 1");

        Section image2 = new Section();
        image2.setPriority(2);
        image2.setLabel("Bild 2");
        image2.setIcon("fa fa-spinner");
        image2.setImage("Bild2.png");
        image2.setDescription("Test Bild 2");

        Section image3 = new Section();
        image3.setPriority(3);
        image3.setLabel("Bild 3");
        image3.setIcon("fa fa-spinner");
        image3.setImage("Bild3.png");
        image3.setDescription("Test Bild 3");

        imageSections.addAll(image1, image2, image3);
        return imageSection;
    }

    @SuppressWarnings("SameParameterValue")
    protected Section buildNetworkImageSection(int priority) {
        Section imageSection = new Section();
        imageSection.setPriority(priority);
        imageSection.setLabel("Agentennetzwerk");
        imageSection.setImage(IRPact.IMAGE_AGENTGRAPH_OUTPUT);
        imageSection.setDescription("Agentennetzwerk in IRPact zum Ende der Simulation");
        imageSection.setIcon("fa fa-spinner");
        return imageSection;
    }

    protected void handleOptAct(Sections sections) {
        Section section = sections.findByLabel("set_side_cust");
        section.setPriority(100);
    }
}
