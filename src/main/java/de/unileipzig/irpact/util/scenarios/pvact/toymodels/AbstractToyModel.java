package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.util.IRPArgs;
import de.unileipzig.irpact.commons.util.table.Table;
import de.unileipzig.irpact.core.spatial.SpatialTableFileLoader;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.start.Start3;
import de.unileipzig.irpact.start.irpact.callbacks.GetInputAndOutput;
import de.unileipzig.irpact.util.scenarios.pvact.AbstractPVactScenario;
import de.unileipzig.irptools.io.perennial.PerennialData;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiConsumer;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractToyModel extends AbstractPVactScenario {

    public static final BiConsumer<InRoot, OutRoot> IGNORE = (in, out) -> {};

    protected BiConsumer<InRoot, OutRoot> resultConsumer;

    public AbstractToyModel(BiConsumer<InRoot, OutRoot> resultConsumer) {
        super();
        this.resultConsumer = Objects.requireNonNull(resultConsumer);
    }

    public AbstractToyModel(
            String name,
            String creator,
            String description,
            BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description);
        this.resultConsumer = Objects.requireNonNull(resultConsumer);
    }

    public void buildData(Path xlsxInput, Path xlsxOutput, Random random) throws ParsingException, IOException, InvalidFormatException {
        Table<SpatialAttribute> inputData = SpatialTableFileLoader.parseXlsx(xlsxInput);
        buildData(inputData, xlsxOutput, random);
    }

    public void buildData(Table<SpatialAttribute> inputData, Path xlsxOutput, Random random) throws IOException {
        buildData(
                inputData,
                xlsxOutput,
                "Daten",
                ParamUtil.getClassNameWithoutClassSuffix(getClass()),
                random
        );
    }

    public void buildData(Path xlsxInput, Path xlsxOutput, String sheetName, String info, Random random) throws ParsingException, IOException, InvalidFormatException {
        Table<SpatialAttribute> inputData = SpatialTableFileLoader.parseXlsx(xlsxInput);
        buildData(inputData, xlsxOutput, sheetName, info, random);
    }

    public void buildData(Table<SpatialAttribute> inputData, Path xlsxOutput, String sheetName, String info, Random random) throws IOException {
        List<List<SpatialAttribute>> outputList = createTestData(inputData.listTable(), random);
        Table<SpatialAttribute> outputData = inputData.emptyCopyWithSameHeader();
        outputData.addRows(outputList);
        SpatialTableFileLoader.writeXlsx(xlsxOutput, sheetName, info, outputData);
    }

    protected abstract List<List<SpatialAttribute>> createTestData(List<List<SpatialAttribute>> input, Random random);

    @Override
    protected void run(IRPArgs args, PerennialData<InRoot> data) throws Throwable {
        GetInputAndOutput callback = new GetInputAndOutput("result_" + getName());
        Start3.start(args.toArray(), data, callback);
        resultConsumer.accept(callback.getInRoot(), callback.getOutRoot());
    }
}
