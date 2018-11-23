#!/usr/bin/env sh
set -eu

export JAVA_OPTS="${JAVA_OPTS:-} -Xmx1024m -Xms128m -Djava.security.egd=file:/dev/./urandom"

if [ -r "${NAV_TRUSTSTORE_PATH:-}" ]; then
    if ! echo "${NAV_TRUSTSTORE_PASSWORD}" | keytool -list -keystore ${NAV_TRUSTSTORE_PATH} > /dev/null;
    then
        echo Truststore is corrupt, or bad password
        exit 1
    fi

    JAVA_OPTS="${JAVA_OPTS} -Djavax.net.ssl.trustStore=${NAV_TRUSTSTORE_PATH}"
    JAVA_OPTS="${JAVA_OPTS} -Djavax.net.ssl.trustStorePassword=${NAV_TRUSTSTORE_PASSWORD}"
fi

export JAVA_OPTS="${JAVA_OPTS:-} --vtp"

# hvor skal gc log, heap dump etc kunne skrives til med Docker?
export todo_JAVA_OPTS="${JAVA_OPTS} -XX:ErrorFile=./hs_err_pid<pid>.log -XX:HeapDumpPath=./java_pid<pid>.hprof -XX:-HeapDumpOnOutOfMemoryError -Xloggc:<filename>"
export STARTUP_CLASS=${STARTUP_CLASS:-"no.nav.foreldrepenger.web.server.jetty.JettyDevServer"}
export CLASSPATH=app.jar:lib/*
export LOGBACK_CONFIG=${LOGBACK_CONFIG:-"./conf/logback.xml"}

exec java -cp ${CLASSPATH:-"app.jar:lib/*"} ${DEFAULT_JAVA_OPTS:-} ${JAVA_OPTS} -Dlogback.configurationFile=${LOGBACK_CONFIG?} -Dconf=${CONF:-"./conf"} -Dwebapp=${WEBAPP:-"./webapp"} -Dklient=${KLIENT:-"./klient"} -Di18n=${I18N:-"./i18n"} -Dapplication.name=${APP_NAME} ${STARTUP_CLASS?} $@
