package de.unileipzig.irpact.core.postprocessing.data4;

import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.network.filter.NodeFilterScheme;
import de.unileipzig.irpact.core.postprocessing.data3.FileType;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.io.param.input.postdata.InNeighborhoodOverview;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public class NeighbourhoodOverview extends AbstractDataHandler<InNeighborhoodOverview> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(NeighbourhoodOverview.class);

    public NeighbourhoodOverview(DataProcessor4 processor, InNeighborhoodOverview dataConfiguration) {
        super(processor, dataConfiguration);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected String getResourceKey() {
        return "NEIGHBOURHOOD_OVERVIEW";
    }

    @Override
    public void init() throws Throwable {
    }

    protected String getBaseName() {
        return dataConfiguration.getBaseFileName();
    }

    @Override
    public void execute() throws Throwable {
        Product product = processor.getSingletonProduct();
        NodeFilterScheme filterScheme = dataConfiguration.getNodeFilterScheme()
                .createScheme();
        MutableDouble maxTargets = MutableDouble.negativeMaxValue();

        JsonTableData3 data = createTableData(product, filterScheme, maxTargets);

        if(dataConfiguration.isStoreCsv()) {
            storeCsvFile(data, maxTargets);
        }

        if(dataConfiguration.isStoreXlsx()) {
            storeXlsxFile(data, maxTargets);
        }
    }

    protected void storeXlsxFile(JsonTableData3 data, MutableDouble maxTargets) throws IOException {
        Path xlsxPath = getTargetFile(getBaseName() + ".xlsx");
        trace("store xlsx file '{}'", xlsxPath);
        updateHeader(FileType.XLSX, data, maxTargets.intValue());
        processor.storeXlsx(
                xlsxPath,
                getLocalizedString(FileType.XLSX, "sheetName"),
                data
        );
    }

    protected void storeCsvFile(JsonTableData3 data, MutableDouble maxTargets) throws IOException {
        Path csvPath = getTargetFile(getBaseName() + ".csv");
        trace("store csv file '{}'", csvPath);
        updateHeader(FileType.CSV, data, maxTargets.intValue());
        processor.storeCsv(
                csvPath,
                StandardCharsets.UTF_8,
                getLocalizedString(FileType.XLSX, "sep"),
                data
        );
    }

    protected JsonTableData3 createTableData(
            Product product,
            NodeFilterScheme filterScheme,
            MutableDouble maxTargets) {
        JsonTableData3 data = new JsonTableData3();

        int rowIndex = 0;
        int columnIndex;

        //data
        for(ConsumerAgent agent: getEnvironment().getAgents().iterableConsumerAgents()) {
            rowIndex++;
            columnIndex = 0;

            List<ConsumerAgent> neighbours = listNeighbours(agent, product, filterScheme);
            List<Dist> distances = calculateDistances(agent, neighbours);
            maxTargets.setMax(distances.size());

            data.setString(rowIndex, columnIndex++, agent.getName());
            data.setInt(rowIndex, columnIndex++, distances.size());
            for(Dist dist: distances) {
                data.setString(rowIndex, columnIndex++, dist.getTarget().getName());
                data.setDouble(rowIndex, columnIndex++, dist.getDist());
            }
        }

        return data;
    }

    protected void updateHeader(FileType type, JsonTableData3 data, int maxTargets) {
        int rowIndex = 0;
        int columnIndex = 0;
        data.setString(rowIndex, columnIndex++, getLocalizedString(type, "sourceAgent"));
        data.setString(rowIndex, columnIndex++, getLocalizedString(type, "targetCount"));
        for(int i = 0; i < maxTargets; i++) {
            data.setString(rowIndex, columnIndex++, getLocalizedFormattedString(type, "targetAgent", getSuffix(i)));
            data.setString(rowIndex, columnIndex++, getLocalizedFormattedString(type, "distanceValue", getSuffix(i)));
        }
    }

    protected String getSuffix(int i) {
        return "_" + (i + 1);
    }

    protected List<ConsumerAgent> listNeighbours(ConsumerAgent source, Product product, NodeFilterScheme filterScheme) {
        return processor.getRAHelperAPI()
                .streamFeasibleAndFinancialNeighbours(getEnvironment(), source, product, filterScheme.createFilter(source))
                .collect(Collectors.toList());
    }

    protected List<Dist> calculateDistances(ConsumerAgent source, List<ConsumerAgent> targets) {
        return targets.stream()
                .map(target -> new Dist(target, distance(source, target)))
                .sorted(Dist.ASC)
                .collect(Collectors.toList());
    }

    protected double distance(ConsumerAgent source, ConsumerAgent target) {
        return getEnvironment().getSpatialModel().distance(source.getSpatialInformation(), target.getSpatialInformation());
    }

    /**
     * @author Daniel Abitz
     */
    private static final class Dist {

        private static final Comparator<Dist> ASC = Comparator.comparingDouble(o -> o.dist);

        private final ConsumerAgent target;
        private final double dist;

        private Dist(ConsumerAgent target, double dist) {
            this.target = target;
            this.dist = dist;
        }

        private ConsumerAgent getTarget() {
            return target;
        }

        private double getDist() {
            return dist;
        }
    }
}
