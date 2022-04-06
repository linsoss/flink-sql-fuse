package com.github.potamois.flink;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.flink.api.java.utils.ParameterTool;

/**
 * Bootstrap for base Flink sql task.
 *
 * @author Al-assad
 */
public class SqlFuse {
    
    private final static transient Logger LOGGER = LoggerFactory.getLogger(SqlFuse.class);
    
    public static void main(String[] args) {
        ParameterTool params = ParameterTool.fromArgs(args);
        
        String executeSqls = params.get("sql");
        System.out.println(Constant.BANNER);
    
        
    }
}
