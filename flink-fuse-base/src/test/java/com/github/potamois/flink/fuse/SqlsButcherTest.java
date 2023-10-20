package com.github.potamois.flink.fuse;

import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.flink.sql.parser.impl.FlinkSqlParserImpl;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.SqlDialect;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.internal.TableEnvironmentImpl;
import org.apache.flink.table.operations.Operation;
import org.apache.flink.table.planner.parse.CalciteParser;
import org.apache.flink.table.planner.parse.ExtendedParser;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqlsButcherTest {

    @Test
    public void testSplitSqls() throws SqlParseException {
        var sqls = "CREATE TABLE datagen (f1 INT,f2 INT,f4 STRING) WITH ('connector' = 'datagen');\n" +
                "select * from datagen; " +
                "insert into hole select * from datagen";
        var expected = new ArrayList<>() {{
            add("CREATE TABLE `datagen` (\n" +
                    "  `f1` INTEGER,\n" +
                    "  `f2` INTEGER,\n" +
                    "  `f4` STRING\n" +
                    ") WITH (\n" +
                    "  'connector' = 'datagen'\n" +
                    ")");
            add("SELECT *\n" +
                    "FROM `datagen`");
            add("INSERT INTO `hole`\n" +
                    "(SELECT *\n" +
                    "FROM `datagen`)");
        }};
        var results = SqlsButcher.astSplit(sqls);
        assertEquals(expected, results);
    }

}

