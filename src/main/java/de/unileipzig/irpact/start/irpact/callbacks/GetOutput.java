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
public class GetOutput extends NameableBase implements IRPactCallback {

    protected AnnualData<OutRoot> out;

    public GetOutput(String name) {
        setName(name);
    }

    @Override
    public void onFinished(IRPActAccess access) {
        out = access.getOutput();
    }

    public void dispose() {
        out = null;
    }

    protected void checkState() {
        if(out == null) {
            throw new IllegalStateException("not called");
        }
    }

    public AnnualData<OutRoot> getOutput() {
        checkState();
        return out;
    }
}
