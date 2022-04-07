package com.github.potamois.flink.fuse;

import org.apache.commons.lang3.tuple.Pair;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Parameter parsing util.
 *
 * @author Al-assad
 */
public class ParamUtil {
    
    public static String SQLS_KEY = "sqls";
    public static String SINGLE_SQL_KEY_PREFIX = "sql.";
    
    /**
     * Extract sql value from props.
     * if SQLS_KEY exists, then ignore all other SINGLE_SQL_KEY_PREFIX matches.
     */
    public static List<String> extractSqls(Properties props) throws IllegalArgumentException {
        
        List<String> lines = props.keySet().stream()
                .filter(key -> ((String) key).startsWith(SINGLE_SQL_KEY_PREFIX))
                .map(key -> {
                    try {
                        String keyStr = ((String) key);
                        int idx = Integer.parseInt(keyStr.substring(SINGLE_SQL_KEY_PREFIX.length()));
                        return Pair.of(keyStr, idx);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid key: " + key, e);
                    }
                })
                .sorted(Comparator.comparingInt(Pair::getRight))
                .map(Pair::getLeft)
                .map(props::getProperty)
                .map(ParamUtil::decodeBase64)
                .filter(e -> e != null && !e.isEmpty())
                .collect(Collectors.toList());
        if (lines != null && lines.size() > 0) {
            return lines;
        }
        
        String sqls = props.getProperty(SQLS_KEY);
        if (sqls == null || sqls.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(decodeBase64(sqls).split(";"))
                .filter(e -> e != null && !e.isEmpty())
                .collect(Collectors.toList());
    }
    
    
    public static String encodeBase64(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes());
    }
    
    public static String decodeBase64(String base64Str) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64Str);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
    
}
