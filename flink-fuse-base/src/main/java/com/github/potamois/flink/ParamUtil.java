package com.github.potamois.flink;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
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
        String sqls = props.getProperty(SQLS_KEY);
        if (sqls != null && sqls.length() > 0) {
            return Arrays.stream(sqls.split(";")).filter(e -> !e.isEmpty()).collect(Collectors.toList());
        }
        
        List<String> sqlKeys = props.keySet().stream()
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
                .collect(Collectors.toList());
        
        List<String> result = new ArrayList<>(30);
        for (String sqlKey : sqlKeys) {
            result.add(props.getProperty(sqlKey));
        }
        return result;
    }
    
    
}
