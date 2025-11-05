FROM ghcr.io/navikt/sif-baseimages/java-25:2025.11.04.1325Z

LABEL org.opencontainers.image.source=https://github.com/navikt/vtp

COPY server/kafkasecurity.conf ./
COPY server/src/main/resources/logback.xml ./
COPY server/target/app.jar ./
COPY server/target/lib/*.jar ./lib/

# lar apprunner eie /app/target slik at zookeeper kan skrive dit
USER root
RUN mkdir /app/target && chown -R apprunner:apprunner /app/target

USER apprunner
