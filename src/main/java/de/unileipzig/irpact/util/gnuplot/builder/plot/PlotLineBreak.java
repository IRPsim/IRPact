package de.unileipzig.irpact.util.gnuplot.builder.plot;

import de.unileipzig.irpact.util.gnuplot.builder.StringSettings;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class PlotLineBreak implements SubCommand {

    protected Object pre;
    protected Object post;

    public PlotLineBreak() {
    }

    public PlotLineBreak(Object pre, Object post) {
        setPre(pre);
        setPost(post);
    }

    public void setPre(Object pre) {
        this.pre = pre;
    }

    public void setPost(Object post) {
        this.post = post;
    }

    @Override
    public void print(StringSettings settings, Appendable target) throws IOException {
        if(pre != null) {
            target.append(pre.toString());
        }
        target.append("\\\n");
        if(post != null) {
            target.append(post.toString());
        }
    }
}
