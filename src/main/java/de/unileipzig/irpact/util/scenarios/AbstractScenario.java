package de.unileipzig.irpact.util.scenarios;

import de.unileipzig.irpact.commons.exception.IRPactIllegalArgumentException;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irpact.io.param.input.visualisation.result.InGenericOutputImage;
import de.unileipzig.irpact.util.IRPArgs;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.core.logging.IRPLevel;
import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.param.input.InInformation;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.InScenarioVersion;
import de.unileipzig.irpact.io.param.input.visualisation.network.InConsumerAgentGroupColor;
import de.unileipzig.irpact.io.param.input.visualisation.network.InGraphvizGeneral;
import de.unileipzig.irpact.start.Start;
import de.unileipzig.irpact.start.Start3;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irptools.graphviz.StandardLayoutAlgorithm;
import de.unileipzig.irptools.io.annual.AnnualData;
import de.unileipzig.irptools.io.perennial.PerennialData;
import de.unileipzig.irptools.io.perennial.PerennialFile;
import de.unileipzig.irptools.io.swagger.DownloadedSwaggerData;
import de.unileipzig.irptools.io.swagger.DownloadedSwaggerFile;
import de.unileipzig.irptools.io.swagger.UploadableSwaggerData;
import de.unileipzig.irptools.io.swagger.UploadableSwaggerFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractScenario implements Scenario {

    public static final Comparator<? super AbstractScenario> SORT_ID = Comparator.comparingInt(o -> o.sortId);

    public static final int DEFAULT_INITIAL_YEAR = 2010;

    protected static final Map<String, Object> NAMED_DATA = new HashMap<>();

    protected int sortId = 0;

    protected String name;
    protected String creator;
    protected String description;
    protected String businessModelDescription;
    protected String investmentCustomerSide;
    protected String parameterAttention;

    protected int revision = 0;

    protected boolean forceLogConsole = true;
    protected boolean logConsole = true;
    protected Path logPath;
    protected Path outputDir;
    protected Path downloadDir;
    protected Path outputPath;
    protected Path dataDir;
    protected Path imagePath;
    protected Path gnuplotCommand;
    protected Path rCommand;
    protected boolean noSimulation;

    protected long seed = 42;

    protected Integer simulationStartYear;
    protected int simulationDelta = 1;
    protected Consumer<? super InGeneral> generalSetup;
    protected Consumer<? super List<InRoot>> postsetupTask;

    public AbstractScenario() {
        this(null, null, null);
    }

    public AbstractScenario(
            String name,
            String creator,
            String description) {
        setName(name);
        setCreator(creator);
        setDescription(description);
    }

    protected void updateArgs(IRPArgs args) {
        if(logPath != null) {
            if(logConsole) args.setLogPathWithConsole(logPath);
            else args.setLogPath(logPath);
        }
        if(outputDir != null) args.setOutputDir(outputDir);
        if(downloadDir != null) args.setDownloadDir(downloadDir);
        if(outputPath != null) args.setOutput(outputPath);
        if(dataDir != null) args.setDataDir(dataDir);
        if(imagePath != null) args.setImage(imagePath);
        if(noSimulation) args.setNoSimulation();
        if(gnuplotCommand != null) args.setGnuPlotCommand(gnuplotCommand);
        if(rCommand != null) args.setRscriptCommand(rCommand);
    }

    @Deprecated
    public List<InRoot> createInRootsOLD() {
        return null;
    }

    public final List<InRoot> createInRoots() {
        List<InRoot> roots = simulationStartYear == null
                ? createInRootsOLD() //legacy
                : createInRoots(simulationStartYear, simulationStartYear + simulationDelta - 1);
        if(roots == null) {
            throw new IllegalStateException("missing data");
        }
        return roots;
    }

    protected final List<InRoot> createInRoots(int firstYear, int lastYear) {
        if(lastYear < firstYear) {
            lastYear = firstYear;
        }
        List<InRoot> roots = new ArrayList<>();
        for(int year = firstYear; year <= lastYear; year++) {
            roots.add(createInRoot(year));
        }
        long nonNull = roots.stream()
                .filter(Objects::nonNull)
                .count();
        return nonNull == 0L
                ? null
                : roots;
    }

    protected InRoot createInRoot(int year) {
        return null;
    }

    protected List<InRoot> createSetupAndValidateRoots() {
        List<InRoot> roots = createInRoots();
        postsetupRoot(roots);
        validate(roots);
        return roots;
    }

    //=========================
    //named cache
    //=========================

    protected InAttributeName getAttributeName(String name) {
        return computeCachedIfAbsent(name, InAttributeName::new);
    }

    protected boolean isCached(String name) {
        return NAMED_DATA.containsKey(name);
    }

    protected void cache(String name, Object data) {
        NAMED_DATA.put(name, data);
    }

    @SuppressWarnings("unchecked")
    protected <R> R computeCachedIfAbsent(String name, Function<? super String, ? extends R> creator) {
        return (R) NAMED_DATA.computeIfAbsent(name, creator);
    }

    @SuppressWarnings("unchecked")
    protected <R> R getCached(String name) {
        return (R) NAMED_DATA.get(name);
    }

    //=========================
    //for running
    //=========================

    protected void run(IRPArgs args, PerennialData<InRoot> data) throws Throwable {
        Start3.start(args.toArray(), data);
    }

    public void run() throws Throwable {
        IRPArgs args = new IRPArgs();
        updateArgs(args);
        List<InRoot> roots = createSetupAndValidateRoots();
        PerennialData<InRoot> data = new PerennialData<>();
        for(InRoot root: roots) {
            if(root == null) {
                data.addNull();
            } else {
                data.add(root.general.getFirstSimulationYear(), root);
            }
        }
        run(args, data);
    }

    public void runLegacy() throws Throwable {
        IRPArgs args = new IRPArgs();
        updateArgs(args);
        List<InRoot> roots = createSetupAndValidateRoots();
        if(roots.size() != 1) {
            throw new IllegalArgumentException("legacy does not supports multiple years");
        }
        InRoot root = roots.get(0);
        AnnualData<InRoot> data = new AnnualData<>(root);
        data.getConfig().setYear(root.getGeneral().getFirstSimulationYear());
        Start.start(args.toArray(), data.get());
    }

    public String printArgs() {
        IRPArgs args = new IRPArgs();
        updateArgs(args);
        return args.print();
    }

    //=========================
    //for swagger
    //=========================

    public void setSimulationDelta(int simulationDelta) {
        this.simulationDelta = simulationDelta;
    }

    public int getSimulationDelta() {
        return simulationDelta;
    }

    public void setSimulationStartYear(Integer simulationStartYear) {
        this.simulationStartYear = simulationStartYear;
    }

    public Integer getSimulationStartYear() {
        return simulationStartYear;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreator() {
        return creator;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setBusinessModelDescription(String businessModelDescription) {
        this.businessModelDescription = businessModelDescription;
    }

    public String getBusinessModelDescription() {
        return businessModelDescription;
    }

    public void setInvestmentCustomerSide(String investmentCustomerSide) {
        this.investmentCustomerSide = investmentCustomerSide;
    }

    public String getInvestmentCustomerSide() {
        return investmentCustomerSide;
    }

    public void setParameterAttention(String parameterAttention) {
        this.parameterAttention = parameterAttention;
    }

    public String getParameterAttention() {
        return parameterAttention;
    }

    public void setLogConsole(boolean logConsole) {
        this.logConsole = logConsole;
    }

    public boolean isLogConsole() {
        return logConsole;
    }

    public void setLogPath(Path logPath) {
        this.logPath = logPath;
    }

    public Path getLogPath() {
        return logPath;
    }

    public void setOutputDir(Path outputDir) {
        this.outputDir = outputDir;
    }

    public Path getOutputDir() {
        return outputDir;
    }

    public void setDownloadDir(Path downloadDir) {
        this.downloadDir = downloadDir;
    }

    public Path getDownloadDir() {
        return downloadDir;
    }

    public void setOutputPath(Path outputPath) {
        this.outputPath = outputPath;
    }

    public Path getOutputPath() {
        return outputPath;
    }

    public void setDataDir(Path dataDir) {
        this.dataDir = dataDir;
    }

    public Path getDataDir() {
        return dataDir;
    }

    public void setImagePath(Path imagePath) {
        this.imagePath = imagePath;
    }

    public Path getImagePath() {
        return imagePath;
    }

    public void setNoSimulation(boolean noSimulation) {
        this.noSimulation = noSimulation;
    }

    public boolean isNoSimulation() {
        return noSimulation;
    }

    public void setGnuplotCommand(Path gnuplotCommand) {
        this.gnuplotCommand = gnuplotCommand;
    }

    public Path getGnuplotCommand() {
        return gnuplotCommand;
    }

    public void setRCommand(Path rCommand) {
        this.rCommand = rCommand;
    }

    public Path getRCommand() {
        return rCommand;
    }

    public void setForceLogConsole(boolean forceLogConsole) {
        this.forceLogConsole = forceLogConsole;
    }

    public boolean isForceLogConsole() {
        return forceLogConsole;
    }

    protected void validate(List<InRoot> inRoots) {
        for(InRoot inRoot: inRoots) {
            //exiting first simulation year
            if(inRoot != null && !inRoot.general.hasFirstSimulationYear()) {
                throw new IRPactIllegalArgumentException("missing initial year (name '{}')", getName());
            }
        }
    }

    private void postsetupRoot(List<InRoot> inRoots) {
        if(postsetupTask != null) {
            postsetupTask.accept(inRoots);
        }
    }

    public void setPostsetupTask(Consumer<? super List<InRoot>> postsetupTask) {
        this.postsetupTask = postsetupTask;
    }

    public Consumer<? super List<InRoot>> getPostsetupTask() {
        return postsetupTask;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public AbstractScenario withRevision(int revision) {
        setRevision(revision);
        return this;
    }

    public void setGeneralSetup(Consumer<? super InGeneral> generalSetup) {
        this.generalSetup = generalSetup;
    }

    public void logAll() {
        setGeneralSetup(general -> {
            general.setLogLevel(IRPLevel.ALL);
            general.logAll = true;
        });
    }

    public Consumer<? super InGeneral> getGeneralSetup() {
        return generalSetup;
    }

    public InRoot createRootWithInformationsWithFullLogging() {
        return createRootWithInformations(true);
    }

    public InRoot createRootWithInformations() {
        return createRootWithInformations(false);
    }

    protected InRoot createRootWithInformations(boolean fullLog) {
        InRoot root = new InRoot();
        root.addInformation(getRevisionInformation());
        root.setVersion(InScenarioVersion.currentVersion());

        root.setGeneral(new InGeneral());
        root.getGeneral().setSeed(seed);
        root.getGeneral().setTimeout(5, TimeUnit.MINUTES);
        root.getGeneral().setFirstSimulationYear(DEFAULT_INITIAL_YEAR);
        root.getGeneral().setLastSimulationYear(root.getGeneral().getFirstSimulationYear() + simulationDelta - 1);

        if(fullLog) {
            root.getGeneral().setLogLevel(IRPLevel.ALL);
            root.getGeneral().logAll = true;
        } else {
            root.getGeneral().setLogLevel(IRPLevel.INFO);
            root.getGeneral().logAllIRPact = true;
        }

        setupGeneral(root.getGeneral());

        root.setGraphvizGeneral(new InGraphvizGeneral());
        setupGraphvizGeneral(root.getGraphvizGeneral());
        root.setConsumerAgentGroupColors(InConsumerAgentGroupColor.ALL);

        setupImages(root);

        return root;
    }

    protected void setupImages(InRoot root) {
        root.setImages(InGenericOutputImage.createDefaultImages());
    }

    public void setupGeneral(InGeneral general) {
        general.setForceLogToConsole(isForceLogConsole());
        if(generalSetup != null) {
            generalSetup.accept(general);
        }
    }

    public void setupGraphvizGeneral(InGraphvizGeneral general) {
        general.setStoreDotFile(false);
        general.setLayoutAlgorithm(StandardLayoutAlgorithm.NEATO);
    }

    public InInformation getRevisionInformation() {
        return new InInformation("revision_" + getRevision());
    }

    public AbstractScenario peek(Consumer<? super AbstractScenario> consumer) {
        consumer.accept(this);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <R extends AbstractScenario> R autoCast() {
        return (R) this;
    }

    @Override
    public void storeUploadableTo(Path target, boolean pretty) throws IOException {
        storeUploadableTo(target, StandardCharsets.UTF_8, pretty);
    }

    @Override
    public void storeUploadableTo(Path target, Charset charset, boolean pretty) throws IOException {
        List<InRoot> roots = createSetupAndValidateRoots();
        UploadableSwaggerData<InRoot> data = new UploadableSwaggerData<>();
        data.setName(getName());
        data.setCreator(getCreator());
        data.setDescription(getDescription());
        for(InRoot root: roots) {
            if(root == null) {
                data.addNull(IRPact.MODELDEFINITION);
            } else {
                data.add(IRPact.MODELDEFINITION, root.general.getFirstSimulationYear(), root);
            }
        }
        UploadableSwaggerFile file = data.serialize(IRPact.getInputConverter());
        JsonUtil.writeJson(file.root(), target, charset, pretty ? JsonUtil.DEFAULT : JsonUtil.MINIMAL);
    }

    @Override
    public void storeRunnableTo(Path target, boolean pretty) throws IOException {
        storeRunnableTo(target, StandardCharsets.UTF_8, pretty);
    }

    @Override
    public void storeRunnableTo(Path target, Charset charset, boolean pretty) throws IOException {
        List<InRoot> roots = createSetupAndValidateRoots();
        PerennialData<InRoot> data = new PerennialData<>();
        for(InRoot root: roots) {
            if(root == null) {
                data.addNull();
            } else {
                data.add(root.general.getFirstSimulationYear(), root);
            }
        }
        PerennialFile file = data.serialize(IRPact.getInputConverter());
        JsonUtil.writeJson(file.root(), target, charset, pretty ? JsonUtil.DEFAULT : JsonUtil.MINIMAL);
    }

    @Override
    public void storePostableTo(Path target, boolean pretty) throws IOException {
        storePostableTo(target, StandardCharsets.UTF_8, pretty);
    }

    @Override
    public void storePostableTo(Path target, Charset charset, boolean pretty) throws IOException {
        List<InRoot> roots = createSetupAndValidateRoots();
        DownloadedSwaggerData<InRoot> data = new DownloadedSwaggerData<>();
        data.getDescription().reset();
        data.getDescription().setBusinessModelDescription(getBusinessModelDescription());
        data.getDescription().setInvestmentCustomerSide(getInvestmentCustomerSide());
        data.getDescription().setParameterAttention(getParameterAttention());
        data.getDescription().setCreator(getCreator());
        for(InRoot root: roots) {
            if(root == null) {
                data.addNull(IRPact.MODELDEFINITION);
            } else {
                data.add(IRPact.MODELDEFINITION, root.general.getFirstSimulationYear(), root);
            }
        }
        DownloadedSwaggerFile file = data.serialize(IRPact.getInputConverter());
        file.getDescription().copyFrom(data.getDescription());
        JsonUtil.writeJson(file.root(), target, charset, pretty ? JsonUtil.DEFAULT : JsonUtil.MINIMAL);
    }
}
