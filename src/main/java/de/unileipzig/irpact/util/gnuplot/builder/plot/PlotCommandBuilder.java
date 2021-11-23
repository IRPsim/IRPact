package de.unileipzig.irpact.util.gnuplot.builder.plot;

import de.unileipzig.irpact.util.gnuplot.builder.Command;
import de.unileipzig.irpact.util.gnuplot.builder.StringSettings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class PlotCommandBuilder implements Command {

    protected final List<SubCommand> commands = new ArrayList<>();

    public PlotCommandBuilder() {
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
        target.append("plot ");
        for(SubCommand command: commands) {
            command.print(settings, target);
        }
    }

    //=========================
    //util
    //=========================

    public static String quote(String input) {
        return "\"" + input + "\"";
    }

    public static String arg(int index) {
        return "ARG" + index;
    }

    public static String xtic(int index) {
        return "xtic(" + index + ")";
    }

    //=========================
    //general
    //=========================

    public void linebreak(String pre, String post) {
        PlotLineBreak lb = new PlotLineBreak();
        lb.setPre(pre);
        lb.setPost(post);
        add(lb);
    }

    public void linebreak() {
        linebreak(null, "   ");
    }

    public void commaAndLinebreak() {
        linebreak(",", "   ");
    }


    //=========================
    //keyentry
    //=========================

    public void keyentry(Object ti) {
        add(new SimplifiedKeyEntry().setTi(ti));
    }

    //=========================
    //NaN
    //=========================

    public void nanLines(Object dt, Object ls, Object ti) {
        SimplifiedNaN nanLines = new SimplifiedNaN();

        add(nanLines);
    }
}
