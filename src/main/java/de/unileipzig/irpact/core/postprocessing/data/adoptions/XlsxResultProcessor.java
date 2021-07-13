package de.unileipzig.irpact.core.postprocessing.data.adoptions;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.attribute.BasicStringAttribute;
import de.unileipzig.irpact.commons.util.xlsx.StandardCellValueConverter;
import de.unileipzig.irpact.commons.util.xlsx.XlsxSheetWriter;
import de.unileipzig.irpact.commons.util.xlsx.XlsxTable;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.postprocessing.data.ResultProcessor;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public abstract class XlsxResultProcessor implements ResultProcessor {

    protected static final Attribute NA_STR = new BasicStringAttribute("-");

    protected Path target;
    protected XlsxTable<Attribute> table;
    protected List<String> infos = new ArrayList<>();
    protected String sheetName = "output";

    public XlsxResultProcessor() {
    }

    public void setTarget(Path target) {
        this.target = target;
    }

    public Path getTarget() {
        return target;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void addInfo(String info) {
        infos.add(info);
    }

    @Override
    public void apply(SimulationEnvironment environment) {
        start();
        long total = 0;
        for(ConsumerAgent agent: environment.getAgents().iterableConsumerAgents()) {
            total += handleAgent(agent);
        }
        try {
            finish(total);
        } catch (Exception e) {
            handleApplyError(e);
        }
    }

    protected abstract void handleApplyError(Exception e);

    public void start() {
        table = new XlsxTable<>();
        addColumns(table);
    }

    protected abstract void addColumns(XlsxTable<Attribute> table);

    public void finish(long total) throws IOException {
        handleTotalCount(total);
        XlsxSheetWriter<Attribute> writer = new XlsxSheetWriter<>();
        writer.setTextConverter(StandardCellValueConverter.ATTR2STR);
        writer.setNumericConverter(StandardCellValueConverter.ATTR2NUM);
        writer.write(
                target,
                sheetName,
                infos,
                table.getHeaderInstance(),
                table.listTable()
        );
    }

    protected abstract void handleTotalCount(long total);

    public abstract int handleAgent(ConsumerAgent agent);
}
