package de.unileipzig.irpact.util.curl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.unileipzig.irpact.commons.util.FileUtil;
import de.unileipzig.irpact.commons.util.JsonUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * @author Daniel Abitz
 */
public final class CurlUtil {

    private CurlUtil() {
    }

    public static Optional<JsonNode> executeToJson(
            Curl curl,
            Path tempOutPath,
            Path tempErrPath,
            Charset charset,
            ObjectMapper mapper) throws IOException, InterruptedException, CurlException {
        return executeToJson(curl, tempOutPath, tempErrPath, charset, mapper, true);
    }

    public static Optional<JsonNode> executeToJson(
            Curl curl,
            Path outPath,
            Path errPath,
            Charset charset,
            ObjectMapper mapper,
            boolean deleteFiles) throws IOException, InterruptedException, CurlException {
        curl.execute(outPath, errPath);
        try {
            if(Files.exists(errPath) && Files.size(errPath) > 0L) {
                String content = FileUtil.readString(errPath, charset);
                throw new CurlException(content);
            } else {
                if(Files.notExists(outPath) || Files.size(outPath) == 0L) {
                    return Optional.empty();
                } else {
                    try {
                        JsonNode root = JsonUtil.read(outPath, charset, mapper);
                        return Optional.of(root);
                    } catch (JsonParseException e) {
                        String content = FileUtil.readString(outPath, charset);
                        throw new CurlException(content);
                    }
                }
            }
        } finally {
            if(deleteFiles) {
                FileUtil.deleteIfExists(outPath, errPath);
            }
        }
    }

    public static int execute(
            Curl curl,
            Path outPath,
            Path errPath,
            Charset charset) throws IOException, InterruptedException, CurlException {
        return execute(curl, outPath, errPath, charset, true);
    }

    public static int execute(
            Curl curl,
            Path outPath,
            Path errPath,
            Charset charset,
            boolean deleteFiles) throws IOException, InterruptedException, CurlException {
        int result = curl.execute(outPath, errPath);
        try {
            if(Files.exists(errPath) && Files.size(errPath) > 0L) {
                String content = FileUtil.readString(errPath, charset);
                throw new CurlException(content);
            } else {
                return result;
            }
        } finally {
            if(deleteFiles) {
                FileUtil.deleteIfExists(outPath, errPath);
            }
        }
    }
}
