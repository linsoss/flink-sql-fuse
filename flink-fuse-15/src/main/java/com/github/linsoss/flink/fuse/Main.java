package com.github.linsoss.flink.fuse;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new FlinkSqlFuse(args).launch();
    }

}
