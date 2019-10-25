#!/usr/bin/env sh

if [[ $MSYSTEM =~ MINGW* ]]; then
	set -ex
	(cd server && exec java ${DEFAULT_JAVA_OPTS} ${JAVA_OPTS} -cp 'target/app.jar;target/lib/*' \
		-Dlogback.configurationFile=logback.xml \
		-Dfile.encoding=UTF8 \
		-Dscenarios.dir=../model/scenarios \
		no.nav.foreldrepenger.vtp.server.MockServer) 
else 
	exec java ${DEFAULT_JAVA_OPTS} ${JAVA_OPTS} -cp app.jar:lib/* \
		-Dlogback.configurationFile=logback.xml \
		-Dfile.encoding=UTF8 no.nav.foreldrepenger.vtp.server.MockServer
fi
