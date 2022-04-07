package com.github.potamois.flink.fuse;

import org.junit.jupiter.api.Test;

import static com.github.potamois.flink.fuse.ParamUtil.encodeBase64;

public class FlinkSqlFuseTest {
    
    @Test
    public void testExecuteSql() {
        String[] args = new String[]{
                "--sql.1", encodeBase64("create temporary table datagen_source (\n" +
                "    f_sequence int,\n" +
                "    f_random int,\n" +
                "    f_random_str string\n" +
                "  ) with (\n" +
                "    'connector' = 'datagen'\n" +
                "  )"),
                "--sql.2", encodeBase64("explain select * from datagen_source limit 100"),
        };
        new FlinkSqlFuse(args).launch();
    }
}
