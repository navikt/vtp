#!/usr/bin/env sh

exec java ${DEFAULT_JAVA_OPTS} ${JAVA_OPTS} -cp app.jar:lib/* no.nav.engangsstønad.mock.MockServer