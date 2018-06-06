FROM navikt/java:8

RUN mkdir /app/lib
COPY mock-server/src/main/resources/logback.xml logback.xml
COPY mock-server/target/mock-server*.jar /app/app.jar
COPY mock-server/lib/*.jar /app/lib/
COPY run-java.sh /
RUN chmod +x /run-java.sh
