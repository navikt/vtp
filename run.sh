#!/bin/bash
cd /var/log/apps/mock
java -cp  /home/deployer/mock-server/target/mock-server-1.0.4-SNAPSHOT.jar:/home/deployer/mock-server/lib/* no.nav.engangsstønad.mock.MockServer >> console.log &
