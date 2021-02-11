package de.unileipzig.irpact.start.optact;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.start.optact.gvin.AgentGroup;
import de.unileipzig.irpact.start.optact.gvin.GvInRoot;
import de.unileipzig.irpact.start.optact.in.SideCustom;
import de.unileipzig.irpact.start.optact.out.OutCustom;
import de.unileipzig.irpact.start.optact.out.OutRoot;
import de.unileipzig.irpact.commons.graph.DirectedAdjacencyListMultiGraph;
import de.unileipzig.irpact.commons.graph.topology.AbstractMultiGraphTopology;
import de.unileipzig.irpact.commons.graph.topology.GraphTopology;
import de.unileipzig.irptools.defstructure.*;
import de.unileipzig.irptools.graphviz.DotProcess;
import de.unileipzig.irptools.graphviz.GraphvizGenerator;
import de.unileipzig.irptools.graphviz.LayoutAlgorithm;
import de.unileipzig.irptools.graphviz.OutputFormat;
import de.unileipzig.irptools.graphviz.def.GraphvizColor;
import de.unileipzig.irptools.io.ContentType;
import de.unileipzig.irptools.io.ContentTypeDetector;
import de.unileipzig.irptools.io.annual.AnnualData;
import de.unileipzig.irptools.io.annual.AnnualFile;
import de.unileipzig.irptools.io.base.AnnualEntry;
import de.unileipzig.irptools.util.ProcessResult;
import de.unileipzig.irptools.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * @author Daniel Abitz
 */
public class OptActMain implements Callable<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(OptActMain.class);

    @CommandLine.Option(
            names = { "-i", "--input" },
            description = "path to input file"
    )
    @SuppressWarnings("unused")
    private String inputFile;

    @CommandLine.Option(
            names = { "-o", "--output" },
            description = "path to output file"
    )
    @SuppressWarnings("unused")
    private String outputFile;

    @CommandLine.Option(
            names = { "--image" },
            description = "path to image file"
    )
    @SuppressWarnings("unused")
    private String imageFile;

    @CommandLine.Option(
            names = { "--noSimulation" },
            description = "disable simulation"
    )
    @SuppressWarnings("unused")
    private boolean noSimulation;

    private Path inputPath;
    private Path outputPath;
    private Path imagePath;

    public OptActMain() {
    }

    private static double calc_par_out_IuOSonnentankNetzversorgung_Summe(SideCustom agentGrp, double mwst) {
        double sumOrId = agentGrp.timeStuff.hasData()
                ? Arrays.stream(agentGrp.timeStuff.toArray()).sum()
                : Integer.parseInt(agentGrp.timeStuff.getId());
        return sumOrId * mwst;
    }

    private static int calc_par_out_S_DS(SideCustom agentGrp) {
        return agentGrp.number + agentGrp.delta;
    }

    private Converter createInputConverter() {
        DefinitionCollection dcoll = AnnotationParser.parse(GvInRoot.CLASSES);
        DefinitionMapper dmap = new DefinitionMapper(dcoll);
        return new Converter(dmap);
    }

    private Converter createOutputConverter() {
        DefinitionCollection dcoll = AnnotationParser.parse(OutRoot.CLASSES);
        DefinitionMapper dmap = new DefinitionMapper(dcoll);
        return new Converter(dmap);
    }

    private AnnualEntry<GvInRoot> loadInput(Converter converter) throws IOException {
        logger.trace("parse input file");
        ObjectNode rootNode = Util.readJson(inputPath, StandardCharsets.UTF_8);
        ContentType contentType = ContentTypeDetector.detect(rootNode);
        logger.trace("content type: {}", contentType);
        logger.trace("deserialize input file");
        return ContentTypeDetector.parseFirstEntry(rootNode, contentType, converter);
    }

    private OutRoot createOutputRoot(GvInRoot inputData) {
        List<OutCustom> outGrps = new ArrayList<>();
        for(SideCustom agentGrp: inputData.customs) {
            OutCustom outGrp = new OutCustom(agentGrp._name);
            outGrp.summe = calc_par_out_IuOSonnentankNetzversorgung_Summe(agentGrp, inputData.global.mwst);
            outGrp.numberOut = calc_par_out_S_DS(agentGrp);
            outGrps.add(outGrp);
        }

        OutRoot outRoot = new OutRoot();
        outRoot.outGrps = outGrps.toArray(new OutCustom[0]);
        return outRoot;
    }

    private AnnualData<OutRoot> createOutputData(AnnualEntry<GvInRoot> inputEntry, OutRoot outRoot) {
        AnnualData<OutRoot> outData = new AnnualData<>(outRoot);
        outData.getConfig().copyFrom(inputEntry.getConfig());
        return outData;
    }

    public void run() {
        AnnualEntry<GvInRoot> inputEntry;
        try {
            inputEntry = loadInput(createInputConverter());
        } catch (IOException e) {
            logger.error("parsing and deserialization failed", e);
            return;
        }

        try {
            if(imagePath != null) {
                runImageDemo(inputEntry);
            }
            if(!noSimulation) {
                runOptActDemo(inputEntry);
            }
            logger.trace("IRPact finished");
        } catch (IOException e) {
            logger.error("IRPact failed", e);
        }
    }

    private void runImageDemo(AnnualEntry<GvInRoot> inputEntry) throws IOException {
        logger.trace("run image demo");
        GvInRoot root = inputEntry.getData();

        LayoutAlgorithm layoutAlgorithm = root.getLayoutAlgorithm();
        if(layoutAlgorithm == null) {
            logger.error("Keine gültiger LayoutAlgorithm gefunden!");
            return;
        }

        OutputFormat outputFormat = root.getOutputFormat();
        if(outputFormat == null) {
            logger.error("Keine gültiges OutputFormat gefunden!");
            return;
        }

        DirectedAdjacencyListMultiGraph<Node, Link, String> g = new DirectedAdjacencyListMultiGraph<>();
        for(AgentGroup grp: root.agentGroups) {
            logger.trace("Gruppe: '{}', Agenten: {}", grp._name, grp.numberOfAgents);
            for(int i = 0; i < grp.numberOfAgents; i++) {
                g.addVertex(new Node(grp._name + "#" + i));
            }
        }
        logger.trace("agents: " + g.vertexCount());

        GraphTopology<Node, Link> topo = root.getTopology();
        if(topo == null) {
            logger.error("Keine gültige Topologie gefunden!");
            return;
        }
        ((AbstractMultiGraphTopology<Node, Link, String>) topo).setEdgeType("x");
        ((AbstractMultiGraphTopology<Node, Link, String>) topo).setMultiEdgeCreatorFunction(Link::create);

        logger.trace("initalize edges");
        topo.initalizeEdges(g);
        logger.trace("edges: " + g.edgeCount());

        GraphvizGenerator<Node, Link> gen = new GraphvizGenerator<>(
                Node::getLabel,
                Link::getSource,
                Link::getTarget
        );
        gen.setDirected(true);
        gen.addAllNodes(g.getVertices());
        gen.addAllLinks(g.getEdges("x"));
        gen.configureNodes((n, gv) -> {
            GraphvizColor gcolor = root.getColor(n.getLabel());
            if(gcolor == null) {
                gcolor = GraphvizColor.BLACK;
            }
            gv.add(gcolor.toGraphvizColor());
        });
        gen.configureLinks((l, gl) -> {
            GraphvizColor gcolor = root.getColor(l.getSource().getLabel());
            if(gcolor == null) {
                gcolor = GraphvizColor.BLACK;
            }
            gl.add(gcolor.toGraphvizColor());
        });
        gen.setHideNodeLabels(true);
        gen.setNodeShape(guru.nidi.graphviz.attribute.Shape.POINT);

        Path dotPath = imagePath.resolveSibling(imagePath.getFileName().toString() + ".dot");
        try {
            logger.debug("store temp-dot file: {}", dotPath);
            gen.store(dotPath);

            logger.debug("init dot process");
            DotProcess process;
            try {
                process = new DotProcess()
                        .setInputFile(dotPath)
                        .setOutputFile(imagePath)
                        .setLayout(layoutAlgorithm)
                        .setFormat(outputFormat)
                        .peek(dp -> logger.trace("cmd: {}", Arrays.toString(dp.buildCommand())))
                        .validate();
            } catch (Exception e) {
                logger.error("dot process validation failed", e);
                return;
            }

            logger.debug("execute dot process");

            ProcessResult result;
            try {
                result = process.execute();
            } catch (Exception e) {
                logger.error("dot process execution failed", e);
                return;
            }

            if(result.isOk()) {
                logger.trace("OK {}", result.printData(Util.IBM850()));
            } else {
                logger.error("ERR {}", result.printErr(Util.IBM850()));
            }

            if(Files.exists(imagePath)) {
                logger.debug("image created ({})", imagePath);
            } else {
                logger.error("image not created ({})", imagePath);
            }
        } finally {
            if(Files.deleteIfExists(dotPath)) {
                logger.trace("temp-dot file deleted: {}", dotPath);
            }
        }
    }

    private void runOptActDemo(AnnualEntry<GvInRoot> inputEntry) {
        logger.trace("run optact demo");
        if(noSimulation) {
            logger.warn("no simulation");
            return;
        }

        storeOutputToAnnualFile(inputEntry);
    }

    private void storeOutputToAnnualFile(AnnualEntry<GvInRoot> inputEntry) {
        logger.trace("generate output data");
        OutRoot outRoot = createOutputRoot(inputEntry.getData());
        logger.trace("create raw output");
        AnnualData<OutRoot> outData = createOutputData(inputEntry, outRoot);
        logger.trace("serialize output");
        try {
            AnnualFile outFile = outData.serialize(createOutputConverter());
            outFile.store(outputPath);
        } catch (Throwable t) {
            logger.error("serialization failed", t);
        }
    }

    @Override
    public Integer call() {
        if(inputFile == null) {
            logger.error("input file missing");
            return CommandLine.ExitCode.USAGE;
        }
        inputPath = Paths.get(inputFile);
        logger.debug("input file: {}", inputFile);
        if(Files.notExists(inputPath)) {
            logger.error("input file not found: {}", inputPath);
            return CommandLine.ExitCode.SOFTWARE;
        }

        if(outputFile == null) {
            if(!noSimulation) {
                logger.error("output file missing");
                return CommandLine.ExitCode.USAGE;
            }
        } else {
            outputPath = Paths.get(outputFile);
            logger.debug("output file: {}", outputPath);
        }

        if(imageFile != null) {
            imagePath = Paths.get(imageFile);
            logger.debug("image file: {}", imagePath);
        }

        if(noSimulation) {
            logger.debug("simulation disabled");
        } else {
            logger.debug("simulation enabled");
        }

        return CommandLine.ExitCode.OK;
    }

    //Helper Graph
    private static class Node {
        private final String label;

        private Node(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    //Helper Graph
    private static class Link {
        private final Node source;
        private final Node target;

        private Link(Node source, Node target) {
            this.source = source;
            this.target = target;
        }

        private static Link create(Node source, Node target, String type) {
            return new Link(source, target);
        }

        public Node getSource() {
            return source;
        }

        public Node getTarget() {
            return target;
        }
    }

    public static void main(String[] args) {
        OptActMain optact = new OptActMain();
        CommandLine cmdLine = new CommandLine(optact);
        int exitCode = cmdLine.execute(args);
        if(exitCode == CommandLine.ExitCode.OK) {
            optact.run();
        } else {
            logger.error("start failed, exit code: {}", exitCode);
            System.exit(exitCode);
        }
    }
}
