#!/usr/bin/env sh

exec java ${DEFAULT_JAVA_OPTS} ${JAVA_OPTS} -cp app.jar:lib/* -Dlogback.configurationFile=logback.xml -Dscenarios.dir="/app/model/scenarios/" no.nav.foreldrepenger.fpmock2.server.MockServer