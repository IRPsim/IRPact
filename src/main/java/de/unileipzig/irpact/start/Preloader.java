package de.unileipzig.irpact.start;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.resource.BasicResourceLoader;
import de.unileipzig.irpact.commons.resource.ResourceLoader;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.develop.Todo;
import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irpact.start.irpact.IRPactCallback;
import de.unileipzig.irpact.start.irpact.IRPactExecutor;
import de.unileipzig.irpact.start.irpact.executors.IRPactExecutors;
import de.unileipzig.irpact.start.optact.OptAct;
import de.unileipzig.irptools.io.ContentType;
import de.unileipzig.irptools.io.ContentTypeDetector;
import de.unileipzig.irptools.io.annual.AnnualFile;
import de.unileipzig.irptools.io.base.data.AnnualEntry;
import de.unileipzig.irptools.io.base.file.DataElementWithYears;
import de.unileipzig.irptools.io.base.file.Scalars;
import de.unileipzig.irptools.io.base.file.Sets;
import de.unileipzig.irptools.io.downloaded.DownloadedFile;
import de.unileipzig.irptools.io.perennial.PerennialFile;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.nio.file.Files;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class Preloader {

    private enum SubModul {
        UNKNOWN,
        IRPACT,
        OPTACT
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(Preloader.class);

    private final MainCommandLineOptions clOptions;
    private final Collection<? extends IRPactCallback> callbacks;
    private ResourceLoader resourceLoader;

    public Preloader(MainCommandLineOptions clOptions, Collection<? extends IRPactCallback> callbacks) {
        this.clOptions = clOptions;
        this.callbacks = callbacks;
    }

    public void start() throws Throwable {
        setup();
        load();
    }

    private void setup() {
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "input file: {}", clOptions.getInputPath());
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "output file: {}", clOptions.getOutputPath());
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "image file: {}", clOptions.getImagePath());
        if(clOptions.isNoSimulation()) {
            LOGGER.trace("simulation disabled");
        } else {
            LOGGER.trace("simulation enabled");
        }

        BasicResourceLoader loader = new BasicResourceLoader();
        loader.setExternalPath(clOptions.getDataDirPath());
        resourceLoader = loader;
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "data dir: {}", clOptions.getDataDirPath());
    }

    private void load() throws Throwable {
        if(Files.isDirectory(clOptions.getInputPath())) {
            loadSpec();
        }
        else {
            loadJson();
        }
    }

    private IRPact createIRPactInstance() {
        return new IRPact(clOptions, callbacks, resourceLoader);
    }

    private void start(IRPact irpact) throws Throwable {
        int runMode = irpact.getInRoot().general.runMode;
        IRPactExecutor exec;
        if(IRPactExecutors.isRegistered(runMode)) {
            exec = IRPactExecutors.find(runMode);
        } else {
            exec = IRPactExecutors.find(clOptions.getRunMode());
        }
        exec.execute(irpact);
    }

    @Todo
    private void loadSpec() throws Throwable {
        throw new UnsupportedOperationException("TODO REMOVE");
//        SpecificationConverter converter = new SpecificationConverter();
//        InRoot root =  converter.toParam(clOptions.getInputPath());
//        AnnualEntry<InRoot> entry = new AnnualEntry<>(root, JsonUtil.JSON.createObjectNode());
//        entry.getConfig().init();
//        entry.getConfig().setYear(root.general.getFirstSimulationYear());
//
//        LOGGER.trace("call IRPact with spec");
//        IRPact irpact = createIRPactInstance();
//        irpact.init(entry);
//        start(irpact);
//        LOGGER.trace("IRPact finished");
    }

    private void loadJson() throws Throwable {
        ObjectNode root = JsonUtil.readJson(clOptions.getInputPath());
        checkForForcedSettings(root);
        load(root);
    }

    private void load(ObjectNode root) throws Throwable {
        SubModul modul = detectModul(root);
        switch (modul) {
            case IRPACT:
                callIRPact(root);
                break;

            case OPTACT:
                callOptact(root);
                break;

            case UNKNOWN:
                throw new IllegalArgumentException("file type not detected");

            default:
                throw new IllegalArgumentException("unknown modul: " + modul);
        }
    }

    private SubModul detectModul(ObjectNode root) throws Exception {
        ContentType type = ContentTypeDetector.detect(root);
        switch (type) {
            case ANNUAL:
                AnnualFile af = new AnnualFile(root);
                return detectModulInAnnualFile(af);

            case PERENNIAL:
                PerennialFile pf = new PerennialFile(root);
                return detectModulInPerennialFile(pf);

            case DOWNLOADED:
                DownloadedFile df = new DownloadedFile(root);
                return detectModulInDownloadedFile(df);

            case EMPTY:
            case UNKNOWN:
            default:
                return SubModul.UNKNOWN;
        }
    }

    private void checkVersion(Sets setsNode) throws Exception {
        JsonNode versionNode = setsNode.getNode(InRoot.SET_VERSION);
        if(versionNode == null) {
            throw new Exception("'" + InRoot.SET_VERSION + "' not found");
        }
        if(versionNode.isObject()) {
            ObjectNode versionObj = (ObjectNode) versionNode;
            if(versionObj.size() != 1) {
                throw new Exception("version node not valid (size != 1)");
            }
            Iterator<String> iter = versionNode.fieldNames();
            String inputVersion = iter.next();
            checkVersion(inputVersion);
        } else {
            throw new Exception("version node not valid (no object)");
        }
    }

    private void checkVersion(String inputVersion) {
        LOGGER.info("IRPact version: {}, input version: {}", IRPact.VERSION, inputVersion);
    }

    private SubModul detectModulInAnnualFile(AnnualFile file) throws Exception {
        checkVersion(file.getYear().getSets());
        Scalars scalars = file.getYear().getScalars();
        return detectModul(scalars);
    }

    private SubModul detectModulInPerennialFile(PerennialFile file) throws Exception {
        if(file.getYears().size() == 0) {
            LOGGER.error("years.size() == 0");
            return SubModul.UNKNOWN;
        }
        checkVersion(file.getYears().get(0).getSets());
        Scalars scalars = file.getYears().get(0).getScalars();
        return detectModul(scalars);
    }

    private SubModul detectModulInDownloadedFile(DownloadedFile file) throws Exception {
        if(file.getData().size() == 0) {
            LOGGER.error("data.size() == 0");
            return SubModul.UNKNOWN;
        }
        DataElementWithYears entry0 = file.getData().get(0);
        if(entry0.getYears().size() == 0) {
            LOGGER.error("years.size() == 0");
            return SubModul.UNKNOWN;
        }
        checkVersion(entry0.getYears().get(0).getSets());
        Scalars scalars = entry0.getYears().get(0).getScalars();
        return detectModul(scalars);
    }

    private SubModul detectModul(Scalars scalars) {
        JsonNode optactNode = scalars.getNode(InGeneral.SCA_INGENERAL_RUNOPTACTDEMO);
        if(optactNode == null) {
            LOGGER.error("'{}' not found", InGeneral.SCA_INGENERAL_RUNOPTACTDEMO);
            return SubModul.UNKNOWN;
        }
        boolean optact;
        if(optactNode.isNumber()) {
            optact = optactNode.intValue() == 1;
        }
        else if(optactNode.isBoolean()) {
            optact = optactNode.booleanValue();
        }
        else {
            LOGGER.error("unsupported node tpye: '{}'", optactNode.getNodeType());
            return SubModul.UNKNOWN;
        }
        return optact
                ? SubModul.OPTACT
                : SubModul.IRPACT;
    }

    private void callOptact(ObjectNode root) {
        OptAct optact = new OptAct(clOptions, root);
        optact.start();
    }

    private void callIRPact(ObjectNode root) throws Throwable {
        LOGGER.trace("call IRPact with param");
        IRPact irpact = createIRPactInstance();
        irpact.init(root);
        start(irpact);
    }

    public void start(AnnualEntry<InRoot> root) throws Throwable {
        checkForForcedSettings(root);
        setup();
        callIRPact(root);
    }

    private void callIRPact(AnnualEntry<InRoot> root) throws Throwable {
        LOGGER.trace("call IRPact with InRoot");
        IRPact irpact = createIRPactInstance();
        irpact.init(root);
        start(irpact);
    }

    private void checkForForcedSettings(ObjectNode root) {
        List<JsonNode> nodes = JsonUtil.findAllNodes(root, InGeneral.SCA_INGENERAL_FORCELOGTOCONSOLE);
        if(nodes.size() > 0) {
            JsonNode node = nodes.get(0);
            if(node.isNumber() && node.intValue() == 1) {
                forceLoggingToConsole();
            }
        }
    }

    private void checkForForcedSettings(AnnualEntry<InRoot> root) {
        InRoot r = root.getData();
        if(r.getGeneral().isForceLogToConsole()) {
            forceLoggingToConsole();
        }
    }

    private void forceLoggingToConsole() {
        IRPLogging.forceWriteToConsoleAndFile();
        LOGGER.warn("force logging to console and file (initial args: {})", clOptions.printArgs());
    }
}
