package de.unileipzig.irpact.core.misc.graphviz;

import de.unileipzig.irpact.commons.util.IRPactBase32;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irptools.graphviz.DotProcess;
import de.unileipzig.irptools.graphviz.GraphvizGenerator;
import de.unileipzig.irptools.graphviz.LayoutAlgorithm;
import de.unileipzig.irptools.graphviz.OutputFormat;
import de.unileipzig.irptools.graphviz.def.GraphvizColor;
import de.unileipzig.irptools.util.ProcessResult;
import de.unileipzig.irptools.util.Util;
import de.unileipzig.irptools.util.log.IRPLogger;
import guru.nidi.graphviz.attribute.Shape;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DeflaterInputStream;

/**
 * @author Daniel Abitz
 */
public class BasicGraphvizConfiguration implements GraphvizConfiguration {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicGraphvizConfiguration.class);

    protected static final int LINE_LEN = 76;

    protected Map<ConsumerAgentGroup, GraphvizColor> colorMapping;
    protected LayoutAlgorithm layoutAlgorithm;
    protected OutputFormat outputFormat;
    protected Path imageOutputPath;
    protected boolean logDotFile;

    public BasicGraphvizConfiguration() {
        this(new HashMap<>());
    }

    public BasicGraphvizConfiguration(Map<ConsumerAgentGroup, GraphvizColor> colorMapping) {
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

    public void putColor(ConsumerAgentGroup cag, GraphvizColor color) {
        colorMapping.put(cag, color);
    }

    public GraphvizColor getColor(ConsumerAgentGroup cag) {
        return colorMapping.get(cag);
    }

    public void setImageOutputPath(Path imageOutputPath) {
        this.imageOutputPath = imageOutputPath;
    }

    public Path getImageOutputPath() {
        return imageOutputPath;
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
            GraphvizColor color = getColor(cag);
            if(color == null) {
                LOGGER.info("no color set for ConsumerAgentGroup '{}', using black", cag.getName());
                color = GraphvizColor.BLACK;
            }
            graphvisNode.add(color.toGraphvizColor());
        };
    }

    private GraphvizGenerator.LinkConfigurator<SocialGraph.Edge> getLinkConfiguration() {
        return (edge, graphvizLink) -> {
            SocialGraph.Node source = edge.getSource();
            ConsumerAgent ca = source.getAgent(ConsumerAgent.class);
            ConsumerAgentGroup cag = ca.getGroup();
            GraphvizColor color = getColor(cag);
            if(color == null) {
                LOGGER.info("no color set for ConsumerAgentGroup '{}', using black", cag.getName());
                color = GraphvizColor.BLACK;
            }
            graphvizLink.add(color.toGraphvizColor());
        };
    }

    private GraphvizGenerator<SocialGraph.Node, SocialGraph.Edge> createAndSetupNewGenerator() {
        GraphvizGenerator<SocialGraph.Node, SocialGraph.Edge> gen = newGenerator();
        gen.setDirected(true);
        gen.configureNodes(getNodeConfiguration());
        gen.configureLinks(getLinkConfiguration());
        gen.setHideNodeLabels(true);
        gen.setNodeShape(Shape.POINT);
        return gen;
    }

    private void applySocialNetwork(
            GraphvizGenerator<SocialGraph.Node, SocialGraph.Edge> gen,
            SocialGraph graph,
            SocialGraph.Type edgeType) {
        gen.addAllNodes(graph.getNodes());
        gen.addAllLinks(graph.getEdges(edgeType));
    }

    private void logContent(Path file) throws IOException {
        byte[] content;
        try(InputStream in = Files.newInputStream(file);
            DeflaterInputStream defIn = new DeflaterInputStream(in)) {
            content = Util.readAll(defIn);
        }
        String b32 = IRPactBase32.encodeToString(content);
        String splittedB32 = de.unileipzig.irpact.commons.Util.splitLen(b32, LINE_LEN);
        LOGGER.info("dot file content\n{}", splittedB32);
    }

    @Override
    public void printSocialGraph(SocialGraph graph, SocialGraph.Type edgeType) throws Exception {
        GraphvizGenerator<SocialGraph.Node, SocialGraph.Edge> gen = createAndSetupNewGenerator();
        applySocialNetwork(gen, graph, edgeType);

        Path dotPath = imageOutputPath.resolveSibling(imageOutputPath.getFileName().toString() + ".dot");
        try {
            LOGGER.debug("store temp-dot file: {}", dotPath);
            gen.store(dotPath);

            if(logDotFile) {
                logContent(dotPath);
            }

            LOGGER.debug("init dot process");
            DotProcess process;
            try {
                process = new DotProcess()
                        .setInputFile(dotPath)
                        .setOutputFile(imageOutputPath)
                        .setLayout(layoutAlgorithm)
                        .setFormat(outputFormat)
                        .peek(dp -> LOGGER.trace("cmd: {}", Arrays.toString(dp.buildCommand())))
                        .validate();
            } catch (Exception e) {
                LOGGER.error("dot process validation failed", e);
                return;
            }

            LOGGER.debug("execute dot process");
            ProcessResult result;
            try {
                result = process.execute();
            } catch (Exception e) {
                LOGGER.error("dot process execution failed", e);
                return;
            }

            if(result.isOk()) {
                LOGGER.trace("OK {}", result.printData(Util.IBM850()));
            } else {
                LOGGER.error("ERR {}", result.printErr(Util.IBM850()));
            }

            if(Files.exists(imageOutputPath)) {
                LOGGER.debug("image created ({})", imageOutputPath);
            } else {
                LOGGER.error("image not created ({})", imageOutputPath);
            }

        } finally {
            if(Files.deleteIfExists(dotPath)) {
                LOGGER.trace("temp-dot file deleted: {}", dotPath);
            }
        }
    }
}
