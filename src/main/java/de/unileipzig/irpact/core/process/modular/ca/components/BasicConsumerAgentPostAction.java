package de.unileipzig.irpact.core.process.modular.ca.components;

import de.unileipzig.irpact.core.process.modular.ca.AdoptionResult;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentPostAction implements ConsumerAgentPostAction {

    protected ConsumerAgentData data;
    protected Action action;

    public BasicConsumerAgentPostAction(ConsumerAgentData data, Action action) {
        this.data = data;
        this.action = action;
    }

    @Override
    public boolean isSupported(Class<?> type) {
        return type.isInstance(data);
    }

    @Override
    public ConsumerAgentData getInput() {
        return data;
    }

    @Override
    public String getInputName() {
        return data.getAgent().getName();
    }

    @Override
    public void execute() throws Throwable {
        data.getModel().execute(this);
    }

    @Override
    public AdoptionResult evaluate() throws Throwable {
        return action.apply(data);
    }

    /**
     * @author Daniel Abitz
     */
    public interface Action {

        AdoptionResult apply(ConsumerAgentData data) throws Throwable;
    }
}
