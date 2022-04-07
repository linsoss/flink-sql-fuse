package com.github.potamois.flink.fuse;

import org.apache.flink.api.java.utils.ParameterTool;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.github.potamois.flink.fuse.ParamUtil.encodeBase64;
import static com.github.potamois.flink.fuse.ParamUtil.decodeBase64;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParamUtilTest {
    
    @Test
    public void testBase64DecodeEncode() {
        String str1 = "CREATE TABLE datagen (f1 INT,f2 INT,f4 STRING) WITH ('connector' = 'datagen')";
        String encoded = encodeBase64(str1);
        String str2 = decodeBase64(encoded);
        assertEquals(str1, str2);
    }
    
    @Test
    public void testExtractSqls1() {
        String[] args = new String[]{
                "--sql.1", encodeBase64("1"),
                "--sql.2", encodeBase64("2"),
                "--sql.3", encodeBase64("3"),
                "--sql.4", encodeBase64("4")
        };
        List<String> expected = new ArrayList<String>() {{
            add("1");
            add("2");
            add("3");
            add("4");
        }};
        Properties props = ParameterTool.fromArgs(args).getProperties();
        List<String> re = ParamUtil.extractSqls(props);
        assertEquals(re, expected);
    }
    
    @Test
    public void testExtractSqls2() {
        String[] args = new String[]{
                "--sql.1", encodeBase64("1"),
                "--sql.4", encodeBase64("4"),
                "--sql.3", encodeBase64("3"),
                "--sql.2", encodeBase64("2")
        };
        List<String> expected = new ArrayList<String>() {{
            add("1");
            add("2");
            add("3");
            add("4");
        }};
        Properties props = ParameterTool.fromArgs(args).getProperties();
        List<String> re = ParamUtil.extractSqls(props);
        assertEquals(re, expected);
    }
    
    @Test
    public void testExtractSqls3() {
        String[] args = new String[]{
                "--sql.1", encodeBase64("1"),
                "--sql.4", encodeBase64("4"),
                "--sql-7", encodeBase64("7"),
                "--sql.3", encodeBase64("3"),
                "--whatTheHell", encodeBase64("8"),
                "--sql.2", encodeBase64("2")
        };
        List<String> expected = new ArrayList<String>() {{
            add("1");
            add("2");
            add("3");
            add("4");
        }};
        Properties props = ParameterTool.fromArgs(args).getProperties();
        List<String> re = ParamUtil.extractSqls(props);
        assertEquals(re, expected);
    }
    
    
    @Test
    public void testExtractSqls4() {
        String[] args = new String[]{
                "--sqls", encodeBase64("1;2;3;4"),
        };
        List<String> expected = new ArrayList<String>() {{
            add("1");
            add("2");
            add("3");
            add("4");
        }};
        Properties props = ParameterTool.fromArgs(args).getProperties();
        List<String> re = ParamUtil.extractSqls(props);
        assertEquals(re, expected);
    }
    
    @Test
    public void testExtractSqls5() {
        String[] args = new String[]{
                "--sqls", encodeBase64("1;2;3;4"),
                "--sql.1", encodeBase64("5"),
                "--sql.4", encodeBase64("6"),
                "--sql.3", encodeBase64("7")
        };
        List<String> expected = new ArrayList<String>() {{
            add("5");
            add("7");
            add("6");
        }};
        Properties props = ParameterTool.fromArgs(args).getProperties();
        List<String> re = ParamUtil.extractSqls(props);
        assertEquals(re, expected);
    }
    
    @Test
    public void testExtractSqls6() {
        String[] args = new String[]{
                "--sql.5", encodeBase64("2"),
                "--sql.8", encodeBase64("3"),
                "--sql.10", encodeBase64("4"),
                "--sql.1", encodeBase64("1"),
        };
        List<String> expected = new ArrayList<String>() {{
            add("1");
            add("2");
            add("3");
            add("4");
        }};
        Properties props = ParameterTool.fromArgs(args).getProperties();
        List<String> re = ParamUtil.extractSqls(props);
        assertEquals(re, expected);
    }
    
    @Test
    public void testExtractSqls7() {
        String[] args = new String[]{
                "--sql.1", encodeBase64("1"),
                "--sql.2", encodeBase64("2"),
                "--sql.a", encodeBase64("3"),
                "--sql.4", encodeBase64("4")
        };
        Properties props = ParameterTool.fromArgs(args).getProperties();
        assertThrows(IllegalArgumentException.class, () -> ParamUtil.extractSqls(props));
    }
    
}
