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
import de.unileipzig.irptools.util.Util;
import de.unileipzig.irptools.util.log.IRPLogger;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Shape;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
    protected Path downloadDir;
    protected Path imageOutputPath;
    protected boolean storeDotFile;
    protected boolean fixedNeatoPosition;
    protected double scaleFactor;

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

    public void setImageOutputPath(Path imageOutputPath) {
        this.imageOutputPath = imageOutputPath;
    }

    public Path getImageOutputPath() {
        return imageOutputPath;
    }

    public void setStoreDotFile(boolean storeDotFile) {
        this.storeDotFile = storeDotFile;
    }

    public boolean isStoreDotFile() {
        return storeDotFile;
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

    public void setDownloadDir(Path downloadDir) {
        this.downloadDir = downloadDir;
    }

    public Path getDownloadDir() {
        return downloadDir;
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
    }

    private void storeDotFile(Path temp) throws IOException {
        if(downloadDir == null) {
            LOGGER.warn("no download dir set, delete '{}': {}", temp.getFileName(), Files.deleteIfExists(temp));
        } else {
            if(Files.notExists(downloadDir)) {
                Files.createDirectories(downloadDir);
            }
            Path target = downloadDir.resolve("AgentNetwork.dot");
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
            LOGGER.trace("dot file stored: {}", target);
        }
    }

    @Override
    public void printSocialGraph(SocialGraph graph, SocialGraph.Type edgeType) throws Exception {
        GraphvizGenerator<SocialGraph.Node, SocialGraph.Edge> gen = createGenerator();
        applySocialNetwork(gen, graph, edgeType);
        setupGenerator(gen);

        Path dotPath = imageOutputPath.resolveSibling(imageOutputPath.getFileName().toString() + ".dot");
        try {
            LOGGER.trace("store temp-dot file: {}", dotPath);
            gen.store(dotPath);

            LOGGER.trace("init dot process");
            DotProcess process;
            try {
                process = new DotProcess()
                        .setInputFile(dotPath)
                        .setOutputFile(imageOutputPath)
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
                LOGGER.trace("OK {}", result.printData(Util.IBM850()));
            } else {
                LOGGER.error("ERR {}", result.printErr(Util.IBM850()));
            }

            if(Files.exists(imageOutputPath)) {
                LOGGER.trace("image created ({})", imageOutputPath);
            } else {
                LOGGER.error("image not created ({})", imageOutputPath);
            }

        } finally {
            if(isStoreDotFile()) {
                storeDotFile(dotPath);
            } else {
                LOGGER.trace("temp-dot file ({}) deleted: {}", dotPath, Files.deleteIfExists(dotPath));
            }
        }
    }
}
