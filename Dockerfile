FROM navikt/java:8

EXPOSE 8080 636

ENV JAVA_OPTS="-Dscenarios.dir=/app/model/scenarios/"
ARG JAR_FILE

RUN mkdir /app/lib
COPY server/lib/*.jar /app/lib/
COPY model/scenarios/ /app/model/scenarios/
RUN chmod -R 777 /app/model/scenarios/

COPY server/src/main/resources/logback.xml logback.xml
COPY server/target/server*.jar app.jar
COPY run-java.sh /

RUN chmod +x /run-java.sh
