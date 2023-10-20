package com.github.linsoss.flink.fuse;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Properties;


/**
 * Bootstrap for a base Flink sql task.
 * The sql parameter should be included in any of the following java launch arguments:
 * 1) --file: indicates the path of sql file.
 * 2) --sqls: indicates a set of sql split by ";".
 */
public class FlinkSqlFuse {


    public static final String BANNER = "\n" +
            "███████╗██╗     ██╗███╗   ██╗██╗  ██╗    ███████╗ ██████╗ ██╗         ███████╗██╗   ██╗███████╗███████╗\n" +
            "██╔════╝██║     ██║████╗  ██║██║ ██╔╝    ██╔════╝██╔═══██╗██║         ██╔════╝██║   ██║██╔════╝██╔════╝\n" +
            "█████╗  ██║     ██║██╔██╗ ██║█████╔╝     ███████╗██║   ██║██║         █████╗  ██║   ██║███████╗█████╗  \n" +
            "██╔══╝  ██║     ██║██║╚██╗██║██╔═██╗     ╚════██║██║▄▄ ██║██║         ██╔══╝  ██║   ██║╚════██║██╔══╝  \n" +
            "██║     ███████╗██║██║ ╚████║██║  ██╗    ███████║╚██████╔╝███████╗    ██║     ╚██████╔╝███████║███████╗\n" +
            "                                                                       \n" +
            "flink-sql-fuse: v0.4                                                    \n" +
            "github: https://github.com/linsoss/flink-sql-fuse                         ";


    private final static Logger LOG = LoggerFactory.getLogger(FlinkSqlFuse.class);

    private final List<String> sqlPlan;

    public FlinkSqlFuse(String[] args) throws IOException {
        LOG.info(BANNER);
        Properties props = ParameterTool.fromArgs(args).getProperties();
        List<String> sqlPlan = ParamResolver.extractSqls(props);
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