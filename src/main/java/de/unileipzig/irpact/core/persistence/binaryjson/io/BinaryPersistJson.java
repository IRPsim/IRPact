package de.unileipzig.irpact.core.persistence.binaryjson.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.unileipzig.irpact.commons.util.irpact32.IRPactBase32;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.io.param.inout.persist.binary.BinaryPersistData;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public final class BinaryPersistJson {

    public static final int DEFAULT_PART_LENGTH = 76;

    private BinaryPersistJson() {
    }

    public static Comparator<String> buildComparator(String uidx) {
        return (o1, o2) -> {
            long uid1 = getId(uidx, o1);
            long uid2 = getId(uidx, o2);
            return Long.compare(uid1, uid2);
        };
    }

    public static void sortUids(List<String> list, String uidx) {
        list.sort(buildComparator(uidx));
    }

    public static String buildId(String x, long id) {
        String hex = Long.toHexString(id);
        return x + hex + x;
    }

    public static long getId(String x, String irp32) {
        int dataStart = findDataStart(x, irp32);
        if(dataStart == -1) {
            throw new IllegalArgumentException();
        }
        String hex = irp32.substring(x.length(), dataStart - x.length());
        return Long.parseLong(hex, 16);
    }

    public static String getData(String uidx, String irp32withId) {
        int dataStart = findDataStart(uidx, irp32withId);
        return irp32withId.substring(dataStart);
    }

    public static int findDataStart(String x, String irp32) {
        int index = irp32.indexOf(x, 1);
        if(index == -1) {
            return -1;
        }
        return index + x.length();
    }

    public static BinaryPersistData toData(JsonNode node, String uidPrefix, long uid) {
        BinaryPersistData data = new BinaryPersistData();
        data.setIRPBase32String(print(node, uidPrefix, uid));
        data.setID(uid);
        return data;
    }

    public static String print(JsonNode node, String prefix) {
        return print(JsonUtil.SMILE, node, prefix);
    }

    public static String print(ObjectMapper mapper, JsonNode node, String prefix) {
        return print(new StringBuilder(), mapper, node, prefix);
    }

    //<prefix><base32>
    public static String print(StringBuilder builder, ObjectMapper mapper, JsonNode node, String prefix) {
        try {
            byte[] nodeBytes = JsonUtil.toBytes(mapper, node);
            String irp32 = IRPactBase32.encodeToString(nodeBytes);
            builder.setLength(0);
            builder.append(prefix);
            builder.append(irp32);
            return builder.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String print(JsonNode node, String uidx, long uid) {
        return print(JsonUtil.SMILE, node, uidx, uid);
    }

    public static String print(ObjectMapper mapper, JsonNode node, String uidx, long uid) {
        return print(new StringBuilder(), mapper, node, uidx, uid);
    }

    //<uidx><uid><uidx><base32>
    public static String print(StringBuilder builder, ObjectMapper mapper, JsonNode node, String uidx, long uid) {
        try {
            byte[] nodeBytes = JsonUtil.toBytes(mapper, node);
            String irp32 = IRPactBase32.encodeToString(nodeBytes);
            builder.setLength(0);
            builder.append(uidx);
            builder.append(uid);
            builder.append(uidx);
            builder.append(irp32);
            return builder.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static JsonNode parse(String irp32withId, String uidx) {
        return parse(JsonUtil.SMILE, irp32withId, uidx);
    }

    public static JsonNode parse(ObjectMapper mapper, String irp32withId, String uidx) {
        try {
            String irp32 = getData(uidx, irp32withId);
            byte[] decodedBytes = IRPactBase32.decode(irp32);
            return JsonUtil.fromBytes(mapper, decodedBytes);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void toLines(
            ObjectMapper mapper,
            JsonNode node,
            String uidx,
            long firstUid,
            int partLength,
            Collection<? super String> out) {
        try {
            byte[] nodeBytes = JsonUtil.toBytes(mapper, node);
            String irp32 = IRPactBase32.encodeToString(nodeBytes);
            int i = 0;
            int len;
            long uid = firstUid;
            while(i < irp32.length()) {
                len = Math.min(partLength, irp32.length() - i);
                String part = buildId(uidx, uid) + irp32.substring(i, i + len);
                out.add(part);

                uid++;
                i += len;
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static JsonNode fromLines(
            ObjectMapper mapper,
            Collection<? extends String> sortedIds,
            String uidx) {
        return fromLines(new StringBuilder(), mapper, sortedIds, uidx);
    }

    public static JsonNode fromLines(
            StringBuilder builder,
            ObjectMapper mapper,
            Collection<? extends String> sortedIds,
            String uidx) {

        builder.setLength(0);
        for(String part: sortedIds) {
            String data = getData(uidx, part);
            builder.append(data);
        }

        String irp32 = builder.toString();
        byte[] decoded = IRPactBase32.decode(irp32);

        try {
            return JsonUtil.fromBytes(mapper, decoded);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
