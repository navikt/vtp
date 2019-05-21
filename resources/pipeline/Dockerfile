FROM docker.adeo.no:5000/fpmock2:latest

COPY enablecustomcerts.sh /init-scripts
RUN mkdir /app/testcerts
COPY keystore/vtpkeystore /app/testcerts/vtpkeystore.jks