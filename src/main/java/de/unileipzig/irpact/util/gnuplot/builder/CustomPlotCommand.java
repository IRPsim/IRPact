package de.unileipzig.irpact.util.gnuplot.builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class CustomPlotCommand implements Command {

    protected final List<SubCommand> commands = new ArrayList<>();

    public CustomPlotCommand() {
    }

    public void add(SubCommand command) {
        commands.add(command);;
    }

    public void add(int index, SubCommand command) {
        commands.add(index, command);
    }

    public SubCommand replace(int index, SubCommand command) {
        return commands.set(index, command);
    }

    public SubCommand remove(int index) {
        return commands.remove(index);
    }

    public boolean remove(SubCommand command) {
        return commands.remove(command);
    }

    @Override
    public void print(StringSettings settings, Appendable target) throws IOException {

    }

    //=========================
    //SubCommands
    //=========================

    /**
     * @author Daniel Abitz
     */
    public interface SubCommand {

        String print(StringSettings settings);
    }


}
