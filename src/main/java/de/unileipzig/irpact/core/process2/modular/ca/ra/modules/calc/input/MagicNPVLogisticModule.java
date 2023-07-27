package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input;

import de.unileipzig.irpact.commons.util.MathUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.ra.npv.NPVData;
import de.unileipzig.irpact.core.process.ra.npv.NPVDataSupplier;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractCACalculationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class MagicNPVLogisticModule
        extends AbstractCACalculationModule2
        implements RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(MagicNPVLogisticModule.class);

    public static final int ANNUAL_AGENT = 1;
    public static final int ANNUAL_ASSET = 2;
    public static final int ANNUAL_EXISTING_ASSET = 3;
    public static final int GLOBAL_AGENT = 4;
    public static final int GLOBAL_ASSET = 5;
    public static final int GLOBAL_EXISTING_ASSET = 6;

    protected NPVDataSupplier dataSupplier;
    protected NPVData data;

    protected int npvId = 1;

    protected double L = 1.0;
    protected double k = 1.0;
    protected double x0 = 0;
    protected double move = 0;
    protected double scale = 1;
    protected double newRangeMin = 0;
    protected double newRangeMax = 1;

    public void setData(NPVData data) {
        this.data = data;
    }

    public void setNpvId(int npvId) {
        this.npvId = npvId;
    }

    public int getNpvId() {
        return npvId;
    }

    public void setX0(double x0) {
        this.x0 = x0;
    }

    public double getX0() {
        return x0;
    }

    public void setL(double l) {
        L = l;
    }

    public double getL() {
        return L;
    }

    public void setK(double k) {
        this.k = k;
    }

    public double getK() {
        return k;
    }

    public void setMove(double move) {
        this.move = move;
    }

    public double getMove() {
        return move;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getScale() {
        return scale;
    }

    public void setNewRangeMin(double newRangeMin) {
        this.newRangeMin = newRangeMin;
    }

    public double getNewRangeMin() {
        return newRangeMin;
    }

    public void setNewRangeMax(double newRangeMax) {
        this.newRangeMax = newRangeMax;
    }

    public double getNewRangeMax() {
        return newRangeMax;
    }

    private double getMin(ConsumerAgentData2 input, int start, int end) {
        switch (npvId) {
            case ANNUAL_AGENT:
                return dataSupplier.annualMinAgentNPV(input.streamConsumerAgents(), getCurrentYear(input));
            case ANNUAL_ASSET:
                return dataSupplier.annualMinAssetNPV(getCurrentYear(input));
            case ANNUAL_EXISTING_ASSET:
                return dataSupplier.annualMinExistingAssetNPV(input.streamConsumerAgents(), getCurrentYear(input));
            case GLOBAL_AGENT:
                return dataSupplier.globalMinAgentNPV(input::streamConsumerAgents, start, end);
            case GLOBAL_ASSET:
                return dataSupplier.globalMinAssetNPV(start, end);
            case GLOBAL_EXISTING_ASSET:
                return dataSupplier.globalMinExistingAssetNPV(input::streamConsumerAgents, start, end);
        }
        throw new IllegalArgumentException("illegal npv mode: " + npvId);
    }

    private double getMax(ConsumerAgentData2 input, int start, int end) {
        switch (npvId) {
            case ANNUAL_AGENT:
                return dataSupplier.annualMaxAgentNPV(input.streamConsumerAgents(), getCurrentYear(input));
            case ANNUAL_ASSET:
                return dataSupplier.annualMaxAssetNPV(getCurrentYear(input));
            case ANNUAL_EXISTING_ASSET:
                return dataSupplier.annualMaxExistingAssetNPV(input.streamConsumerAgents(), getCurrentYear(input));
            case GLOBAL_AGENT:
                return dataSupplier.globalMaxAgentNPV(input::streamConsumerAgents, start, end);
            case GLOBAL_ASSET:
                return dataSupplier.globalMaxAssetNPV(start, end);
            case GLOBAL_EXISTING_ASSET:
                return dataSupplier.globalMaxExistingAssetNPV(input::streamConsumerAgents, start, end);
        }
        throw new IllegalArgumentException("illegal npv mode: " + npvId);
    }

    private double getNPV(ConsumerAgentData2 input) {
        switch (npvId) {
            case ANNUAL_AGENT:
            case ANNUAL_ASSET:
            case ANNUAL_EXISTING_ASSET:
            case GLOBAL_AGENT:
            case GLOBAL_ASSET:
            case GLOBAL_EXISTING_ASSET:
                return dataSupplier.NPV(input.getAgent(), getCurrentYear(input));
        }
        throw new IllegalArgumentException("illegal npv mode: " + npvId);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void validate() throws Throwable {
        traceModuleValidation();
        if(data == null) {
            throw new NullPointerException("missing NPVData");
        }
    }

    @Override
    public void initialize(SimulationEnvironment environment) throws Throwable {
        if(alreadyInitalized()) {
            return;
        }

        traceModuleInitalization();
        dataSupplier = getNPVDataSupplier(environment, data);
        setInitalized();
    }

    @Override
    public void initializeNewInput(ConsumerAgentData2 input) throws Throwable {
    }

    @Override
    public void setup(SimulationEnvironment environment) throws Throwable {
        if(alreadySetupCalled()) {
            return;
        }

        traceModuleSetup();
        setSetupCalled();
    }

    @Override
    public double calculate(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleCall(input);
        SimulationEnvironment environment = input.getEnvironment();
        int start = environment.getSettings().getFirstSimulationYear();
        int end = environment.getSettings().getLastSimulationYear();

        // default NPV
        double minNpv = getMin(input, start, end);
        double maxNpv = getMax(input, start, end);
        double npv = getNPV(input);

        // normalize NPV between 0 and 1
        double normNpv = MathUtil.normalize(npv, minNpv, maxNpv);

        // rescale normalized NPV
        double moveScaledMin = getScale() * getMove();
        double moveScaledMax = getScale() * (getMove() + 1);
        double movedScaledNpv = getScale() * (getMove() + normNpv);

        // logistic
        double logisticMin = MathUtil.logistic(getL(), getK(), moveScaledMin, getX0());
        double logisticMax = MathUtil.logistic(getL(), getK(), moveScaledMax, getX0());
        double logisticNpv = MathUtil.logistic(getL(), getK(), movedScaledNpv, getX0());

        // rearrange
        double newRangeNpv = MathUtil.transform2(logisticNpv, logisticMin, logisticMax, getNewRangeMin(), getNewRangeMax());

        trace("[{}]@[{}] npvMode={} | " +
                "npv={}, min={}, max={}, normalized={} | " +
                "scale={}, move={}, result={}, min={}, max={} | " +
                "L={}, k={}, x0={}, x={}, r={}, min={}, max={} | " +
                "[{}, {}] -> [{}, {}], {} -> {}",
            getName(), printInputInfo(input), getNpvId(),
            npv, minNpv, maxNpv, normNpv,
            getScale(), getMove(), movedScaledNpv, moveScaledMin, moveScaledMax,
            getL(), getK(), getX0(), movedScaledNpv, logisticNpv, logisticMin, logisticMax,
            logisticMin, logisticMax, getNewRangeMin(), getNewRangeMax(), logisticNpv, newRangeNpv);

        return newRangeNpv;
    }
}
