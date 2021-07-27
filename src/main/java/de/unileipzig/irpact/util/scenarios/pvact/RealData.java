package de.unileipzig.irpact.util.scenarios.pvact;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.io.param.input.affinity.InAffinities;
import de.unileipzig.irpact.io.param.input.affinity.InAffinityEntry;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.distribution.InBernoulliDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InDiracUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InTruncatedNormalDistribution;
import de.unileipzig.irpact.util.pvact.Milieu;

import java.util.*;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public final class RealData {

    //=========================
    //xlsx
    //=========================

    public static final List<Milieu> XLSX_ORDER = Arrays.asList(
            Milieu.KET,
            Milieu.LIB,
            Milieu.PER,
            Milieu.EPE,
            Milieu.PRA,
            Milieu.SOK,
            Milieu.BUM,
            Milieu.TRA,
            Milieu.PRE,
            Milieu.HED
    );
    public static final Milieu[] XLSX_ORDER_ARR = XLSX_ORDER.toArray(new Milieu[0]);

    public static final double[] NS_MEANS = {
            0.469977553,
            0.440444645,
            0.538128934,
            0.459394907,
            0.493895967,
            0.369047621,
            0.418478262,
            0.335937498,
            0.334325395,
            0.476092895
    };
    public static final double[] NS_SD = {
            0.209684591,
            0.224698364,
            0.212903502,
            0.205263117,
            0.224942382,
            0.212907301,
            0.216839083,
            0.219753169,
            0.249955992,
            0.222675603
    };

    public static final double[] DEP_MEANS = {
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1
    };
    public static final double[] DEP_SD = {
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0
    };

    public static final double[] NEP_MEANS = {
            0.722351721,
            0.746942099,
            0.750483784,
            0.746611134,
            0.737546946,
            0.791276615,
            0.72098352,
            0.745993586,
            0.695360195,
            0.686212698
    };
    public static final double[] NEP_SD = {
            0.15589877,
            0.1469412,
            0.15294908,
            0.131716696,
            0.150078226,
            0.140908223,
            0.151834724,
            0.173108196,
            0.179601083,
            0.177211469
    };

    public static final double[] NSIZE = {
            0.50953985,
            0.521790348,
            0.527515731,
            0.468683659,
            0.480360943,
            0.441358031,
            0.42632851,
            0.397321432,
            0.366402122,
            0.487021861
    };
    public static final double SCALE = 0.5;
    public static final double MULTIPLIER = 15;

    public static final double WEIGHT_NS = 0.3472;
    public static final double WEIGHT_NEP = -0.0780;
    public static final double WEIGHT_EK = 0.0013;
    public static final double WEIGHT_NPV = 0.5673;
    public static final double WEIGHT_SOCIAL = 0.0113;
    public static final double WEIGHT_LOCALE = 0.1509;

    public static final double[] COMMU = {
            0.204,
            0.203,
            0.214,
            0.210,
            0.216,
            0.191,
            0.192,
            0.174,
            0.145,
            0.206
    };

    public static final double[] INITIAL_ADOPTER = {
            0.1345,
            0.1218,
            0.1193,
            0.0406,
            0.1675,
            0.0838,
            0.0685,
            0.0508,
            0.0203,
            0.1929
    };

    public static final double[] REWIRE = {
            0.5,
            0.75,
            1,
            1,
            0.75,
            0.75,
            0.5,
            0.25,
            0.5,
            1
    };

    public final PvActConsumerAgentGroupBuilder CAGS;
    public final InPVactConsumerAgentGroup[] KET_;
    public final InPVactConsumerAgentGroup[] LIB_;
    public final InPVactConsumerAgentGroup[] PER_;
    public final InPVactConsumerAgentGroup[] EXP_;
    public final InPVactConsumerAgentGroup[] PRA_;
    public final InPVactConsumerAgentGroup[] SOK_;
    public final InPVactConsumerAgentGroup[] BUM_;
    public final InPVactConsumerAgentGroup[] TRA_;
    public final InPVactConsumerAgentGroup[] PRE_;
    public final InPVactConsumerAgentGroup[] HED_;

    public RealData(Function<? super String, ? extends InPVactConsumerAgentGroup> cagCreator) {
        CAGS = new PvActConsumerAgentGroupBuilder(cagCreator, Milieu.WITHOUT_G);
        KET_ = CAGS.part(Milieu.KET, Milieu.LIB, Milieu.SOK, Milieu.BUM, Milieu.TRA);
        LIB_ = CAGS.part(Milieu.KET, Milieu.LIB, Milieu.PER, Milieu.SOK);
        PER_ = CAGS.part(Milieu.LIB, Milieu.PER, Milieu.EPE, Milieu.PRA, Milieu.SOK);
        EXP_ = CAGS.part(Milieu.PER, Milieu.EPE, Milieu.PRA, Milieu.HED);
        PRA_ = CAGS.part(Milieu.PER, Milieu.EPE, Milieu.PRA, Milieu.SOK, Milieu.BUM, Milieu.HED);
        SOK_ = CAGS.part(Milieu.KET, Milieu.LIB, Milieu.PER, Milieu.PRA, Milieu.SOK, Milieu.BUM);
        BUM_ = CAGS.part(Milieu.KET, Milieu.PRA, Milieu.SOK, Milieu.BUM, Milieu.TRA, Milieu.PRE, Milieu.HED);
        TRA_ = CAGS.part(Milieu.KET, Milieu.BUM, Milieu.TRA, Milieu.PRE);
        PRE_ = CAGS.part(Milieu.BUM, Milieu.TRA, Milieu.PRE, Milieu.HED);
        HED_ = CAGS.part(Milieu.EPE, Milieu.PRA, Milieu.BUM, Milieu.PRE, Milieu.HED);
    }

    //=========================
    //static
    //=========================

    public static double[] calculate(InConsumerAgentGroup[] all, InConsumerAgentGroup src, InConsumerAgentGroup[] tars, double self) {
        double rest = 1.0 - self;
        double restPart = rest / tars.length;
        double[] affinities = new double[all.length];
        for(int i = 0; i < affinities.length; i++) {
            if(all[i] == src) {
                affinities[i] = self;
            }
            else if(CollectionUtil.containsSame(tars, all[i])) {
                affinities[i] = restPart;
            }
            else {
                affinities[i] = 0.0;
            }
        }
        return affinities;
    }

    public static InComplexAffinityEntry[] buildEntries(InConsumerAgentGroup[] all, InConsumerAgentGroup src, InConsumerAgentGroup[] tars, double self) {
        double[] values = calculate(all, src, tars, self);
        return buildEntries(src, all, values);
    }

    public static InComplexAffinityEntry[] buildEntries(InConsumerAgentGroup src, InConsumerAgentGroup[] all, double[] values) {
        InComplexAffinityEntry[] entries = new InComplexAffinityEntry[all.length];
        for(int i = 0; i < entries.length; i++) {
            entries[i] = new InComplexAffinityEntry(
                    src.getName() + "_" + all[i].getName(),
                    src,
                    all[i],
                    values[i]
            );
        }
        return entries;
    }

    public static Map<Milieu, InTruncatedNormalDistribution> buildTruncNorm(
            String name,
            Milieu[] milieus,
            double[] means,
            double[] sds) {
        Map<Milieu, InTruncatedNormalDistribution> map = new LinkedHashMap<>();
        for(int i = 0; i < milieus.length; i++) {
            map.put(
                    milieus[i],
                    new InTruncatedNormalDistribution(name + "_" + milieus[i].print(), sds[i], means[i], 0, 1)
            );
        }
        return map;
    }

    public static Map<Milieu, InBernoulliDistribution> buildBernoulli(
            String name,
            Milieu[] milieus,
            double[] p) {
        Map<Milieu, InBernoulliDistribution> map = new LinkedHashMap<>();
        for(int i = 0; i < milieus.length; i++) {
            map.put(
                    milieus[i],
                    new InBernoulliDistribution(name + "_" + milieus[i].print(), p[i])
            );
        }
        return map;
    }

    public static Map<Milieu, InDiracUnivariateDistribution> buildDirac(
            String name,
            Milieu[] milieus,
            double[] p) {
        Map<Milieu, InDiracUnivariateDistribution> map = new LinkedHashMap<>();
        for(int i = 0; i < milieus.length; i++) {
            map.put(
                    milieus[i],
                    new InDiracUnivariateDistribution(name + "_" + milieus[i].print(), p[i])
            );
        }
        return map;
    }

    public static Map<Milieu, Integer> calcEdgeCount(Milieu[] milieus, double[] nsize, double scale, double defaultSize) {
        Map<Milieu, Integer> map = new LinkedHashMap<>();

        for(int i = 0; i < milieus.length; i++) {
            double scaled = nsize[i] + scale;
            double muliplied = scaled * defaultSize;
            int edgeCount = (int) (muliplied < defaultSize ? Math.floor(muliplied) : Math.ceil(muliplied));
            map.put(milieus[i], edgeCount);
        }

        return map;
    }

    //=========================
    //this
    //=========================

    public InAffinities buildAffinities(String name, double self) {
        InPVactConsumerAgentGroup[] all = CAGS.cags();

        List<InComplexAffinityEntry> entries = new ArrayList<>();
        Collections.addAll(entries, buildEntries(all, CAGS.get(Milieu.KET), KET_, self));
        Collections.addAll(entries, buildEntries(all, CAGS.get(Milieu.LIB), LIB_, self));
        Collections.addAll(entries, buildEntries(all, CAGS.get(Milieu.PER), PER_, self));
        Collections.addAll(entries, buildEntries(all, CAGS.get(Milieu.EPE), EXP_, self));
        Collections.addAll(entries, buildEntries(all, CAGS.get(Milieu.PRA), PRA_, self));
        Collections.addAll(entries, buildEntries(all, CAGS.get(Milieu.SOK), SOK_, self));
        Collections.addAll(entries, buildEntries(all, CAGS.get(Milieu.BUM), BUM_, self));
        Collections.addAll(entries, buildEntries(all, CAGS.get(Milieu.TRA), TRA_, self));
        Collections.addAll(entries, buildEntries(all, CAGS.get(Milieu.PRE), PRE_, self));
        Collections.addAll(entries, buildEntries(all, CAGS.get(Milieu.HED), HED_, self));

        return new InAffinities(name, entries.toArray(new InAffinityEntry[0]));
    }
}
