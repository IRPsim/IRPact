package de.unileipzig.irpact.start;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.IRPactJson;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.LoggingPart;
import de.unileipzig.irpact.core.log.LoggingType;
import de.unileipzig.irpact.io.input.InGeneral;
import de.unileipzig.irpact.start.optact.OptAct;
import de.unileipzig.irptools.io.ContentType;
import de.unileipzig.irptools.io.ContentTypeDetector;
import de.unileipzig.irptools.io.annual.AnnualFile;
import de.unileipzig.irptools.io.base.DataEntry;
import de.unileipzig.irptools.io.base.Scalars;
import de.unileipzig.irptools.io.downloaded.DownloadedFile;
import de.unileipzig.irptools.io.perennial.PerennialFile;
import de.unileipzig.irptools.start.IRPtools;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;

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

    public Preloader(Start param) {
        this.param = param;
    }

    public void start() throws Throwable {
        setup();
        if(checkForTools()) {
            return;
        }
        load();
    }

    private boolean checkForTools() {
        if(param.isCallIRPtools()) {
            IRPtools.main(param.getArgs());
            return true;
        } else {
            return false;
        }
    }

    private void setup() {
        if(param.hasLogPath()) {
            IRPLogging.initFile(param.getLogPath());
            LOGGER.debug(LoggingType.INITIALIZATION, LoggingPart.PARAMETER, "log file: {}", param.getLogPath());
        }
        if(param.isCallIRPtools()) {
            LOGGER.debug(LoggingType.INITIALIZATION, LoggingPart.PARAMETER, "call IRPtools");
        }
        LOGGER.debug(LoggingType.INITIALIZATION, LoggingPart.PARAMETER, "input file: {}", param.getInputPath());
        LOGGER.debug(LoggingType.INITIALIZATION, LoggingPart.PARAMETER, "output file: {}", param.getOutputPath());
        LOGGER.debug(LoggingType.INITIALIZATION, LoggingPart.PARAMETER, "image file: {}", param.getImagePath());
        if(param.isNoSimulation()) {
            LOGGER.debug("simulation disabled");
        } else {
            LOGGER.debug("simulation enabled");
        }
    }

    private void load() throws IOException {
        ObjectNode root = IRPactJson.readJson(param.getInputPath());
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

    private SubModul detectModul(ObjectNode root) {
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

    private SubModul detectModulInAnnualFile(AnnualFile file) {
        Scalars scalars = file.getYear().getScalars();
        return detectModul(scalars);
    }

    private SubModul detectModulInPerennialFile(PerennialFile file) {
        if(file.getYears().size() == 0) {
            LOGGER.error("years.szie() == 0");
            return SubModul.UNKNOWN;
        }
        Scalars scalars = file.getYears().get(0).getScalars();
        return detectModul(scalars);
    }

    private SubModul detectModulInDownloadedFile(DownloadedFile file) {
        if(file.getData().size() == 0) {
            LOGGER.error("data.szie() == 0");
            return SubModul.UNKNOWN;
        }
        DataEntry entry0 = file.getData().get(0);
        if(entry0.getYears().size() == 0) {
            LOGGER.error("years.szie() == 0");
            return SubModul.UNKNOWN;
        }
        Scalars scalars = entry0.getYears().get(0).getScalars();
        return detectModul(scalars);
    }

    private SubModul detectModul(Scalars scalars) {
        JsonNode optactNode = scalars.getNode(InGeneral.OPT_ACT_PAR_NAME);
        if(optactNode == null) {
            LOGGER.error("'{}' not found", InGeneral.OPT_ACT_PAR_NAME);
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

    private void callIRPact(ObjectNode root) {
        IRPact irpact = new IRPact(param, root);
        irpact.start();
    }

    private void callOptact(ObjectNode root) {
        OptAct optact = new OptAct(param, root);
        optact.start();
    }
}
