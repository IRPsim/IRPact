package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.ResourceLoader;
import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.util.xlsx.XlsxUtil;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.consumer.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.ra.npv.NPVCalculator;
import de.unileipzig.irpact.core.process.ra.npv.NPVData;
import de.unileipzig.irpact.core.process.ra.npv.NPVMatrix;
import de.unileipzig.irpact.core.process.ra.npv.NPVXlsxData;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.spatial.DataType;
import de.unileipzig.irpact.core.spatial.SimpleSpatialAttribute;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;

/**
 * @author Daniel Abitz
 */
public class RAProcessModel extends NameableBase implements ProcessModel {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RAProcessModel.class);

    protected SimulationEnvironment environment;

    protected RADataSupplier orientationSupplier;
    protected RADataSupplier slopeSupplier;

    protected NPVData npvData;
    protected NPVCalculator npvCalculator;
    protected RAModelData modelData;
    protected Rnd rnd;

    public RAProcessModel() {
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    public void setModelData(RAModelData modelData) {
        this.modelData = modelData;
    }

    public RAModelData getModelData() {
        return modelData;
    }

    public void setRnd(Rnd rnd) {
        this.rnd = rnd;
    }

    public void setOrientationSupplier(RADataSupplier orientationSupplier) {
        this.orientationSupplier = orientationSupplier;
    }

    public void setSlopeSupplier(RADataSupplier slopeSupplier) {
        this.slopeSupplier = slopeSupplier;
    }

    @Override
    public void initialize() {
        loadNPVData();

        if(npvData != null) {
            npvCalculator = new NPVCalculator();
            npvCalculator.setData(npvData);
        }
    }

    @SuppressWarnings("UnnecessaryReturnStatement")
    private void loadNPVData() {
        if(tryLoadXlxsFromResources()) {
            return;
        }
        //hier kommt noch mehr
    }

    private boolean tryLoadXlxsFromResources() {
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "try loading npv data from resources ('{}')", RAConstants.XLSX_FILE);
        final InputStream in = ResourceLoader.open(RAConstants.XLSX_FILE);
        if(in == null) {
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "npv data not found in resources");
            return false;
        }
        try {
            try {
                LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "parsing xlsx");
                XSSFWorkbook book = new XSSFWorkbook(in);
                NPVXlsxData xlsxData = new NPVXlsxData();
                xlsxData.setAllgemeinSheet(XlsxUtil.extractKeyValueTable(book.getSheetAt(0)));
                xlsxData.putAllTables(XlsxUtil.extractTablesWithTwoHeaderLines(book, 1));
                LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "parsing finished");
                npvData = xlsxData;
                return true;
            } finally {
                in.close();
            }
        } catch (Exception e) {
            LOGGER.error("npv data loading failed", e);
            return false;
        }
    }

    @Override
    public void validate() throws ValidationException {
        if(npvData == null) {
            throw new ValidationException("npv data missing");
        }
        if(npvCalculator == null) {
            throw new ValidationException("npv calculator missing");
        }
        checkSpatialInformation();
    }

    private void checkSpatialInformation() throws ValidationException {
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            for(ConsumerAgent ca: cag.getAgents()) {
                SpatialInformation spaInf = ca.getSpatialInformation();
                if(spaInf == null) {
                    throw new ValidationException("missing spatial information in consumer agent '" +  ca.getName() + "'");
                }
                if(!spaInf.hasAttribute(RAConstants.ORIENTATION)) {
                    addOrientationFor(ca);
                }
                if(!spaInf.hasAttribute(RAConstants.SLOPE)) {
                    addSlopeTo(ca);
                }
            }
        }
    }

    private void addOrientationFor(ConsumerAgent ca) throws ValidationException {
        if(orientationSupplier == null) {
            throw new ValidationException("missing orientation supplier");
        }
        SpatialInformation spaInf = ca.getSpatialInformation();
        SimpleSpatialAttribute attr = new SimpleSpatialAttribute(DataType.DOUBLE);
        attr.setName(RAConstants.ORIENTATION);
        attr.setDoubleValue(orientationSupplier.drawDoubleValue());
        spaInf.addAttribute(attr);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "added orientation={} to agent '{}'", attr.getDoubleValue(), ca.getName());
    }

    private void addSlopeTo(ConsumerAgent ca) throws ValidationException {
        if(orientationSupplier == null) {
            throw new ValidationException("missing slope supplier");
        }
        SpatialInformation spaInf = ca.getSpatialInformation();
        SimpleSpatialAttribute attr = new SimpleSpatialAttribute(DataType.DOUBLE);
        attr.setName(RAConstants.SLOPE);
        attr.setDoubleValue(slopeSupplier.drawDoubleValue());
        spaInf.addAttribute(attr);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "added slope={} to agent '{}'", attr.getDoubleValue(), ca.getName());
    }

    @Override
    public void setup() {
        int startYear = environment.getTimeModel().getStartYear();
        int endYear = environment.getTimeModel().getEndYearInclusive();

        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "calculating npv matrix from '{}' to '{}'", startYear, endYear);
        for(int y = startYear; y <= endYear; y++) {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "> '{}'", y);
            NPVMatrix matrix = new NPVMatrix();
            matrix.calculate(npvCalculator, y);
            modelData.put(y, matrix);
        }
    }

    @Override
    public void onNewSimulationPeriod() {
        AgentManager agentManager = environment.getAgents();
        agentManager.streamConsumerAgents()
                .forEach(this::setupAgentForNewPeriod);
    }

    protected void setupAgentForNewPeriod(ConsumerAgent agent) {
        double renovationRate = RAProcessPlan.getRenovationRate(agent);
        boolean doRenovation = rnd.nextDouble() < renovationRate;
        setUnderRenovation(agent, doRenovation);

        double constructionRate = RAProcessPlan.getConstructionRate(agent);
        boolean doConstruction = rnd.nextDouble() < constructionRate;
        setUnderConstruction(agent, doConstruction);
    }

    @Override
    public ProcessPlan newPlan(Agent agent, Need need, Product product) {
        ConsumerAgent cAgent = cast(agent);
        Rnd rnd = environment.getSimulationRandom().createNewRandom();
        return new RAProcessPlan(environment, this, rnd, cAgent, need, product);
    }

    protected static ConsumerAgent cast(Agent agent) {
        if(agent instanceof ConsumerAgent) {
            return (ConsumerAgent) agent;
        } else {
            throw new IllegalArgumentException("requires ConsumerAgent");
        }
    }

    //=========================
    //util
    //=========================

    protected void setUnderConstruction(ConsumerAgent agent, boolean value) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.UNDER_CONSTRUCTION);
        if(attr == null) {
            BasicConsumerAgentAttribute battr = new BasicConsumerAgentAttribute();
            battr.setName(RAConstants.UNDER_CONSTRUCTION);
            battr.setDoubleValue(value ? 1.0 : 0.0);
            battr.setGroup(MockConsumerAgentGroupAttribute.INSTANCE);
            agent.addAttribute(battr);
        } else {
            attr.setDoubleValue(value ? 1.0 : 0.0);
        }
    }

    protected void setUnderRenovation(ConsumerAgent agent, boolean value) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.UNDER_RENOVATION);
        if(attr == null) {
            BasicConsumerAgentAttribute battr = new BasicConsumerAgentAttribute();
            battr.setName(RAConstants.UNDER_RENOVATION);
            battr.setDoubleValue(value ? 1.0 : 0.0);
            battr.setGroup(MockConsumerAgentGroupAttribute.INSTANCE);
            agent.addAttribute(battr);
        } else {
            attr.setDoubleValue(value ? 1.0 : 0.0);
        }
    }
}
