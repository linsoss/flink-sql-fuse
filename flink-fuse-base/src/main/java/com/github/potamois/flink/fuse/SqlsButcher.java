package com.github.potamois.flink.fuse;

import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.SqlDialect;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.flink.sql.parser.impl.FlinkSqlParserImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SqlsButcher {

    private final static Logger LOG = LoggerFactory.getLogger(SqlsButcher.class);

    public static List<String> splitSqls(String sqls) {
        if (sqls == null) {
            return new ArrayList<>();
        }
        try {
            return astSplit(sqls);
        } catch (SqlParseException e) {
            LOG.info("Fail to split flink sqls by AST, fallback to simple split.", e);
            return simpleSplit(sqls);
        }
    }


    // Currently only Flink SQL dialect is supported, and support for Hive dialect will be provided in the future.
    protected static List<String> astSplit(String sqls) throws SqlParseException {
        var config = SqlParser.config()
                .withParserFactory(FlinkSqlParserImpl.FACTORY)
                .withIdentifierMaxLength(256)
                .withQuoting(Quoting.BACK_TICK)
                .withQuotedCasing(Casing.UNCHANGED)
                .withUnquotedCasing(Casing.UNCHANGED);

        SqlParser parser = SqlParser.create(sqls, config);
        SqlNodeList nodes = parser.parseStmtList();

        return nodes.stream()
                .map(SqlNode::toString)
                .collect(Collectors.toList());
    }

    protected static List<String> simpleSplit(String sqls) {
        return Arrays.stream(sqls.split(";"))
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(e -> !e.isEmpty())
                .collect(Collectors.toList());
    }

    public static class FlinkSqlDialect extends SqlDialect {

        public FlinkSqlDialect(Context context) {
            super(context);
        }
    }


}
