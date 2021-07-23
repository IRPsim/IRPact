package de.unileipzig.irpact.util.scenarios.pvact;

import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irpact.util.pvact.Milieu;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public class PvActConsumerAgentGroupBuilder {

    protected Map<String, InPVactConsumerAgentGroup> cags = new LinkedHashMap<>();
    protected Function<? super String, ? extends InPVactConsumerAgentGroup> cagCreator;
    protected InSpatialDistribution spatialDistribution;

    public PvActConsumerAgentGroupBuilder(Function<? super String, ? extends InPVactConsumerAgentGroup> cagCreator, Collection<Milieu> milieus) {
        this.cagCreator = cagCreator;
        for(Milieu milieu: milieus) {
            add(milieu.print());
        }
    }

    public void add(String... names) {
        for(String name: names) {
            if(cags.containsKey(name)) {
                throw new IllegalArgumentException("name '" + name + "' already exists");
            }
            InPVactConsumerAgentGroup cag = cagCreator.apply(name);
            if(spatialDistribution != null) {
                cag.setSpatialDistribution(spatialDistribution);
            }
            cags.put(name, cag);
        }
    }

    public InPVactConsumerAgentGroup get(Milieu milieu) {
        return get(milieu.print());
    }

    public InPVactConsumerAgentGroup get(String name) {
        InPVactConsumerAgentGroup cag = cags.get(name);
        if(cag == null) {
            throw new NoSuchElementException(name);
        }
        return cag;
    }

    public void forEach(Consumer<? super InPVactConsumerAgentGroup> consumer) {
        for(InPVactConsumerAgentGroup cag: cags.values()) {
            consumer.accept(cag);
        }
    }

    public <V> Map<InPVactConsumerAgentGroup, V> map(Map<Milieu, V> in) {
        if(in instanceof LinkedHashMap) {
            return map(in, new LinkedHashMap<>());
        }
        if(in instanceof HashMap) {
            return map(in, new HashMap<>());
        }
        throw new IllegalArgumentException("unsupported map: " + in.getClass());
    }

    public <V> Map<InPVactConsumerAgentGroup, V> map(Map<Milieu, V> in, Map<InPVactConsumerAgentGroup, V> out) {
        for(Map.Entry<Milieu, V> entry: in.entrySet()) {
            InPVactConsumerAgentGroup cag = get(entry.getKey());
            out.put(cag, entry.getValue());
        }
        return out;
    }

    public void applyMilieus(
            Map<Milieu, ? extends InUnivariateDoubleDistribution> dists,
            BiConsumer<? super InPVactConsumerAgentGroup, ? super InUnivariateDoubleDistribution> consumer) {
        apply(map(dists), consumer);
    }

    public void apply(
            Map<InPVactConsumerAgentGroup, ? extends InUnivariateDoubleDistribution> dists,
            BiConsumer<? super InPVactConsumerAgentGroup, ? super InUnivariateDoubleDistribution> consumer) {
        for(Map.Entry<? extends InPVactConsumerAgentGroup, ? extends InUnivariateDoubleDistribution> entry: dists.entrySet()) {
            consumer.accept(entry.getKey(), entry.getValue());
        }
    }

    public InPVactConsumerAgentGroup[] cags() {
        return cags.values().toArray(new InPVactConsumerAgentGroup[0]);
    }

    public InPVactConsumerAgentGroup[] part(Milieu... milieus) {
        InPVactConsumerAgentGroup[] cags = new InPVactConsumerAgentGroup[milieus.length];
        for(int i = 0; i < cags.length; i++) {
            cags[i] = get(milieus[i].print());
        }
        return cags;
    }
}
