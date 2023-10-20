
# Build project
.PHONY: build
build:
	mvn clean install


REGISTER ?= ghcr.io/linsoss/
VERSION ?= 0.4

# Build and publish docker image
.PHONY: release-image
release-image: build build-image publish-image

# Build docker image
.PHONY: build-image
build-image:
	docker build --build-arg SRC_JAR=flink-fuse-17/target/flink-fuse-17-${VERSION}.jar \
		-t $(REGISTER)flink-sql-fuse:${VERSION}-flink_1.17 \
		-t $(REGISTER)flink-sql-fuse:flink_1.17 .

	docker build --build-arg SRC_JAR=flink-fuse-16/target/flink-fuse-16-${VERSION}.jar \
		-t $(REGISTER)flink-sql-fuse:${VERSION}-flink_1.16 \
		-t $(REGISTER)flink-sql-fuse:flink_1.16 .

	docker build --build-arg SRC_JAR=flink-fuse-15/target/flink-fuse-15-${VERSION}.jar \
		-t $(REGISTER)flink-sql-fuse:${VERSION}-flink_1.15 \
		-t $(REGISTER)flink-sql-fuse:flink_1.15 .

# Publish docker image
.PHONY: publish-image
publish-image:
	docker push $(REGISTER)flink-sql-fuse:${VERSION}-flink_1.17
	docker push $(REGISTER)flink-sql-fuse:${VERSION}-flink_1.16
	docker push $(REGISTER)flink-sql-fuse:${VERSION}-flink_1.15
	docker push $(REGISTER)flink-sql-fuse:flink_1.17
	docker push $(REGISTER)flink-sql-fuse:flink_1.16
	docker push $(REGISTER)flink-sql-fuse:flink_1.15

