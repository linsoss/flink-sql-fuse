package com.github.potamois.flink;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;

@Ignore
public class FlinkSqlFuseTest {
    
    @Test
    public void testExecuteSql() {
        String[] args = new String[]{
                "--sql.1", "create temporary table datagen_source (\n" +
                "    f_sequence int,\n" +
                "    f_random int,\n" +
                "    f_random_str string\n" +
                "  ) with (\n" +
                "    'connector' = 'datagen'\n" +
                "  )",
                "--sql.2", "select * from datagen_source limit 100",
        };
        new FlinkSqlFuse(args).launch();
    }
}
