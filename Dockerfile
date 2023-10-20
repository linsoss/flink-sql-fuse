FROM scratch

ARG SRC_JAR

COPY $SRC_JAR /flink-artifacts/flink-sql-fuse.jar