FROM docker.adeo.no:5000/fpmock2:autotest

ENV CUSTOM_KEYSTORE_PATH=/app/testcerts/vtpkeystore.jks
ENV CUSTOM_KEYSTORE_PASSWORD=changeit
ENV ENABLE_CUSTOM_TRUSTSTORE=true

RUN mkdir /app/testcerts
COPY vtpkeystore /app/testcerts/vtpkeystore.jks