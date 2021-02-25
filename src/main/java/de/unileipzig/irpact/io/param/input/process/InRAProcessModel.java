package de.unileipzig.irpact.io.param.input.process;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.process.FixProcessModelFindingScheme;
import de.unileipzig.irpact.core.process.ra.RAModelData;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.core.process.ra.npv.NPVXlsxData;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InRAProcessModel implements InProcessModel {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InRAProcessModel.class,
                res.getCachedElement("Prozessmodell"),
                res.getCachedElement("Relative Agreement")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("a")
                .setGamsDescription("a")
                .store(InRAProcessModel.class, "a");
        res.newEntryBuilder()
                .setGamsIdentifier("b")
                .setGamsDescription("b")
                .store(InRAProcessModel.class, "b");
        res.newEntryBuilder()
                .setGamsIdentifier("c")
                .setGamsDescription("c")
                .store(InRAProcessModel.class, "c");
        res.newEntryBuilder()
                .setGamsIdentifier("d")
                .setGamsDescription("d")
                .store(InRAProcessModel.class, "d");

        res.newEntryBuilder()
                .setGamsIdentifier("Adopter-Punkte")
                .setGamsDescription("-")
                .store(InRAProcessModel.class, "adopterPoints");
        res.newEntryBuilder()
                .setGamsIdentifier("Interessenten-Punkte")
                .setGamsDescription("-")
                .store(InRAProcessModel.class, "interestedPoints");
        res.newEntryBuilder()
                .setGamsIdentifier("Aware-Punkte")
                .setGamsDescription("-")
                .store(InRAProcessModel.class, "awarePoints");
        res.newEntryBuilder()
                .setGamsIdentifier("Unbekannt-Punkte")
                .setGamsDescription("-")
                .store(InRAProcessModel.class, "unknownPoints");

        res.newEntryBuilder()
                .setGamsIdentifier("PV Datei")
                .setGamsDescription("-")
                .store(InRAProcessModel.class, "pvFile");

        res.newEntryBuilder()
                .setGamsIdentifier("Datenerweiterung-Neigung")
                .setGamsDescription("-")
                .store(InRAProcessModel.class, "slopeSuppliers");

        res.newEntryBuilder()
                .setGamsIdentifier("Datenerweiterung-Orientierung")
                .setGamsDescription("-")
                .store(InRAProcessModel.class, "orientationSuppliers");

        res.newEntryBuilder()
                .setGamsIdentifier("Attribute für Unsicherheit")
                .setGamsDescription("-")
                .store(InRAProcessModel.class, "uncertaintyGroupAttributes");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InRAProcessModel.class);

    public String _name;

    @FieldDefinition
    public double a;

    @FieldDefinition
    public double b;

    @FieldDefinition
    public double c;

    @FieldDefinition
    public double d;

    @FieldDefinition
    public int adopterPoints = 3;

    @FieldDefinition
    public int interestedPoints = 2;

    @FieldDefinition
    public int awarePoints = 1;

    @FieldDefinition
    public int unknownPoints = 0;

    @FieldDefinition
    public InPVFile pvFile;

    @FieldDefinition
    public InSlopeSupplier[] slopeSuppliers = new InSlopeSupplier[0];

    @FieldDefinition
    public InOrientationSupplier[] orientationSuppliers = new InOrientationSupplier[0];

    @FieldDefinition
    public InUncertaintyGroupAttribute[] uncertaintyGroupAttributes = new InUncertaintyGroupAttribute[0];

    public InRAProcessModel() {
    }

    public InRAProcessModel(
            String name,
            double a, double b, double c, double d,
            int adopterPoints, int interestedPoints, int awarePoints, int unknownPoints,
            InPVFile pvFile,
            InSlopeSupplier[] slopeSuppliers,
            InOrientationSupplier[] orientationSuppliers,
            InUncertaintyGroupAttribute[] uncertaintyGroupAttributes) {
        this._name = name;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.adopterPoints = adopterPoints;
        this.interestedPoints = interestedPoints;
        this.awarePoints = awarePoints;
        this.unknownPoints = unknownPoints;
        this.pvFile = pvFile;
        this.slopeSuppliers = slopeSuppliers;
        this.orientationSuppliers = orientationSuppliers;
        this.uncertaintyGroupAttributes = uncertaintyGroupAttributes;
    }

    @Override
    public String getName() {
        return _name;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getD() {
        return d;
    }

    public int getAdopterPoints() {
        return adopterPoints;
    }

    public int getInterestedPoints() {
        return interestedPoints;
    }

    public int getAwarePoints() {
        return awarePoints;
    }

    public int getUnknownPoints() {
        return unknownPoints;
    }

    public InPVFile getPvFile() {
        return pvFile;
    }

    public InSlopeSupplier[] getSlopeSuppliers() {
        return slopeSuppliers;
    }

    public InOrientationSupplier[] getOrientationSuppliers() {
        return orientationSuppliers;
    }

    public InUncertaintyGroupAttribute[] getUncertaintyGroupAttributes() {
        return uncertaintyGroupAttributes;
    }

    @Override
    public Object parse(InputParser parser) throws ParsingException {
        RAModelData data = new RAModelData();
        data.setA(getA());
        data.setB(getB());
        data.setC(getC());
        data.setD(getD());
        data.setAdopterPoints(getAdopterPoints());
        data.setInterestedPoints(getInterestedPoints());
        data.setAwarePoints(getAwarePoints());
        data.setUnknownPoints(getUnknownPoints());

        Rnd rnd = parser.deriveRnd();
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "RAProcessModel '{}' uses seed: {}", getName(), rnd.getInitialSeed());

        RAProcessModel model = new RAProcessModel();
        model.setName(getName());
        model.setEnvironment(parser.getEnvironment());
        model.setModelData(data);
        model.setRnd(rnd);

        //FIXED
        FixProcessModelFindingScheme findingScheme = new FixProcessModelFindingScheme();
        findingScheme.setName("FIXED_ProcessFindingScheme");
        findingScheme.setModel(model);
        AgentManager agentManager = parser.getEnvironment().getAgents();
        for(ConsumerAgentGroup cag: agentManager.getConsumerAgentGroups()) {
            JadexConsumerAgentGroup jcag = (JadexConsumerAgentGroup) cag;
            jcag.setProcessFindingScheme(findingScheme);
            LOGGER.info(IRPSection.INITIALIZATION_PARAMETER, "set '{}' to '{}'", findingScheme.getName(), jcag.getName());
        }

        if(getSlopeSuppliers() != null) {
            for(InSlopeSupplier inSlope: getSlopeSuppliers()) {
                inSlope.setup(parser, model);
            }
        }
        if(getOrientationSuppliers() != null) {
            for(InOrientationSupplier inOri: getOrientationSuppliers()) {
                inOri.setup(parser, model);
            }
        }
        if(getUncertaintyGroupAttributes() != null) {
            for(InUncertaintyGroupAttribute inUncert: getUncertaintyGroupAttributes()) {
                inUncert.setup(parser, model);
            }
        }

        NPVXlsxData xlsxData = parser.parseEntityTo(getPvFile());
        model.setNpvData(xlsxData);

        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InRAProcessModel)) return false;
        InRAProcessModel that = (InRAProcessModel) o;
        return Double.compare(that.a, a) == 0 && Double.compare(that.b, b) == 0 && Double.compare(that.c, c) == 0 && Double.compare(that.d, d) == 0 && adopterPoints == that.adopterPoints && interestedPoints == that.interestedPoints && awarePoints == that.awarePoints && unknownPoints == that.unknownPoints && Objects.equals(_name, that._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, a, b, c, d, adopterPoints, interestedPoints, awarePoints, unknownPoints);
    }

    @Override
    public String toString() {
        return "InRAProcessModel{" +
                "_name='" + _name + '\'' +
                ", a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", d=" + d +
                ", adopterPoints=" + adopterPoints +
                ", interesetedPoints=" + interestedPoints +
                ", awarePoints=" + awarePoints +
                ", unknownPoints=" + unknownPoints +
                '}';
    }
}
