FROM navikt/java:10

EXPOSE 8636 8063 8060 8389

ENV JAVA_OPTS="-Dscenarios.dir=/app/model/scenarios/"
ENV DUMMYPROP=fraDockerfile

ARG JAR_FILE
COPY resources/pipeline/enablecustomcerts.sh /init-scripts
RUN mkdir /app/testcerts
COPY resources/pipeline/keystore/vtpkeystore            /app/testcerts/vtpkeystore.jks
COPY resources/pipeline/keystore/nav_truststore_path    /app/testcerts/nav_truststore_path

RUN mkdir /app/lib
COPY server/lib/*.jar /app/lib/
COPY model/scenarios/ /app/model/scenarios/
RUN chmod -R 777 /app/model/scenarios/

COPY server/src/main/resources/logback.xml logback.xml
COPY server/target/server*.jar app.jar
COPY run-java.sh /

RUN chmod +x /run-java.sh
