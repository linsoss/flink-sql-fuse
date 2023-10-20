package com.github.linsoss.flink.fuse;

import org.apache.calcite.sql.parser.SqlParseException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqlsButcherTest {

    @Test
    public void testSplitSqls1() {
        var sqls = "CREATE TABLE datagen (f1 INT,f2 INT,f4 STRING) WITH ('connector' = 'datagen');\n" +
                "select * from datagen; " +
                "insert into hole select * from datagen";
        var expected = new ArrayList<>() {{
            add("CREATE TABLE datagen (f1 INT,f2 INT,f4 STRING) WITH ('connector' = 'datagen')");
            add("select * from datagen");
            add("insert into hole select * from datagen");
        }};
        var results = SqlsButcher.splitSqls(sqls);
        assertEquals(expected, results);
    }

    @Test
    public void testSplitSqls2()  {
        var sqls = "CREATE TABLE datagen (f1 INT,f2 INT,f4 STRING) WITH ('connector' = 'datagen');\n" +
                "select replace(';', '_') from datagen; " +
                "insert into hole select replace(\";\", \"_\")  from datagen";
        var expected = new ArrayList<>() {{
            add("CREATE TABLE datagen (f1 INT,f2 INT,f4 STRING) WITH ('connector' = 'datagen')");
            add("select replace(';', '_') from datagen");
            add("insert into hole select replace(\";\", \"_\")  from datagen");
        }};
        var results = SqlsButcher.splitSqls(sqls);
        assertEquals(expected, results);
    }


}

