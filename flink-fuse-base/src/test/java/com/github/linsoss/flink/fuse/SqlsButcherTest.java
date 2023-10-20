package com.github.linsoss.flink.fuse;

import org.apache.calcite.sql.parser.SqlParseException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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

