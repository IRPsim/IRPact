package de.unileipzig.irpact.util.scenarios;

import de.unileipzig.irpact.commons.exception.IRPactIllegalArgumentException;
import de.unileipzig.irpact.commons.util.IRPArgs;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.start.Start;
import de.unileipzig.irpact.start.Start3;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irptools.io.annual.AnnualData;
import de.unileipzig.irptools.io.perennial.PerennialData;
import de.unileipzig.irptools.io.perennial.PerennialFile;
import de.unileipzig.irptools.io.swagger.UploadableSwaggerData;
import de.unileipzig.irptools.io.swagger.UploadableSwaggerFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractScenario implements Scenario {

    public static final int DEFAULT_INITIAL_YEAR = 2015;

    protected String name;
    protected String creator;
    protected String description;

    protected Path logPath;
    protected Path outputDir;
    protected Path downloadDir;
    protected Path outputPath;

    public AbstractScenario(
            String name,
            String creator,
            String description) {
        this(name, creator, description, null, null, null);
    }

    public AbstractScenario(
            String name,
            String creator,
            String description,
            Path logPath,
            Path outputDir,
            Path downloadDir) {
        setName(name);
        setCreator(creator);
        setDescription(description);
        setLogPath(logPath);
        setOutputDir(outputDir);
        setDownloadDir(downloadDir);
    }

    protected void updateArgs(IRPArgs args) {
        if(logPath != null) args.setLogPathWithConsole(logPath);
        if(outputDir != null) args.setOutputDir(outputDir);
        if(downloadDir != null) args.setDownloadDir(downloadDir);
        if(outputPath != null) args.setOutput(outputPath);
    }

    public abstract List<InRoot> createInRoots();

    //=========================
    //for running
    //=========================

    protected void run(IRPArgs args, PerennialData<InRoot> data) throws Throwable {
        Start3.start(args.toArray(), data);
    }

    public void run() throws Throwable {
        IRPArgs args = new IRPArgs();
        updateArgs(args);
        List<InRoot> roots = createInRoots();
        validate(roots);
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
        List<InRoot> roots = createInRoots();
        if(roots.size() != 1) {
            throw new IllegalArgumentException("legacy does not supports multiple years");
        }
        InRoot root = roots.get(0);
        AnnualData<InRoot> data = new AnnualData<>(root);
        data.getConfig().setYear(root.getGeneral().getFirstSimulationYear());
        Start.start(args.toArray(), data.get());
    }

    //=========================
    //for swagger
    //=========================

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

    protected void validate(List<InRoot> inRoots) {
        for(InRoot inRoot: inRoots) {
            //exiting first simulation year
            if(inRoot != null && !inRoot.general.hasFirstSimulationYear()) {
                throw new IRPactIllegalArgumentException("missing initial year (name '{}')", getName());
            }
        }
    }

    @Override
    public void storeUploadableTo(Path target, boolean pretty) throws IOException {
        storeUploadableTo(target, StandardCharsets.UTF_8, pretty);
    }

    @Override
    public void storeUploadableTo(Path target, Charset charset, boolean pretty) throws IOException {
        List<InRoot> roots = createInRoots();
        validate(roots);
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
        List<InRoot> roots = createInRoots();
        validate(roots);
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
}
