FROM ghcr.io/navikt/fp-baseimages/java:17

LABEL org.opencontainers.image.source=https://github.com/navikt/vtp

RUN mkdir lib

ENV JAVA_OPTS "-Dlogback.configurationFile=logback.xml -XX:MaxRAMPercentage=75.0 -Dfile.encoding=UTF-8 -Duser.timezone=Europe/Oslo"

COPY server/target/lib/*.jar ./lib/
COPY server/kafkasecurity.conf ./
COPY server/src/main/resources/logback.xml ./
COPY server/target/app.jar ./
