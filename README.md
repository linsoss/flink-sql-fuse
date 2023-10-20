# Flink SQL Fuse

Flink SQL Fuse is a tiny shims for submitting Flink SQL job directly to cluster by specifying the sqls content with Java
program arguments.

The supported Flink versions are as following:

- 1.17.x
- 1.16.x
- 1.15.x

## Get Started

- Download the `flink-sql-fuse` JAR for the specified Flink version from
  the [Release](https://github.com/linsoss/flink-sql-fuse/releases) page:

    ```shell
    wget https://github.com/linsoss/flink-sql-fuse/releases/download/v0.4/flink-fuse-17-0.4.jar
    ```

- Submit it to your local Flink cluster using the Flink CLI:

    ```shell
    # via flink cli tools
    ./bin/flink run flink-fuse-17-0.4.jar --sqls "create table datagen_source (  
        f_sequence int,  
        f_random int,  
        f_random_str string  
        ) with (  
        'connector' = 'datagen'  
        );  
    create table print_table with ('connector' = 'print') like datagen_source (excluding all);  
    insert into print_table select * from datagen_source; "
    ```

## Program parameters

- `--sqls`  indicates a set of sql split by ";"
- `--file` indicates the path of sql file;

## Submit to Flink Cluster on Kubernetes

### Session Mode

If you need to quickly submit a Flink SQL job to a Flink cluster deployed on Kubernetes in session mode, you can define
your SQL content using the `--sqls` parameter of `flink-sql-fuse.jar`:

```shell
# via flink cli tools
./bin/flink run \
    --target kubernetes-session \
    -Dkubernetes.cluster-id=${flink_k8s_cluster_id} \
    -Dkubernetes.namespace=${flink_k8s_namespce} \
    flink-fuse-17-0.4.jar --sqls "create table datagen_source (  
    f_sequence int,  
    f_random int,  
    f_random_str string  
    ) with (  
    'connector' = 'datagen'  
    );  
create table print_table with ('connector' = 'print') like datagen_source (excluding all);  
insert into print_table select * from datagen_source; "
```

### Application Mode

	In fact, Flink SQL Fuse was designed from the outset for Flink Kubernetes Application.


	- Create a ConfigMap with SQL content, and save it as `flink-sql-configmap.yaml`

    ```yaml
    apiVersion: v1
    kind: ConfigMap
    metadata:
      name: my-flink-sql
    data:
      run.sql: |
        create table datagen_source (  
          f_sequence int,  
          f_random int,  
          f_random_str string  
          ) with (  
          'connector' = 'datagen'  
        );  
        create table print_table with ('connector' = 'print') like datagen_source (excluding all);  
        insert into print_table select * from datagen_source;
    ```

- Create a Flink Pod Template definition and save it as `flink-podtemplate.yaml`

    ```yaml
    apiVersion: v1
    kind: Pod
    metadata:
      name: pod-template
    spec:
      initContainers:
        - name: sql-fuse-fetcher
          image: ghcr.io/linsoss/flink-sql-fuse:flink_1.17
          volumeMounts:
            - mountPath: /flink-artifact
              name: flink-artifact
              subPath: flink-sql-fuse.jar
      containers:
        - name: flink-main-container
          volumeMounts:
            - mountPath: /opt/flink/artifacts
              name: flink-artifact
            - mountPath: /opt/sql
              name: sql-script
      volumes:
        - name: flink-artifact
          emptyDir: { }
        - name: sql-script
          configMap:
            name: my-flink-sql
    ```

- Submit the Flink application job:

    ```shell
    kubectl apply -f flink-sql-configmap.yaml 
    
    # vis flink cli tools
    ./bin/flink run-application \  
        --target kubernetes-application \  
        -Dkubernetes.cluster-id=flink-app \  
        -Dkubernetes.pod-template-file=flink-pod-template.yml \  
        -Dkubernetes.container.image=flink:1.17 \  
        local:///opt/flink/artifacts/flink-fuse.jar --file opt/sql/run.sql
    ```

## Build Project

- Build Java Project

    ```other
    make build
    ```

- Build Docker image

    ```shell
    make build-image
    ```

