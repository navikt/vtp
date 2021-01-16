FROM navikt/java:15
ENV JAVA_OPTS --enable-preview
ENV DUMMYPROP=fraDockerfile

LABEL org.opencontainers.image.source=https://github.com/navikt/vtp

ARG JAR_FILE

# Curl brukes av healthcheck i docker-compose.
RUN apt-get -qq update && apt-get -qq -y install curl
RUN mkdir /app/lib
COPY server/target/lib/*.jar /app/lib/

COPY server/kafkasecurity.conf /app/
COPY server/src/main/resources/logback.xml logback.xml
COPY server/target/app.jar app.jar
COPY run-java.sh /

EXPOSE 8636 8063 8060 8389 9093

RUN chmod +x /run-java.sh
