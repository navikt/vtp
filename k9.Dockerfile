FROM ghcr.io/navikt/sif-baseimages/java-chainguard-25:latest

LABEL org.opencontainers.image.source=https://github.com/navikt/vtp

COPY server/kafkasecurity.conf ./
COPY server/src/main/resources/logback.xml ./
COPY server/target/app.jar ./
COPY server/target/lib/*.jar ./lib/
