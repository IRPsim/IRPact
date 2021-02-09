package de.unileipzig.irpact.misc.tconfig;

import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.commons.Util;
import de.unileipzig.irpact.experimental.deprecated.input.agent.consumer.IConsumerAgentGroup;
import de.unileipzig.irpact.experimental.deprecated.input.agent.consumer.IConsumerAgentGroupAttribute;
import de.unileipzig.irpact.experimental.deprecated.input.awareness.IAwareness;
import de.unileipzig.irpact.experimental.deprecated.input.awareness.IFixedProductAwareness;
import de.unileipzig.irpact.experimental.deprecated.input.awareness.IThresholdAwareness;
import de.unileipzig.irpact.experimental.deprecated.input.distribution.IConstantUnivariateDistribution;
import de.unileipzig.irpact.experimental.deprecated.input.distribution.IFiniteMassPointsDiscreteDistribution;
import de.unileipzig.irpact.experimental.deprecated.input.distribution.IMassPoint;
import de.unileipzig.irpact.experimental.deprecated.input.distribution.IUnivariateDoubleDistribution;
import de.unileipzig.irpact.experimental.deprecated.input.product.IFixedProduct;
import de.unileipzig.irpact.experimental.deprecated.input.product.IProductGroup;
import de.unileipzig.irpact.experimental.deprecated.input.product.IProductGroupAttribute;
import de.unileipzig.irptools.config.testcase.TableData;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class TConfigConverter {

    public static final String DiraqDistribution = "DiraqDistribution";

    private final Map<String, Object> CACHE = new HashMap<>();

    //=========================
    //static util
    //=========================

    private static String conc(String... names) {
        return de.unileipzig.irptools.util.Util.concate("|", names);
    }

    private static List<String> getCAGs(TableData td) {
        List<String> cags = new ArrayList<>();
        for(int i = 1; i < td.columnCount(); i++) {
            cags.add(td.getHeader()[i]);
        }
        return cags;
    }

    private static int getColumn(TableData td, String cag) {
        for(int i = 1; i < td.columnCount(); i++) {
            if(cag.equals(td.getHeader()[i])) {
                return i;
            }
        }
        return -1;
    }

    private static boolean isDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //=========================
    //util
    //=========================

    @SuppressWarnings("unchecked")
    private <T> T getOrPut(String name, T value) {
        if(CACHE.containsKey(name)) {
            return (T) CACHE.get(name);
        } else {
            CACHE.put(name, value);
            return value;
        }
    }

    private IMassPoint getIMassPoint(double value, double weight) {
        String name = value + "|" + weight;
        return getOrPut(name, new IMassPoint(name, value, weight));
    }

    //=========================
    //stuff
    //=========================

    public void convert(TableData td) {
        List<String> cags = getCAGs(td);
    }

    //==========
    //ProductGroup
    //==========

    public IProductGroup getIProductGroup_PV(TableData td) {
        final String pgName = "PV";
        IProductGroupAttribute[] attributes = getAllIProductGroupAttributes(td, pgName);
        return new IProductGroup(pgName, attributes);
    }

    public IProductGroupAttribute[] getAllIProductGroupAttributes(TableData td, String pgName) {
//        List<IProductGroupAttribute> list = CollectionUtil.arrayListOf(
//                getIProductGroupAttribute(pgName, "D1", td.get(0, "D1", 1)),
//                getIProductGroupAttribute(pgName, "D2", td.get(0, "D2", 1)),
//                getIProductGroupAttribute(pgName, "D3", td.get(0, "D3", 1)),
//                getIProductGroupAttribute(pgName, "D4", td.get(0, "D4", 1)),
//                getIProductGroupAttribute(pgName, "D5", td.get(0, "D5", 1))
//        );
//        list.removeIf(Objects::isNull);
//        return list.toArray(new IProductGroupAttribute[0]);
        return new IProductGroupAttribute[0];
    }

    public IProductGroupAttribute getIProductGroupAttribute(String pgName, String pgaName, String distName) {
        if(distName == null) return null;
        String name = conc(pgName, pgaName, distName);
        IUnivariateDoubleDistribution dist = getIUnivariateDoubleDistribution(distName);
        return getOrPut(name, new IProductGroupAttribute(name, dist));
    }

    //==========
    //CAG
    //==========

    public IConsumerAgentGroup getIConsumerAgentGroup(TableData td, String cagName) {
        int cagColumn = getColumn(td, cagName);
        IConsumerAgentGroup cag = new IConsumerAgentGroup(cagName);
        cag.informationAuthority = 1.0;
        cag.productAwareness = getAwareness(conc(cagName, "D2"), td.get(0, "D2", cagColumn));


        return cag;
    }

    public IConsumerAgentGroupAttribute[] getAllIConsumerAgentGroupAttribute(TableData td, String cagName) {
        int cagColumn = getColumn(td, cagName);
        List<IConsumerAgentGroupAttribute> list = CollectionUtil.arrayListOf(
                getIConsumerAgentGroupAttribute(cagName, "A1", td.get(0, "A1", cagColumn)),
                getIConsumerAgentGroupAttribute(cagName, "A2", td.get(0, "A2", cagColumn)),
                getIConsumerAgentGroupAttribute(cagName, "A3", td.get(0, "A3", cagColumn)),
                getIConsumerAgentGroupAttribute(cagName, "A4", td.get(0, "A4", cagColumn)),
                getIConsumerAgentGroupAttribute(cagName, "A5", td.get(0, "A5", cagColumn)),
                getIConsumerAgentGroupAttribute(cagName, "A6", td.get(0, "A6", cagColumn)),
                getIConsumerAgentGroupAttribute(cagName, "A7", td.get(0, "A7", cagColumn)),
                getIConsumerAgentGroupAttribute(cagName, "A8", td.get(0, "A8", cagColumn)),
                getIConsumerAgentGroupAttribute(cagName, "A9", td.get(0, "A9", cagColumn))
        );
        list.removeIf(Objects::isNull);
        return list.toArray(new IConsumerAgentGroupAttribute[0]);
    }

    public IConsumerAgentGroupAttribute getIConsumerAgentGroupAttribute(String cagName, String cagaName, String distName) {
        String name = conc(cagName, cagaName, distName);
        IUnivariateDoubleDistribution dist = getIUnivariateDoubleDistribution(distName);
        return getOrPut(name, new IConsumerAgentGroupAttribute(name, dist));
    }

    public IAwareness getAwareness(String awaName, String valueStr) {
        String name = conc(awaName, valueStr);
        double value = Double.parseDouble(valueStr);
        return getOrPut(name, new IThresholdAwareness(name, value));
    }

    public IFixedProductAwareness getIFixedProductAwareness(IConsumerAgentGroup cag, IFixedProduct fp, String distName) {
        String name = conc(cag.getName(), fp.getName(), distName);
        IUnivariateDoubleDistribution dist = getIUnivariateDoubleDistribution(distName);
        return new IFixedProductAwareness(name, cag, fp, dist);
    }

    //==========
    //Dist
    //==========

    public IUnivariateDoubleDistribution getIUnivariateDoubleDistribution(String name) {
        if(name.startsWith(DiraqDistribution)) {
            String suffix = name.substring(DiraqDistribution.length());
            if(suffix.equals("Any")) {
                throw new UnsupportedOperationException("TODO");
            }
            try {
                int suffixInt = Integer.parseInt(suffix);
                double value;
                if(suffix.startsWith("0")) {
                    int len = suffix.length();
                    value = (double) suffixInt / (double) len;
                } else {
                    value = suffixInt;
                }
                return getIFiniteMassPointsDiscreteDistribution(name, value);
            } catch (NumberFormatException e) {
                //ignore
            }
        }
        if(isDouble(name)) {
            double value = Double.parseDouble(name);
            return getIConstantUnivariateDistribution(name, value);
        }
        throw new IllegalArgumentException("unsupported: " + name);
    }

    public IFiniteMassPointsDiscreteDistribution getIFiniteMassPointsDiscreteDistribution(String name, double value) {
        IMassPoint massPoint = getIMassPoint(value, 1.0);
        return getOrPut(name, new IFiniteMassPointsDiscreteDistribution(name, Util.USE_RANDOM_SEED, massPoint));
    }

    public IConstantUnivariateDistribution getIConstantUnivariateDistribution(String name, double value) {
        return getOrPut(name, new IConstantUnivariateDistribution(name, value));
    }
}
