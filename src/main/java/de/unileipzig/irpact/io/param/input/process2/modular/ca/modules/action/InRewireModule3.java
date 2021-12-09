package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.action;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.action.RewireModule2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.input.process2.modular.util.CAMPMGraphSettings;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.graph.GraphNode;
import de.unileipzig.irptools.defstructure.annotation.graph.Subsets;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODEL4_PVACTMODULES_ACTION_REWIRE;

/**
 * @author Daniel Abitz
 */
@Definition(
//        graphNode = @GraphNode(
//                id = MODULAR_GRAPH,
//                label = ACTION_LABEL,
//                shape = ACTION_SHAPE,
//                color = ACTION_COLOR,
//                border = ACTION_BORDER,
//                tags = {ACTION_GRAPHNODE}
//        )
        graphNode3 = @GraphNode(
                graphId = CAMPMGraphSettings.GRAPH_ID,
                subsetsColor = @Subsets(
                        value = CAMPMGraphSettings.ACTION_COLOR
                ),
                subsetsBorder = @Subsets(
                        value = CAMPMGraphSettings.ACTION_BORDER
                ),
                subsetsShape = @Subsets(
                        value = CAMPMGraphSettings.ACTION_SHAPE
                )
        )
)
@LocalizedUiResource.PutClassPath(PROCESS_MODEL4_PVACTMODULES_ACTION_REWIRE)
public class InRewireModule3 implements InConsumerAgentActionModule2 {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(TreeAnnotationResource res) {
//        setShapeColorFillBorder(res, thisClass(), ACTION_SHAPE, ACTION_COLOR, ACTION_FILL, ACTION_BORDER);
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    @DefinitionName
    public String name;
    @Override
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double placeholder = 0;

    public InRewireModule3() {
    }

    public InRewireModule3(String name) {
        setName(name);
    }

    @Override
    public InRewireModule3 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InRewireModule3 newCopy(CopyCache cache) {
        InRewireModule3 copy = new InRewireModule3();
        return Dev.throwException();
    }

    @Override
    public RewireModule2 parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        RewireModule2 module = new RewireModule2();
        module.setName(getName());

        return module;
    }
}
