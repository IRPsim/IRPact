package de.unileipzig.irpact.util.scenarios.toymodels;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.table.Table;
import de.unileipzig.irpact.commons.util.xlsx.XlsxSheetWriter;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.SpatialTableFileLoader;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.distribution.InDiracUnivariateDistribution;
import de.unileipzig.irpact.io.param.output.OutRoot;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

/**
 * @author Daniel Abitz
 */
public class ToyModel1 extends AbstractToyModel {

    protected InDiracUnivariateDistribution dirac0 = new InDiracUnivariateDistribution("dirac0", 0);
    protected InDiracUnivariateDistribution dirac07 = new InDiracUnivariateDistribution("dirac07", 0.7);
    protected InDiracUnivariateDistribution dirac1 = new InDiracUnivariateDistribution("dirac1", 1);

    public ToyModel1(BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(resultConsumer);
    }

    @Override
    public String getName() {
        return "Toymodel_1";
    }

    public List<List<SpatialAttribute>> buildData(
            List<List<SpatialAttribute>> input,
            int sizeOfA, int sizeOfK,
            Random rnd) {
        List<List<SpatialAttribute>> output = SpatialUtil.drawRandom(input, sizeOfA + sizeOfK, rnd);
        //A
        for(int i = 0; i < sizeOfA; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceDouble(row, RAConstants.PURCHASE_POWER, dirac1.getValue());  //A1
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac1.getValue());     //A6
        }
        //K
        for(int i = sizeOfK; i < sizeOfA + sizeOfK; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceDouble(row, RAConstants.PURCHASE_POWER, dirac0.getValue());  //A1
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac1.getValue());     //A6
        }

        return output;
    }

    public void buildData(
            Path xlsxInput, Path xlsxOutput,
            int sizeOfA, int sizeOfK,
            Random rnd) throws ParsingException, IOException, InvalidFormatException {
        Table<SpatialAttribute> inputData = SpatialTableFileLoader.parseXlsx(xlsxInput);
        List<List<SpatialAttribute>> outputList = buildData(inputData.listTable(), sizeOfA, sizeOfK, rnd);
        Table<SpatialAttribute> outputData = inputData.emptyCopyWithSameHeader();
        outputData.addRows(outputList);
        SpatialTableFileLoader.writeXlsx(xlsxOutput, "Data", "Daten fuer " + getName(), outputData);
    }

    protected InPVactConsumerAgentGroup createAgentGroup(String name) {
        InPVactConsumerAgentGroup grp = new InPVactConsumerAgentGroup();
        grp.setName(name);

        //A1 in file
        grp.setNoveltySeeking(dirac1);                            //A2
        grp.setDependentJudgmentMaking(dirac1);                   //A3
        grp.setEnvironmentalConcern(dirac1);                      //A4
        //A5 in file
        //A6 in file
        grp.setConstructionRate(dirac0);                          //A7
        grp.setRenovationRate(dirac0);                            //A8

        grp.setRewire(dirac0);                                    //B6

        grp.setCommunication(dirac0);                             //C1
        grp.setRateOfConvergence(dirac0);                         //C3

        grp.setInitialProductAwareness(null);                     //D1
        grp.setInterestThreshold(dirac1);                         //D2
        grp.setFinancialThreshold(dirac07);                       //D3
        grp.setAdoptionThreshold(dirac07);                        //D4
        grp.setInitialAdopter(dirac0);                            //D5
        grp.setInitialProductInterest(dirac1);                    //D6

        return grp;
    }

    @Override
    public InRoot createInRoot() {
        InPVactConsumerAgentGroup A = createAgentGroup("A");
        A.setInitialProductAwareness(dirac1);                     //D1

        InPVactConsumerAgentGroup K = createAgentGroup("K");
        K.setInitialProductAwareness(dirac0);                     //D1

        return null;
    }
}
