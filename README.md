# Potamoi Fuse


Potamoi Fuse is a tiny shims for sumitting Flink-SQL job directly to cluster by specifying the sqls content with Java program arguments.

The supported flink versions are as following:

* Flink-1.14.x
* Flink-1.13.x
* Flink-1.12.x
* Flink-1.11.x

## Usage

The precompiled jars for `potamoi-fuse` can be found in the [release page](https://github.com/potamois/flink-fuse/releases), for example:

```shell
wget https://github.com/potamois/flink-fuse/releases/v0.2/flink-fuse-14-0.2.jar
```

### Flink-session mode

If you need to submit a flink job to the cluster with the following sql content,  you can quickly submit it via `potamoi-fuse.jar` by specifying the `-sqls`  program argument, which is the sql text in base64 format.

```sql
-- flink sql content
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

```shell
# submit flink-sql job
./bin/flink run .flink-fuse-14-0.2.jar --sqls Y3JlYXRlIHRhYmxlIGRhdGFnZW5fc291cmNlICgKICAgIGZfc2VxdWVuY2UgaW50LAogICAgZl9yYW5kb20gaW50LAogICAgZl9yYW5kb21fc3RyIHN0cmluZwogICAgKSB3aXRoICgKICAgICdjb25uZWN0b3InID0gJ2RhdGFnZW4nCiAgICApOwpjcmVhdGUgdGFibGUgcHJpbnRfdGFibGUgd2l0aCAoJ2Nvbm5lY3RvcicgPSAncHJpbnQnKSBsaWtlIGRhdGFnZW5fc291cmNlIChleGNsdWRpbmcgYWxsKTsKaW5zZXJ0IGludG8gcHJpbnRfdGFibGUgc2VsZWN0ICogZnJvbSBkYXRhZ2VuX3NvdXJjZTs=
```

In addition, there is another prefix parameter `--sql.x` to specify a single sql text,  the `x` represents the execution sequence  number of the sql, which is used in some scenarios where the `";"` contained in the sql may conflict with the default split symbol. 

```sql
-- flink sql content
-- sql.1
create table datagen_source (
    f_sequence int,
    f_random int,
    f_random_str string
    ) with (
    'connector' = 'datagen'
    )
-- sql.2
create table print_table with ('connector' = 'print') like datagen_source (excluding all)
-- sql.3
insert into print_table select * from datagen_source
```

```shell
./bin/flink run .flink-fuse-14-0.2.jar  \
  --sql.1 Y3JlYXRlIHRhYmxlIGRhdGFnZW5fc291cmNlICgKICAgIGZfc2VxdWVuY2UgaW50LAogICAgZl9yYW5kb20gaW50LAogICAgZl9yYW5kb21fc3RyIHN0cmluZwogICAgKSB3aXRoICgKICAgICdjb25uZWN0b3InID0gJ2RhdGFnZW4nCiAgICAp \
  --sql.2 Y3JlYXRlIHRhYmxlIHByaW50X3RhYmxlIHdpdGggKCdjb25uZWN0b3InID0gJ3ByaW50JykgbGlrZSBkYXRhZ2VuX3NvdXJjZSAoZXhjbHVkaW5nIGFsbCk= \
  --sql.3 
  aW5zZXJ0IGludG8gcHJpbnRfdGFibGUgc2VsZWN0ICogZnJvbSBkYXRhZ2VuX3NvdXJjZQ== 
```

The main reason for using base64 as the sql  format is that sql text contains command-line special characters such as `"` and `'`which may cause some unexpected parameters parsing error.

### Flink-application mode

In fact, flink-fuse works very well with flink-application mode on kubernetes.

```shell
# create flink-pod-template file
touch pod-template.yml
cat>pod-template.yml<<EOF
apiVersion: v1
kind: Pod
metadata:
  name: pod-template
spec:
  initContainers:
    - name: artifacts-fetcher
      image: cirrusci/wget:latest
      command: [ 'wget', 'https://github.com/potamois/flink-fuse/releases/v0.2/flink-fuse-14-0.2.jar', '-O', '/flink-artifact/flink-fuse.jar' ]
      volumeMounts:
        - mountPath: /flink-artifact
          name: flink-artifact
  containers:
    - name: flink-main-container
      volumeMounts:
        - mountPath: /opt/flink/volumes/hostpath
          name: flink-volume-hostpath
        - mountPath: /opt/flink/artifacts
          name: flink-artifact
        - mountPath: /opt/flink/log
          name: flink-logs
  volumes:
    - name: flink-volume-hostpath
      hostPath:
        path: /tmp
        type: Directory
    - name: flink-artifact
      emptyDir: { }
    - name: flink-logs
      emptyDir: { }
EOF
      
# launch a flink job on application-mode
./bin/flink run-application \
    --target kubernetes-application \
    -Dkubernetes.cluster-id=flink-app \
    -Dkubernetes.pod-template-file=pod-template.yml \
    -Dkubernetes.container.image=flink:1.14.4 \
    local:///opt/flink/artifacts/flink-fuse.jar \
    --sqls Y3JlYXRlIHRhYmxlIGRhdGFnZW5fc291cmNlICgKICAgIGZfc2VxdWVuY2UgaW50LAogICAgZl9yYW5kb20gaW50LAogICAgZl9yYW5kb21fc3RyIHN0cmluZwogICAgKSB3aXRoICgKICAgICdjb25uZWN0b3InID0gJ2RhdGFnZW4nCiAgICApOwpjcmVhdGUgdGFibGUgcHJpbnRfdGFibGUgd2l0aCAoJ2Nvbm5lY3RvcicgPSAncHJpbnQnKSBsaWtlIGRhdGFnZW5fc291cmNlIChleGNsdWRpbmcgYWxsKTsKaW5zZXJ0IGludG8gcHJpbnRfdGFibGUgc2VsZWN0ICogZnJvbSBkYXRhZ2VuX3NvdXJjZTs=
```

## Build Project

```shell
mvn clean && install
```

<br>

<br>





