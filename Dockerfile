FROM gcr.io/distroless/java17-debian11:nonroot

LABEL org.opencontainers.image.source=https://github.com/navikt/vtp

ENV JAVA_OPTS "-Dlogback.configurationFile=/app/logback.xml -XX:MaxRAMPercentage=75.0 -Dfile.encoding=UTF-8 -Duser.timezone=Europe/Oslo"

WORKDIR /app

COPY server/kafkasecurity.conf ./
COPY server/src/main/resources/logback.xml ./
COPY server/target/app.jar ./
COPY server/target/lib/*.jar ./lib/

CMD ["/app/app.jar"]
