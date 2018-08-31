FROM navikt/java:8

RUN mkdir /app/lib
COPY server/src/main/webapp /app/webapp
COPY server/src/main/resources/logback.xml logback.xml
COPY server/target/server*.jar /app/app.jar
COPY server/lib/*.jar /app/lib/
COPY run-java.sh /
RUN chmod +x /run-java.sh
