package de.unileipzig.irpact.util.scenarios;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.FileUtil;
import de.unileipzig.irpact.commons.util.IRPArgs;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irpact.util.curl.Curl;
import de.unileipzig.irpact.util.curl.CurlException;
import de.unileipzig.irpact.util.curl.CurlUtil;
import de.unileipzig.irpact.util.scenarios.toymodels.ToyModelUtil;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irpact.start.irpact.IRPactScenario;
import de.unileipzig.irpact.xxx.curl.IdResult;
import de.unileipzig.irptools.io.base.data.AnnualEntry;
import de.unileipzig.irptools.io.data.DataData;
import de.unileipzig.irptools.io.data.DataFile;
import de.unileipzig.irptools.io.perennial.PerennialData;
import de.unileipzig.irptools.io.perennial.PerennialFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractScenario implements IRPactScenario {

    public AbstractScenario() {
    }

    public abstract String getName();

    public abstract List<InRoot> createInRoots();

    protected void validateInitialYear(List<InRoot> inRoots) {
        for(InRoot inRoot: inRoots) {
            if(inRoot != null && !inRoot.general.hasFirstSimulationYear()) {
                throw new IllegalArgumentException("missing initial year");
            }
        }
    }

    protected void updateArgs(IRPArgs args) {
    }

    public int storeOnline(
            String targetUrl,
            String name,
            String creator,
            String description,
            String user,
            String password,
            Path tempDir,
            MainCommandLineOptions clOptions) throws IOException, CurlException, InterruptedException {
        Dev.throwException();

        List<InRoot> roots = createInRoots();
        InRoot root = roots.get(0);

        DataData<InRoot> data = new DataData<>();
        data.setName(name);
        data.setCreator(creator);
        data.setDescription(description);
        data.add(root.general.firstSimulationYear, root);

        DataFile file = data.serialize(IRPact.getInputConverter(clOptions));

        Path dataTemp = FileUtil.createTempFile(tempDir, "", "");
        Curl curl = new Curl();
        try {
            JsonUtil.writeJson(file.root(), dataTemp, JsonUtil.MINIMAL);

            curl.silent()
                    .showError()
                    .target(targetUrl)
                    .PUT()
                    .acceptJson()
                    .contentTypeJson()
                    .fileContent(dataTemp)
                    .user(user, password);

            Path outTemp = FileUtil.createTempFile(tempDir, "", "");
            Path errTemp = FileUtil.createTempFile(tempDir, "", "");

            Optional<JsonNode> result = CurlUtil.executeToJson(
                    curl,
                    outTemp,
                    errTemp,
                    StandardCharsets.UTF_8,
                    JsonUtil.JSON,
                    true
            );

            if(result.isPresent()) {
                ObjectNode resultRoot = (ObjectNode) result.get();
                IdResult idResult = new IdResult(resultRoot);
                return idResult.getId();
            } else {
                throw new NoSuchElementException();
            }
        } finally {
            curl.reset();
            FileUtil.deleteIfExists(dataTemp);
        }
    }

    public void run() throws Throwable {
        IRPArgs args = new IRPArgs();
        updateArgs(args);
        List<InRoot> roots = createInRoots();
        AnnualEntry<InRoot> entry = ToyModelUtil.buildEntry(roots.get(0));
        run(args, entry);
    }

    protected abstract void run(IRPArgs args, AnnualEntry<InRoot> entry) throws Throwable;

    public void store(Path target) throws IOException {
        store(target, StandardCharsets.UTF_8);
    }

    public void store(Path target, Charset charset) throws IOException {
        List<InRoot> roots = createInRoots();
        validateInitialYear(roots);
        PerennialData<InRoot> data = new PerennialData<>();
        for(InRoot root: roots) {
            if(root == null) {
                //data.addNull();
                Dev.throwException();
            } else {
                data.add(root.general.getFirstSimulationYear(), root);
            }
        }
        PerennialFile file = data.serialize(IRPact.getInputConverter(null));
        file.store(target, charset);
    }
}
