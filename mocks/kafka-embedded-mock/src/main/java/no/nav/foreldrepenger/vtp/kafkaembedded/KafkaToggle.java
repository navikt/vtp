package no.nav.foreldrepenger.vtp.kafkaembedded;


import java.util.Objects;

import static java.lang.System.getenv;

public class KafkaToggle {

    public static boolean skalBrukeNyKafka() {
        return Objects.equals(getenv("KAFKA_BRUK_AIVEN_PROPERTY_LOKALT"), "true");
    }
}
