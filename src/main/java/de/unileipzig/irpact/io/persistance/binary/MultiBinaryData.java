package de.unileipzig.irpact.io.persistance.binary;

import de.unileipzig.irpact.commons.util.IRPactBase32;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
@Definition
public class MultiBinaryData {

    public String _name;

    @FieldDefinition
    public long uid;

    @FieldDefinition
    public int section;

    @FieldDefinition
    public int part;

    public MultiBinaryData() {
    }

    public static Map<Integer, byte[]> getAllBytes(Collection<? extends MultiBinaryData> coll) {
        Set<Integer> sections = coll.stream()
                .map(mbd -> mbd.section)
                .collect(Collectors.toSet());
        Map<Integer, byte[]> map = new HashMap<>();
        for(Integer section : sections) {
            byte[] content = getBytes(coll, section);
            map.put(section, content);
        }
        return map;
    }

    public static byte[] getBytes(Collection<? extends MultiBinaryData> coll, int section) {
        List<MultiBinaryData> sorted = coll.stream()
                .filter(mbd -> mbd.section == section)
                .sorted(Comparator.comparingInt(o -> o.part))
                .collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        for(MultiBinaryData mbd: sorted) {
            sb.append(mbd._name);
        }
        String fullText = sb.toString();
        return IRPactBase32.decodeString(fullText);
    }
}
