package com.github.potamois.flink;

import org.apache.flink.api.java.utils.ParameterTool;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParamUtilTest {
    
    @Test
    public void testExtractSqls1() {
        String[] args = new String[]{
                "--sql.1", "1",
                "--sql.2", "2",
                "--sql.3", "3",
                "--sql.4", "4"
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
                "--sql.1", "1",
                "--sql.4", "4",
                "--sql.3", "3",
                "--sql.2", "2"
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
                "--sql.1", "1",
                "--sql.4", "4",
                "--sql-7", "7",
                "--sql.3", "3",
                "--whatTheHell", "8",
                "--sql.2", "2"
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
                "--sqls", "1;2;3;4",
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
                "--sqls", "1;2;3;4",
                "--sql.1", "5",
                "--sql.4", "6",
                "--sql.3", "7"
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
    public void testExtractSqls6() {
        String[] args = new String[]{
                "--sql.5", "2",
                "--sql.8", "3",
                "--sql.10", "4",
                "--sql.1", "1",
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
                "--sql.1", "1",
                "--sql.2", "2",
                "--sql.a", "3",
                "--sql.4", "4"
        };
        Properties props = ParameterTool.fromArgs(args).getProperties();
        assertThrows(IllegalArgumentException.class, () -> ParamUtil.extractSqls(props));
    }
    
}
