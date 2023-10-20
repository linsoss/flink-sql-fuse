package com.github.linsoss.flink.fuse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SqlsButcher {

    public static List<String> splitSqls(String sqls) {
        List<String> sqlList = new ArrayList<>();
        boolean insideDoubleQuotes = false;
        boolean insideSingleQuotes = false;
        StringBuilder currentSql = new StringBuilder();

        for (char c : sqls.toCharArray()) {
            if (c == '"') {
                insideDoubleQuotes = !insideDoubleQuotes;
            } else if (c == '\'') {
                insideSingleQuotes = !insideSingleQuotes;
            }

            if (c == ';' && !insideDoubleQuotes && !insideSingleQuotes) {
                sqlList.add(currentSql.toString());
                currentSql = new StringBuilder();
            } else {
                currentSql.append(c);
            }
        }

        if (currentSql.length() > 0) {
            sqlList.add(currentSql.toString());
        }
        return sqlList.stream().map(String::trim).filter(e -> !e.isBlank()).collect(Collectors.toList());
    }


}
