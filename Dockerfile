# Denne containeren kjører med en non-root bruker
FROM ghcr.io/navikt/fp-baseimages/chainguard:jre-25

LABEL org.opencontainers.image.source=https://github.com/navikt/vtp

COPY server/kafkasecurity.conf ./
COPY server/src/main/resources/logback.xml ./
COPY server/target/app.jar ./
COPY server/target/lib/*.jar ./lib/
