FROM navikt/java:8

ENV JAVA_OPTS="-Dscenarios.dir=./model/scenarios"
ENV LC_ALL="no_NB.UTF-8"
ENV LANG="no_NB.UTF-8"
ENV TZ="Europe/Oslo"
ARG JAR_FILE

RUN mkdir /app/lib
COPY server/lib/*.jar /app/lib/
COPY model/scenarios/ /app/model/scenarios/

COPY server/src/main/resources/logback.xml logback.xml
COPY server/target/server*.jar app.jar
COPY run-java.sh /

RUN chmod +x /run-java.sh
