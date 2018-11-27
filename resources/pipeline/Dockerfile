FROM docker.adeo.no:5000/fpmock2:autotest

COPY enablecustomcerts.sh /init-scripts
RUN mkdir /app/testcerts
COPY vtpkeystore /app/testcerts/vtpkeystore.jks