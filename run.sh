#!/bin/bash
java -cp mock-server/target/mock-server-1.0.2-SNAPSHOT.jar:mock-server/lib/* no.nav.engangsstønad.mock.MockServer >> /var/log/mock.log &
