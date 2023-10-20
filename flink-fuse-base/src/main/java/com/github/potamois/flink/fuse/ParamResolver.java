package com.github.potamois.flink.fuse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ParamResolver {

    // Sql script file path
    public static String SQL_FILE_KEY = "file";

    // Sql script content
    public static String SQLS_KEY = "sqls";

    /**
     * Extract sql value from properties, The priority of the parameter sql key:
     * "--file" -> "--sqls"
     */
    public static List<String> extractSqls(Properties props) throws IOException {
        var sqls = new ArrayList<String>();
        sqls.addAll(fromSqlsKey(props));
        sqls.addAll(fromSqlFileKey(props));
        return sqls;
    }


    private static List<String> fromSqlFileKey(Properties props) throws IOException {
        if (!props.containsKey(SQL_FILE_KEY)) {
            return new ArrayList<>();
        }
        String path = props.getProperty(SQL_FILE_KEY);
        String content = Files.readString(Paths.get(path), StandardCharsets.UTF_8);
        return SqlsButcher.splitSqls(content);
    }

    private static List<String> fromSqlsKey(Properties props) {
        if (!props.containsKey(SQLS_KEY)) {
            return new ArrayList<>();
        }
        String sqls = props.getProperty(SQLS_KEY);
        return SqlsButcher.splitSqls(sqls);
    }

}
