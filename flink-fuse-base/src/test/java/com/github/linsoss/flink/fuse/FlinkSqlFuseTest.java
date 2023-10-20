package com.github.linsoss.flink.fuse;

import org.junit.jupiter.api.Test;

import java.io.IOException;


public class FlinkSqlFuseTest {

    @Test
    public void testExecuteSql() throws IOException {
        var sqls = "create temporary table datagen_source (f_sequence int,f_random int,f_random_str string) with ('connector' = 'datagen');" +
                "select * from datagen_source limit 100";
        String[] args = new String[]{"--sqls", sqls};
        new FlinkSqlFuse(args).launch();
    }

    @Test
    public void testExecuteSql2() throws IOException {
        var classLoader = getClass().getClassLoader();
        var filepath = classLoader.getResource("test2.sql").getFile();
        String[] args = new String[]{"--file", filepath};
        new FlinkSqlFuse(args).launch();
    }

}
