package com.github.potamois.flink.fuse;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;


/**
 * Bootstrap for base Flink sql task.
 *
 * The sql parameter should be included in any of the following java launch arguments:
 * 1) --sqls : indicates a set of sql split by ";"
 * 2) -sql.x: indicates a separate sql, the "x" indicates the number sequence of sql execution,
 *            e.g. "-sql.1", "-sql.2".
 *
 * When the sql.x configuration exists, the sqls configured by "--sqls" will be ignored.
 *
 * @author Al-assad
 */
public class FlinkSqlFuse {
    
    public static final String BANNER = "\n" +
            " _____ _____ _____ _____ _____ _____ _____    _____ _____ _____ _____  \n" +
            "|  _  |     |_   _|  _  |     |     |     |  |   __|  |  |   __|   __| \n" +
            "|   __|  |  | | | |     | | | |  |  |-   -|  |   __|  |  |__   |   __| \n" +
            "|__|  |_____| |_| |__|__|_|_|_|_____|_____|  |__|  |_____|_____|_____| \n" +
            "                                                                       \n" +
            "potamoi-flink-fuse: v0.2                                               \n" +
            "github: https://github.com/potamois                                    ";
    
    
    private final static Logger LOG = LoggerFactory.getLogger(FlinkSqlFuse.class);
    
    private final List<String> sqlPlan;
    
    public FlinkSqlFuse(String[] args) {
        LOG.info(BANNER);
        Properties props = ParameterTool.fromArgs(args).getProperties();
        List<String> sqlPlan = ParamUtil.extractSqls(props);
        this.sqlPlan = sqlPlan;
        LOG.info("sql plan: \n" + String.join(";\n", sqlPlan));
    }
    
    public void launch() {
        final TableEnvironment env = TableEnvironment.create(EnvironmentSettings.newInstance().build());
        LOG.info("Start execution of sqls plan...");
        for (String sql : sqlPlan) {
            env.executeSql(sql);
        }
    }
    
}
