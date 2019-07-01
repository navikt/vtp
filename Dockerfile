FROM navikt/java:11

ENV JAVA_OPTS="-Dscenarios.dir=/app/model/scenarios/"
ENV DUMMYPROP=fraDockerfile

ARG JAR_FILE

RUN mkdir /app/lib
COPY server/target/lib/*.jar /app/lib/
COPY model/scenarios/ /app/model/scenarios/
RUN chmod -R 777 /app/model/scenarios/

COPY server/kafkasecurity.conf /app/
COPY server/src/main/resources/logback.xml logback.xml
COPY server/target/server*.jar app.jar
COPY run-java.sh /

EXPOSE 8636 8063 8060 8389 9092 9093

RUN chmod +x /run-java.sh
