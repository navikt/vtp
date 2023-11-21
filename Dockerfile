FROM gcr.io/distroless/java21-debian12:nonroot
LABEL org.opencontainers.image.source=https://github.com/navikt/vtp
# Healtcheck lokalt/test
COPY --from=busybox:stable-musl /bin/wget /usr/bin/wget

WORKDIR /app

COPY server/kafkasecurity.conf ./
COPY server/src/main/resources/logback.xml ./
COPY server/target/app.jar ./
COPY server/target/lib/*.jar ./lib/

ENV JAVA_OPTS "-XX:MaxRAMPercentage=75.0 \
    -XX:+PrintCommandLineFlags \
    -Dfile.encoding=UTF-8 \
    -Duser.timezone=Europe/Oslo \
    -Dlogback.configurationFile=/app/logback.xml"

CMD ["app.jar"]
