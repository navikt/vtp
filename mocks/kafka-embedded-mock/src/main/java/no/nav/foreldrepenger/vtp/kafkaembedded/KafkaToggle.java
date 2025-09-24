package no.nav.foreldrepenger.vtp.kafkaembedded;


import static java.lang.System.getenv;

import java.util.Objects;

public class KafkaToggle {

    public static boolean skalBrukeNyKafka() {
        return Objects.equals(getenv("KAFKA_BRUK_AIVEN_PROPERTY_LOKALT"), "true");
    }
}
