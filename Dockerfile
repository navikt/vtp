FROM navikt/java:17

LABEL org.opencontainers.image.source=https://github.com/navikt/vtp

# Curl brukes av healthcheck i docker-compose.
RUN mkdir /app/lib

COPY server/target/lib/*.jar /app/lib/
COPY server/kafkasecurity.conf /app/
COPY server/src/main/resources/logback.xml logback.xml
COPY server/target/app.jar app.jar

ENV JAVA_OPTS="-Dlogback.configurationFile=logback.xml \
               		-XX:MaxRAMPercentage=75.0 \
               		-Dfile.encoding=UTF-8 \
               		-Duser.timezone=Europe/Oslo"
