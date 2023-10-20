package com.github.linsoss.flink.fuse;

import org.apache.flink.api.java.utils.ParameterTool;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParamResolverTest {


    @Test
    public void testExtractSqls1() throws IOException {
        var args = new String[]{"--sqls", "CREATE TABLE datagen (f1 INT,f2 INT,f4 STRING) WITH ('connector' = 'datagen'); select * from datagen"};
        var expected = new ArrayList<>() {{
            add("CREATE TABLE datagen (f1 INT,f2 INT,f4 STRING) WITH ('connector' = 'datagen')");
            add("select * from datagen");
        }};
        var props = ParameterTool.fromArgs(args).getProperties();
        var res = ParamResolver.extractSqls(props);
        assertEquals(res, expected);
    }

    @Test
    public void testExtractSqls2() throws IOException {
        var classLoader = getClass().getClassLoader();
        var filepath = classLoader.getResource("test.sql").getFile();

        var args = new String[]{"--file", filepath};
        var props = ParameterTool.fromArgs(args).getProperties();
        var res = ParamResolver.extractSqls(props);

        var expected = new ArrayList<>() {{
            add("create catalog myhive with (\n" +
                    "    'type' = 'hive',\n" +
                    "    'hive-conf-dir' = '/opt/hive-conf'\n" +
                    ")");
            add("create temporary table heros (\n" +
                    "    hname string,\n" +
                    "    hpower string,\n" +
                    "    hage int\n" +
                    ") with (\n" +
                    "  'connector' = 'faker',\n" +
                    "    'rows-per-second' = '100',\n" +
                    "    'fields.hname.expression' = '#{superhero.name}',\n" +
                    "    'fields.hpower.expression' = '#{superhero.power}',\n" +
                    "    'fields.hpower.null-rate' = '0.05',\n" +
                    "    'fields.hage.expression' = '#{number.numberbetween ''0'',''1000''}'\n" +
                    ")");
            add("insert into myhive.test.heros select * from heros");
        }};

        assertEquals(expected, res);
    }


}
