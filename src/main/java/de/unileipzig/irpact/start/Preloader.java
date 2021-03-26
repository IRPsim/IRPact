package de.unileipzig.irpact.start;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.res.BasicResourceLoader;
import de.unileipzig.irpact.commons.res.ResourceLoader;
import de.unileipzig.irpact.commons.util.IRPactJson;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.log.SectionLoggingFilter;
import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.spec2.SpecificationConverter2;
import de.unileipzig.irpact.io.spec2.SpecificationData2;
import de.unileipzig.irpact.start.optact.OptAct;
import de.unileipzig.irptools.io.ContentType;
import de.unileipzig.irptools.io.ContentTypeDetector;
import de.unileipzig.irptools.io.annual.AnnualFile;
import de.unileipzig.irptools.io.base.AnnualEntry;
import de.unileipzig.irptools.io.base.DataEntry;
import de.unileipzig.irptools.io.base.Scalars;
import de.unileipzig.irptools.io.base.Sets;
import de.unileipzig.irptools.io.downloaded.DownloadedFile;
import de.unileipzig.irptools.io.perennial.PerennialData;
import de.unileipzig.irptools.io.perennial.PerennialFile;
import de.unileipzig.irptools.start.IRPtools;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Iterator;

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

    private final CommandLineOptions clOptions;
    private final Collection<? extends IRPactCallback> callbacks;
    private ResourceLoader resourceLoader;

    public Preloader(CommandLineOptions clOptions, Collection<? extends IRPactCallback> callbacks) {
        this.clOptions = clOptions;
        this.callbacks = callbacks;
    }

    public void start() throws Throwable {
        setup();
        if(clOptions.isCallIRPtools()) {
            runIRPTools();
            return;
        }
        if(clOptions.hasSpecInputDirPath()) {
            convertSpecToParam();
            return;
        }
        load();
    }

    private void runIRPTools() {
        LOGGER.info("executing IRPtools");
        SectionLoggingFilter filter = new SectionLoggingFilter();
        IRPSection.addAllToolsTo(filter);
        IRPLogging.setFilter(filter);
        IRPtools.setLoggingFilter(filter);
        IRPSection.addSectionsToTools();
        IRPtools.main(clOptions.getArgs());
    }

    private void setup() {
        if(clOptions.hasLogPath()) {
            IRPLogging.initFile(clOptions.getLogPath());
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "log file: {}", clOptions.getLogPath());
        }
        if(clOptions.isCallIRPtools()) {
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "call IRPtools");
        }
        if(clOptions.hasSpecOutputDirPath()) {
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "call specification converter");
        }
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "input file: {}", clOptions.getInputPath());
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "output file: {}", clOptions.getOutputPath());
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "image file: {}", clOptions.getImagePath());
        if(clOptions.isNoSimulation()) {
            LOGGER.debug("simulation disabled");
        } else {
            LOGGER.debug("simulation enabled");
        }

        BasicResourceLoader loader = new BasicResourceLoader();
        loader.setDir(clOptions.getDataDirPath());
        resourceLoader = loader;
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "data dir: {}", clOptions.getDataDirPath());
    }

    private void load() throws Exception {
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

    private void loadSpec() throws Exception {
        SpecificationConverter2 converter = new SpecificationConverter2();
        InRoot root =  converter.toParam(clOptions.getSpecInputDirPath());
        AnnualEntry<InRoot> entry = new AnnualEntry<>(root, IRPactJson.JSON.createObjectNode());
        entry.getConfig().init();
        entry.getConfig().setYear(root.general.startYear);

        LOGGER.debug("call IRPact with spec");
        IRPact irpact = createIRPactInstance();
        irpact.start(entry);
        LOGGER.debug("IRPact finished");
    }

    private void loadJson() throws Exception {
        ObjectNode root = IRPactJson.readJson(clOptions.getInputPath());
        if(clOptions.hasSpecOutputDirPath()) {
            convertParamToSpec(root);
            return;
        }

        load(root);
    }

    private void convertParamToSpec(ObjectNode root) throws Exception {
        LOGGER.debug("convert parameter to specification");
        AnnualEntry<InRoot> entry = IRPact.convert(clOptions, root);
        InRoot inRoot = entry.getData();
        inRoot.general.startYear = entry.getConfig().getYear(); //!
        SpecificationConverter2 converter = new SpecificationConverter2();
        SpecificationData2 data = converter.toSpec(inRoot);
        data.store(clOptions.getSpecOutputDirPath());
    }

    private void convertSpecToParam() throws Exception {
        LOGGER.debug("convert specification to parameter");
        SpecificationConverter2 converter = new SpecificationConverter2();
        InRoot root =  converter.toParam(clOptions.getSpecInputDirPath());
        PerennialData<InRoot> pData = new PerennialData<>();
        pData.add(root.general.startYear, root);
        PerennialFile pFile = pData.serialize(IRPact.getInputConverter(clOptions));
        pFile.store(clOptions.getOutputPath(), StandardCharsets.UTF_8);
        LOGGER.debug(IRPSection.SPECIFICATION_CONVERTER, "param file stored: '{}'", clOptions.getOutputPath());
    }

    private void load(ObjectNode root) throws Exception {
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
            LOGGER.error("years.szie() == 0");
            return SubModul.UNKNOWN;
        }
        checkVersion(file.getYears().get(0).getSets());
        Scalars scalars = file.getYears().get(0).getScalars();
        return detectModul(scalars);
    }

    private SubModul detectModulInDownloadedFile(DownloadedFile file) throws Exception {
        if(file.getData().size() == 0) {
            LOGGER.error("data.szie() == 0");
            return SubModul.UNKNOWN;
        }
        DataEntry entry0 = file.getData().get(0);
        if(entry0.getYears().size() == 0) {
            LOGGER.error("years.szie() == 0");
            return SubModul.UNKNOWN;
        }
        checkVersion(entry0.getYears().get(0).getSets());
        Scalars scalars = entry0.getYears().get(0).getScalars();
        return detectModul(scalars);
    }

    private SubModul detectModul(Scalars scalars) {
        JsonNode optactNode = scalars.getNode(InGeneral.RUN_OPTACT_DEMO_PARAM_NAME);
        if(optactNode == null) {
            LOGGER.error("'{}' not found", InGeneral.RUN_OPTACT_DEMO_PARAM_NAME);
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
            LOGGER.error(" unsupported node tpye: '{}'", optactNode.getNodeType());
            return SubModul.UNKNOWN;
        }
        return optact
                ? SubModul.OPTACT
                : SubModul.IRPACT;
    }

    private void callIRPact(ObjectNode root) throws Exception {
        LOGGER.debug("call IRPact with param");
        IRPact irpact = createIRPactInstance();
        irpact.start(root);
        LOGGER.debug("IRPact finished");
    }

    private void callOptact(ObjectNode root) {
        OptAct optact = new OptAct(clOptions, root);
        optact.start();
    }
}
