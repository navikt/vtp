FROM navikt/java:8

COPY mock-server/target/mock-server*.jar /app/app.jar
COPY mock-server/lib/*.jar /app/
COPY run-java.sh /