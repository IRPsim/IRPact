package de.unileipzig.irpact.start.irpact.callbacks;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irpact.start.irpact.IRPActAccess;
import de.unileipzig.irpact.start.irpact.IRPactCallback;
import de.unileipzig.irptools.io.annual.AnnualData;
import de.unileipzig.irptools.io.base.data.AnnualEntry;

/**
 * @author Daniel Abitz
 */
public class GetResult extends NameableBase implements IRPactCallback {

    protected AnnualEntry<InRoot> in;
    protected AnnualData<OutRoot> out;
    protected MainCommandLineOptions cmd;

    public GetResult(String name) {
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

    public MainCommandLineOptions getCommandLineOptions() {
        checkState();
        return cmd;
    }

    public AnnualEntry<InRoot> getInEntry() {
        checkState();
        return in;
    }

    public InRoot getInRoot() {
        return getInEntry().getData();
    }

    public AnnualData<OutRoot> getOutData() {
        checkState();
        return out;
    }

    public OutRoot getOutRoot() {
        return getOutData().getData();
    }
}
