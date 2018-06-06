#!/usr/bin/env sh

mkdir logs
export APP_LOG_HOME=./logs
exec java ${DEFAULT_JAVA_OPTS} ${JAVA_OPTS} -cp app.jar:lib/* no.nav.vedtak.mock.local.MockServer