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
import de.unileipzig.irpact.io.spec.SpecificationConverter;
import de.unileipzig.irpact.io.spec.SpecificationManager;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    private final Start param;
    private ResourceLoader resourceLoader;

    public Preloader(Start param) {
        this.param = param;
    }

    public void start() throws Throwable {
        setup();
        if(param.isCallIRPtools()) {
            runIRPTools();
            return;
        }
        if(param.hasSpecInputDirPath()) {
            convertSpecToParam();
            return;
        }
        load();
    }

    private void runIRPTools() {
        LOGGER.info("executing IRPtools");
        SectionLoggingFilter filter = new SectionLoggingFilter();
        filter.add(IRPSection.TOOLS);
        IRPLogging.setFilter(filter);
        IRPtools.setLoggingFilter(filter);
        IRPtools.setLoggingSection(IRPSection.TOOLS);
        IRPtools.main(param.getArgs());
    }

    private void setup() {
        if(param.hasLogPath()) {
            IRPLogging.initFile(param.getLogPath());
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "log file: {}", param.getLogPath());
        }
        if(param.isCallIRPtools()) {
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "call IRPtools");
        }
        if(param.hasSpecOutputDirPath()) {
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "call specification converter");
        }
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "input file: {}", param.getInputPath());
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "output file: {}", param.getOutputPath());
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "image file: {}", param.getImagePath());
        if(param.isNoSimulation()) {
            LOGGER.debug("simulation disabled");
        } else {
            LOGGER.debug("simulation enabled");
        }

        BasicResourceLoader loader = new BasicResourceLoader();
        loader.setDir(param.getDataDirPath());
        resourceLoader = loader;
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "data dir: {}", param.getDataDirPath());
    }

    private void load() throws Exception {
        ObjectNode root = IRPactJson.readJson(param.getInputPath());
        if(param.hasSpecOutputDirPath()) {
            convertParamToSpec(root);
            return;
        }

        load(root);
    }

    private void convertParamToSpec(ObjectNode root) throws Exception {
        LOGGER.debug("convert parameter to specification");
        AnnualEntry<InRoot> entry = IRPact.convert(root);
        InRoot inRoot = entry.getData();
        inRoot.general.startYear = entry.getConfig().getYear(); //!
        SpecificationConverter converter = new SpecificationConverter();
        SpecificationManager manager = converter.toSpec(inRoot);
        manager.store(param.getSpecOutputDirPath());
    }

    private void convertSpecToParam() throws IOException {
        LOGGER.debug("convert specification to parameter");
        SpecificationManager manager = new SpecificationManager(IRPactJson.JSON);
        manager.load(param.getSpecInputDirPath());
        SpecificationConverter converter = new SpecificationConverter();
        InRoot root = converter.toParam(manager);
        PerennialData<InRoot> pData = new PerennialData<>();
        pData.add(root.general.startYear, root);
        PerennialFile pFile = pData.serialize(IRPact.getConverter());
        pFile.store(param.getOutputPath(), StandardCharsets.UTF_8);
        LOGGER.debug(IRPSection.SPECIFICATION_CONVERTER, "param file stored: '{}'", param.getOutputPath());
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
        IRPact irpact = new IRPact(param, resourceLoader);
        irpact.start(root);
    }

    private void callOptact(ObjectNode root) {
        OptAct optact = new OptAct(param, root);
        optact.start();
    }
}
