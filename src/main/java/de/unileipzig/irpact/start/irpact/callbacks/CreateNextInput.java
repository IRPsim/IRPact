package de.unileipzig.irpact.start.irpact.callbacks;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.util.data.Pair;
import de.unileipzig.irpact.develop.Todo;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irpact.start.irpact.IRPActAccess;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irpact.start.irpact.IRPactCallback;
import de.unileipzig.irptools.io.annual.AnnualData;
import de.unileipzig.irptools.io.annual.AnnualFile;
import de.unileipzig.irptools.io.base.AnnualEntry;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public class CreateNextInput extends NameableBase implements IRPactCallback {

    protected AnnualEntry<InRoot> in;
    protected AnnualData<OutRoot> out;
    protected MainCommandLineOptions cmd;

    public CreateNextInput(String name) {
        setName(name);
    }

    @Override
    public void onFinished(IRPActAccess access) {
        in = access.getInput();
        out = access.getOutput();
        cmd = access.getCommandLineOptions();
    }

    public void dispose() {
        in = null;
        out = null;
        cmd = null;
    }

    protected void checkState() {
        if(in == null) {
            throw new IllegalStateException("not called");
        }
    }

    public AnnualData<InRoot> createForNextYear() {
        checkState();
        int currentYear = in.getConfig().getYear();
        return createFor(currentYear + 1);
    }

    @Todo("copy richtig einbauen")
    public AnnualData<InRoot> createFor(int year) {
        checkState();
        InRoot copy = in.getData(); //hier
        copy.binaryPersistData = out.getData().getBinaryPersistData(); //hier

        AnnualData<InRoot> nextRoot = new AnnualData<>(copy);
        nextRoot.getConfig().copyFrom(in.getConfig());
        nextRoot.getConfig().setYear(year);
        return nextRoot;
    }

    public Pair<AnnualData<InRoot>, AnnualFile> createAndStore(int year, Path target) throws IOException {
        checkState();
        AnnualData<InRoot> data = createFor(year);
        AnnualFile file = toFile(data);
        store(target, file);
        return new Pair<>(data, file);
    }

    public AnnualFile toFile(AnnualData<InRoot> data) {
        checkState();
        return data.serialize(IRPact.getInputConverter(cmd));
    }

    public void store(Path target, AnnualFile file) throws IOException {
        file.store(target);
    }
}
