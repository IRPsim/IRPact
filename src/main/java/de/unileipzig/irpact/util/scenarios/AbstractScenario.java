package de.unileipzig.irpact.util.scenarios;

import de.unileipzig.irpact.commons.util.IRPArgs;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irptools.io.perennial.PerennialData;
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

    public AbstractScenario() {
    }

    protected void updateArgs(IRPArgs args) {
    }

    public abstract List<InRoot> createInRoots();

    //=========================
    //for running
    //=========================

    protected abstract void run(IRPArgs args, PerennialData<InRoot> data) throws Throwable;

    public void run() throws Throwable {
        IRPArgs args = new IRPArgs();
        updateArgs(args);
        List<InRoot> roots = createInRoots();
        validateInitialYear(roots);
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

    //=========================
    //for swagger
    //=========================

    protected abstract String getName();

    protected abstract String getCreator();

    protected abstract String getDescription();

    protected void validateInitialYear(List<InRoot> inRoots) {
        for(InRoot inRoot: inRoots) {
            if(inRoot != null && !inRoot.general.hasFirstSimulationYear()) {
                throw new IllegalArgumentException("missing initial year");
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
        validateInitialYear(roots);
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
        UploadableSwaggerFile file = data.serialize(IRPact.getInputConverter(null));
        JsonUtil.writeJson(file.root(), target, charset, pretty ? JsonUtil.DEFAULT : JsonUtil.MINIMAL);
    }
}
