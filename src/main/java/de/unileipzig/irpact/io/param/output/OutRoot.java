package de.unileipzig.irpact.io.param.output;

import de.unileipzig.irpact.io.param.LocalizedSnakeYamlBasedUiResource;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
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

import java.io.IOException;
import java.io.UncheckedIOException;
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
    //TODO remove
    public AnnotationResource getResources() {
//        return new IOResources();
        throw new UnsupportedOperationException();
    }

    @Override
    public AnnotationResource getResource(Path pathToFile) {
//        return new IOResources(pathToFile);
        try {
            LocalizedSnakeYamlBasedUiResource res = new LocalizedSnakeYamlBasedUiResource();
            res.load(pathToFile);
            res.add(INPUT_WITH_ROOT);
            res.update();
            return res;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    //TODO update
    public AnnotationResource getResource(Locale locale) {
//        return new IOResources(locale);
        throw new UnsupportedOperationException();
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

    @TreeAnnotationResource.Init
    public static void initRes(LocalizedUiResource res) {
        res.addPathElement(INFORMATIONS_OUT, ROOT);
        res.addPathElement(OutInformation.thisName(), INFORMATIONS_OUT);
        res.addPathElement(OutConsumerAgentGroup.thisName(), ROOT);
    }

    @TreeAnnotationResource.Apply
    public static void applyRes(LocalizedUiResource res) {
    }

    @Override
    public void peekEdn(Sections sections, UiEdn ednType) {
        if(ednType == UiEdn.OUTPUT) {
            sections.add(buildImageSection(-1));
            addStracktraceSubSectionToInformationSection(sections);
            handleOptAct(sections);
        }
    }

    protected void addStracktraceSubSectionToInformationSection(Sections sections) {
        Section stackTraceSection = new Section();
        stackTraceSection.setLabel("Fehlermeldung");
        stackTraceSection.setDescription("Falls während der Simulation ein schwerwiegender Fehler aufgetreten ist, welcher zum Programmabbruch führte, wird dieser hier angezeigt. Anderenfalls wird nichts oder der Hinweis auf eine fehlerfreie Durchführung angegeben.");
        stackTraceSection.setIcon(IRPact.ICON_WARNING);
        stackTraceSection.setPriority(100); //should be large
        stackTraceSection.setImage(IRPact.IMAGE_STACKTRACE_PNG);

        Section informations = sections.findByLabel("Informationen");
        informations.getSections().add(stackTraceSection);
    }

    @SuppressWarnings("SameParameterValue")
    protected Section buildImageSection(int priority) {
        Section imageSection = new Section();
        imageSection.setPriority(priority);
        imageSection.setLabel("Ergebnisvisualisierungen");
        imageSection.setDescription("Visualisierung von verschiedenen Ergebnissen aus IRPact.");
        imageSection.setIcon(IRPact.ICON_IMAGES);
        imageSection.getSections().addAll(
                buildNetworkImageSection(1),
                buildAdoptionSection(2),
                buildQuantilSection(3),
                buildAnnualBucketSection(4),
                buildCustomSection(5)
        );
        return imageSection;
    }

    @SuppressWarnings("SameParameterValue")
    protected Section buildNetworkImageSection(int priority) {
        Section networkSection = new Section();
        networkSection.setPriority(priority);
        networkSection.setLabel("Finales Agentennetzwerk");
        networkSection.setImage(IRPact.IMAGE_AGENTGRAPH_OUTPUT_PNG);
        networkSection.setDescription("Agentennetzwerk von IRPact zum Ende der Simulation");
        networkSection.setIcon(IRPact.ICON_IMAGE);
        return networkSection;
    }

    @SuppressWarnings("SameParameterValue")
    protected Section buildAdoptionSection(int priority) {
        Section adoptionSection = new Section();
        adoptionSection.setPriority(priority);
        adoptionSection.setLabel("Visualisierung der Adoptionen");
        adoptionSection.setDescription("Darstellung der Adoptionen im Simulationszeitraum nach unterschiedlichen Kriterien.");
        adoptionSection.setIcon(IRPact.ICON_IMAGES);

        Sections adoptionSections = adoptionSection.getSections();

        Section image1 = new Section();
        image1.setPriority(1);
        image1.setLabel("Jährliche Adoptionen (Milieu)");
        image1.setIcon(IRPact.ICON_IMAGE);
        image1.setImage(IRPact.IMAGE_ANNUAL_ADOPTION_MILIEU_PNG);
        image1.setDescription("Zeigt die jährlichen Adoptionen für die einzelnen Milieus.");

        Section image2 = new Section();
        image2.setPriority(2);
        image2.setLabel("Jährliche Adoptionen im Vergleich (PLZ)");
        image2.setIcon(IRPact.ICON_IMAGE);
        image2.setImage(IRPact.IMAGE_COMPARED_ANNUAL_ADOPTIONS_ZIP_PNG);
        image2.setDescription("Zeigt die jährlichen Adoptionen für die einzelnen Postleitzahlen im Vergleich zu realen Daten.");

        Section image3 = new Section();
        image3.setPriority(3);
        image3.setLabel("Jährliche Adoptionen (Phase)");
        image3.setIcon(IRPact.ICON_IMAGE);
        image3.setImage(IRPact.IMAGE_PROCESS_MODEL_PHASE_OVERVIEW_PNG);
        image3.setDescription("Zeigt die kumulierten jährlichen Adoptionen für die verschiedenen Adoptionsphasen.");

        Section image4 = new Section();
        image4.setPriority(4);
        image4.setLabel("Interessensentwicklung");
        image4.setIcon(IRPact.ICON_IMAGE);
        image4.setImage(IRPact.IMAGE_ANNUAL_INTEREST_GROWTH_PNG);
        image4.setDescription("Zeigt die Interessensentwicklung an.");

        Section image5 = new Section();
        image5.setPriority(5);
        image5.setLabel("Phasenuebersicht");
        image5.setIcon(IRPact.ICON_IMAGE);
        image5.setImage(IRPact.IMAGE_PHASE_OVERVIEW_PNG);
        image5.setDescription("Zeigt die Entwicklung der Phasen an.");

        Section image6 = new Section();
        image6.setPriority(6);
        image6.setLabel("Jährliche Adoptionen im Vergleich");
        image6.setIcon(IRPact.ICON_IMAGE);
        image6.setImage(IRPact.IMAGE_COMPARED_ANNUAL_ADOPTIONS_PNG);
        image6.setDescription("Zeigt die jährlichen Adoptionen für die einzelnen Postleitzahlen im Vergleich zu realen Daten.");

        Section image7 = new Section();
        image7.setPriority(7);
        image7.setLabel("Jährliche Übersicht der interessierten Agenten");
        image7.setIcon(IRPact.ICON_IMAGE);
        image7.setImage(IRPact.IMAGE_ANNUAL_INTEREST_OVERVIEW_PNG);
        image7.setDescription("Zeigt die jährliche Anzahl der interessierten Agenten.");

        Section image8 = new Section();
        image8.setPriority(8);
        image8.setLabel("Kumulierte jährliche Übersicht der interessierten Agenten");
        image8.setIcon(IRPact.ICON_IMAGE);
        image8.setImage(IRPact.IMAGE_ANNUAL_CUMULATED_INTEREST_OVERVIEW_PNG);
        image8.setDescription("Zeigt die jährliche kumulierte Anzahl der interessierten Agenten.");

        Section image9 = new Section();
        image9.setPriority(9);
        image9.setLabel("Jährliche kumulierte Adoptionen im Vergleich");
        image9.setIcon(IRPact.ICON_IMAGE);
        image9.setImage(IRPact.IMAGE_COMPARED_CUMULATED_ANNUAL_ADOPTIONS_PNG);
        image9.setDescription("Zeigt die jährlichen kumulierten Adoptionen für die einzelnen Postleitzahlen im Vergleich zu realen Daten.");

        Section image10 = new Section();
        image10.setPriority(10);
        image10.setLabel("Jährliche kumulierte Adoptionen im Vergleich (PLZ)");
        image10.setIcon(IRPact.ICON_IMAGE);
        image10.setImage(IRPact.IMAGE_COMPARED_CUMULATED_ANNUAL_ADOPTIONS_ZIP_PNG);
        image10.setDescription("Zeigt die jährlichen kumulierten Adoptionen für die einzelnen Postleitzahlen im Vergleich zu realen Daten.");

        adoptionSections.addAll(image1, image2, image3, image4, image5, image9, image7, image8, image9, image10);

        return adoptionSection;
    }

    @SuppressWarnings("SameParameterValue")
    protected Section buildQuantilSection(int priority) {
        Section adoptionSection = new Section();
        adoptionSection.setPriority(priority);
        adoptionSection.setLabel("Quantile");
        adoptionSection.setDescription("todo");
        adoptionSection.setIcon(IRPact.ICON_IMAGES);

        Sections adoptionSections = adoptionSection.getSections();

        Section image1 = new Section();
        image1.setPriority(1);
        image1.setLabel("NPV");
        image1.setIcon(IRPact.ICON_IMAGE);
        image1.setImage(IRPact.IMAGE_QUANTILE_NPV_PNG);
        image1.setDescription("todo");

        Section image2 = new Section();
        image2.setPriority(2);
        image2.setLabel("ENV");
        image2.setIcon(IRPact.ICON_IMAGE);
        image2.setImage(IRPact.IMAGE_QUANTILE_ENV_PNG);
        image2.setDescription("todo");

        Section image3 = new Section();
        image3.setPriority(3);
        image3.setLabel("NOV");
        image3.setIcon(IRPact.ICON_IMAGE);
        image3.setImage(IRPact.IMAGE_QUANTILE_NOV_PNG);
        image3.setDescription("todo");

        Section image4 = new Section();
        image4.setPriority(4);
        image4.setLabel("SOCIAL");
        image4.setIcon(IRPact.ICON_IMAGE);
        image4.setImage(IRPact.IMAGE_QUANTILE_SOCIAL_PNG);
        image4.setDescription("todo");

        Section image5 = new Section();
        image5.setPriority(5);
        image5.setLabel("LOCAL");
        image5.setIcon(IRPact.ICON_IMAGE);
        image5.setImage(IRPact.IMAGE_QUANTILE_LOCAL_PNG);
        image5.setDescription("todo");

        Section image6 = new Section();
        image6.setPriority(6);
        image6.setLabel("UTILITY");
        image6.setIcon(IRPact.ICON_IMAGE);
        image6.setImage(IRPact.IMAGE_QUANTILE_UTILITY_PNG);
        image6.setDescription("todo");

        adoptionSections.addAll(image1, image2, image3, image4, image5, image6);

        return adoptionSection;
    }

    @SuppressWarnings("SameParameterValue")
    protected Section buildAnnualBucketSection(int priority) {
        Section adoptionSection = new Section();
        adoptionSection.setPriority(priority);
        adoptionSection.setLabel("Bereichsanalyse");
        adoptionSection.setDescription("todo");
        adoptionSection.setIcon(IRPact.ICON_IMAGES);

        Sections adoptionSections = adoptionSection.getSections();

        Section image1 = new Section();
        image1.setPriority(1);
        image1.setLabel("NPV");
        image1.setIcon(IRPact.ICON_IMAGE);
        image1.setImage(IRPact.IMAGE_BUCKET_NPV_PNG);
        image1.setDescription("todo");

        Section image2 = new Section();
        image2.setPriority(2);
        image2.setLabel("ENV");
        image2.setIcon(IRPact.ICON_IMAGE);
        image2.setImage(IRPact.IMAGE_BUCKET_ENV_PNG);
        image2.setDescription("todo");

        Section image3 = new Section();
        image3.setPriority(3);
        image3.setLabel("NOV");
        image3.setIcon(IRPact.ICON_IMAGE);
        image3.setImage(IRPact.IMAGE_BUCKET_NOV_PNG);
        image3.setDescription("todo");

        Section image4 = new Section();
        image4.setPriority(4);
        image4.setLabel("SOCIAL");
        image4.setIcon(IRPact.ICON_IMAGE);
        image4.setImage(IRPact.IMAGE_BUCKET_SOCIAL_PNG);
        image4.setDescription("todo");

        Section image5 = new Section();
        image5.setPriority(5);
        image5.setLabel("LOCAL");
        image5.setIcon(IRPact.ICON_IMAGE);
        image5.setImage(IRPact.IMAGE_BUCKET_LOCAL_PNG);
        image5.setDescription("todo");

        Section image6 = new Section();
        image6.setPriority(6);
        image6.setLabel("UTILITY");
        image6.setIcon(IRPact.ICON_IMAGE);
        image6.setImage(IRPact.IMAGE_BUCKET_UTILITY_PNG);
        image6.setDescription("todo");

        adoptionSections.addAll(image1, image2, image3, image4, image5, image6);

        return adoptionSection;
    }

    @SuppressWarnings("SameParameterValue")
    protected Section buildCustomSection(int priority) {
        Section customSection = new Section();
        customSection.setPriority(priority);
        customSection.setLabel("Benutzerdefiniert");
        customSection.setDescription("todo");
        customSection.setIcon(IRPact.ICON_IMAGES);

        for(int i = 0; i < IRPact.CUSTOM_IMAGE_SECTION_SIZE; i++) {
            int imageId = i + 1;
            Section image = new Section();
            image.setPriority(i);
            image.setLabel("Bild " + imageId);
            image.setIcon(IRPact.ICON_IMAGE);
            image.setImage(IRPact.getCustomImagePng(imageId));
            image.setDescription("todo");
            customSection.getSections().add(image);
        }

        return customSection;
    }

    protected void handleOptAct(Sections sections) {
        //sections.getList().forEach(s -> System.out.println(">>> " + s.getLabel()));
        Section section = sections.findByLabel("set_side_cust");
        section.setPriority(100);
    }
}
