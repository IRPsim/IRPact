package de.unileipzig.irpact.io.param.input.process.modular.ca;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentEvaluationModule;
import de.unileipzig.irpact.core.process.modular.ca.model.SimpleConsumerAgentMPM;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.InConsumerAgentEvaluationModule;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InSimpleConsumerAgentMPM implements InConsumerAgentModularProcessModel {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR2_MODEL_SIMPLE);

        addEntry(res, thisClass(), "startModule");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;
    @Override
    public String getName() {
        return _name;
    }
    public void setName(String name) {
        this._name = name;
    }

    @FieldDefinition
    public InConsumerAgentEvaluationModule[] startModule;
    public void setStartModule(InConsumerAgentEvaluationModule startModule) {
        this.startModule = new InConsumerAgentEvaluationModule[]{startModule};
    }
    public InConsumerAgentEvaluationModule getStartModule() throws ParsingException {
        return ParamUtil.getInstance(startModule, "startModule");
    }

    public InSimpleConsumerAgentMPM() {
    }

    @Override
    public InSimpleConsumerAgentMPM copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InSimpleConsumerAgentMPM newCopy(CopyCache cache) {
        InSimpleConsumerAgentMPM copy = new InSimpleConsumerAgentMPM();
        return Dev.throwException();
    }

    @Override
    public SimpleConsumerAgentMPM parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            ProcessModel model = MPMSettings.searchModel(parser, getName());
            if(model instanceof SimpleConsumerAgentMPM) {
                return (SimpleConsumerAgentMPM) model;
            } else {
                throw new ParsingException("class mismatch");
            }
        }

        SimpleConsumerAgentMPM model = new SimpleConsumerAgentMPM();
        model.setName(getName());
        model.setEnvironment(parser.getEnvironment());

        Rnd rnd = parser.deriveRnd();
        model.setRnd(rnd);

        ConsumerAgentEvaluationModule startModule = parser.parseEntityTo(getStartModule());
        model.setStartModule(startModule);

        return model;
    }
}
