#!/usr/bin/env sh

exec java ${DEFAULT_JAVA_OPTS} ${JAVA_OPTS} -cp app.jar:lib/* -Dlogback.configurationFile=logback.xml no.nav.vedtak.mock.local.MockServer