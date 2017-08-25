#!/bin/bash
SERVER=
IS_JETTY=false

usage() {
    echo "Usage: -s [jboss|jetty]" >&2; exit 1
}

while getopts "s:" option; do
    case "${option}" in
        s) SERVER=${OPTARG};;
        *) usage;;
    esac
done

if [[ ($SERVER == "") && (!($SERVER == "jetty") || !($SERVER == "jboss")) ]]; then
    usage
fi

if [[ $SERVER == "jetty" ]]; then
    IS_JETTY=true
fi

export mock_keystore=/home/deployer/keystore.jks
export modigcerts=$IS_JETTY
cd /var/log/apps/mock
java -cp  /home/deployer/mock-server/target/mock-server-1.0.5-SNAPSHOT.jar:/home/deployer/mock-server/lib/* no.nav.engangsstønad.mock.MockServer >> console.log &