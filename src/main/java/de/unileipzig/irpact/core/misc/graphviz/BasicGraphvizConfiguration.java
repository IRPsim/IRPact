package de.unileipzig.irpact.core.misc.graphviz;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irptools.graphviz.DotProcess;
import de.unileipzig.irptools.graphviz.GraphvizGenerator;
import de.unileipzig.irptools.graphviz.LayoutAlgorithm;
import de.unileipzig.irptools.graphviz.OutputFormat;
import de.unileipzig.irptools.util.ProcessResult;
import de.unileipzig.irptools.util.log.IRPLogger;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Shape;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicGraphvizConfiguration implements GraphvizConfiguration {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicGraphvizConfiguration.class);

    protected Map<ConsumerAgentGroup, Color> colorMapping;
    protected LayoutAlgorithm layoutAlgorithm;
    protected OutputFormat outputFormat;
    protected boolean fixedNeatoPosition;
    protected double scaleFactor;
    protected Charset terminalCharset = StandardCharsets.UTF_8;
    protected boolean setDefaultSize = true;

    public BasicGraphvizConfiguration() {
        this(new HashMap<>());
    }

    public BasicGraphvizConfiguration(Map<ConsumerAgentGroup, Color> colorMapping) {
        this.colorMapping = colorMapping;
    }

    public void setLayoutAlgorithm(LayoutAlgorithm layoutAlgorithm) {
        this.layoutAlgorithm = layoutAlgorithm;
    }

    public LayoutAlgorithm getLayoutAlgorithm() {
        return layoutAlgorithm;
    }

    public void setOutputFormat(OutputFormat outputFormat) {
        this.outputFormat = outputFormat;
    }

    public OutputFormat getOutputFormat() {
        return outputFormat;
    }

    public boolean hasColor(ConsumerAgentGroup cag) {
        return colorMapping.containsKey(cag);
    }

    public void putColor(ConsumerAgentGroup cag, Color color) {
        colorMapping.put(cag, color);
    }

    public Color getColor(ConsumerAgentGroup cag) {
        return colorMapping.get(cag);
    }

    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public double getScaleFactor() {
        return scaleFactor;
    }

    public void setFixedNeatoPosition(boolean fixedNeatoPosition) {
        this.fixedNeatoPosition = fixedNeatoPosition;
    }

    public boolean isFixedNeatoPosition() {
        return fixedNeatoPosition;
    }

    public void setTerminalCharset(Charset terminalCharset) {
        this.terminalCharset = terminalCharset;
    }

    public Charset getTerminalCharset() {
        return terminalCharset;
    }

    private static GraphvizGenerator<SocialGraph.Node, SocialGraph.Edge> newGenerator() {
        return new GraphvizGenerator<>(
                SocialGraph.Node::getLabel,
                SocialGraph.Edge::getSource,
                SocialGraph.Edge::getTarget
        );
    }

    private GraphvizGenerator.NodeConfigurator<SocialGraph.Node> getNodeConfiguration() {
        return (node, graphvisNode) -> {
            ConsumerAgent ca = node.getAgent(ConsumerAgent.class);
            ConsumerAgentGroup cag = ca.getGroup();
            Color color = getColor(cag);
            if(color == null) {
                LOGGER.info("no color set for ConsumerAgentGroup '{}', using black for node", cag.getName());
                color = Color.BLACK;
            }
            graphvisNode.add(color);
        };
    }

    private GraphvizGenerator.LinkConfigurator<SocialGraph.Edge> getLinkConfiguration() {
        return (edge, graphvizLink) -> {
            SocialGraph.Node source = edge.getSource();
            ConsumerAgent ca = source.getAgent(ConsumerAgent.class);
            ConsumerAgentGroup cag = ca.getGroup();
            Color color = getColor(cag);
            if(color == null) {
                LOGGER.info("no color set for ConsumerAgentGroup '{}', using black for links", cag.getName());
                color = Color.BLACK;
            }
            graphvizLink.add(color);
        };
    }

    private GraphvizGenerator<SocialGraph.Node, SocialGraph.Edge> createGenerator() {
        GraphvizGenerator<SocialGraph.Node, SocialGraph.Edge> gen = newGenerator();
        gen.setDirected(true);
        return gen;
    }

    private void applySocialNetwork(
            GraphvizGenerator<SocialGraph.Node, SocialGraph.Edge> gen,
            SocialGraph graph,
            SocialGraph.Type edgeType) {
        gen.addAllNodes(graph.getNodes());
        gen.addAllLinks(graph.getEdges(edgeType));
    }

    private void setupGenerator(GraphvizGenerator<SocialGraph.Node, SocialGraph.Edge> gen) {
        gen.configureNodes(getNodeConfiguration());
        gen.configureLinks(getLinkConfiguration());
        gen.setHideNodeLabels(true);
        gen.setNodeShape(Shape.POINT);
        if(setDefaultSize) {
            gen.setPixelSizePreferred(800, 800);
        }
    }

    protected Path createTempDotPathIfNull(Path inputDotPath, Path outputFile) {
        if(inputDotPath == null) {
            return outputFile.resolveSibling(outputFile.getFileName().toString() + ".dot");
        } else {
            return inputDotPath;
        }
    }

    @Override
    public void printSocialGraph(
            SocialGraph graph,
            SocialGraph.Type edgeType,
            Path outputFile,
            Path dotFile) throws Exception {
        GraphvizGenerator<SocialGraph.Node, SocialGraph.Edge> gen = createGenerator();
        applySocialNetwork(gen, graph, edgeType);
        setupGenerator(gen);

        boolean isTempDotFile = dotFile == null;
        Path usedDotFile = createTempDotPathIfNull(dotFile, outputFile);
        try {
            LOGGER.trace("store dot file (temp={}): {}", isTempDotFile, usedDotFile);
            gen.store(usedDotFile);

            LOGGER.trace("init dot process");
            DotProcess process;
            try {
                process = new DotProcess()
                        .setInputFile(usedDotFile)
                        .setOutputFile(outputFile)
                        .setLayout(layoutAlgorithm)
                        .setFormat(outputFormat)
                        .setScale(scaleFactor)
                        .enableNeatoPos(fixedNeatoPosition)
                        .peek(dp -> LOGGER.trace("cmd: {}", Arrays.toString(dp.buildCommand())))
                        .validate();
            } catch (Exception e) {
                LOGGER.error("dot process validation failed", e);
                return;
            }

            LOGGER.trace("execute dot process");
            ProcessResult result;
            try {
                result = process.execute();
            } catch (Exception e) {
                LOGGER.error("dot process execution failed", e);
                return;
            }

            if(result.isOk()) {
                LOGGER.trace("OK {}", result.printData(terminalCharset));
            } else {
                LOGGER.error("ERR {}", result.printErr(terminalCharset));
            }

            if(Files.exists(outputFile)) {
                LOGGER.trace("image created ({})", outputFile);
            } else {
                LOGGER.error("image not created ({})", outputFile);
            }

        } finally {
            if(isTempDotFile) {
                LOGGER.trace("temp-dot file ({}) deleted: {}", usedDotFile, Files.deleteIfExists(usedDotFile));
            } else {
                LOGGER.trace("keep dot file: {}", usedDotFile);
            }
        }
    }
}
