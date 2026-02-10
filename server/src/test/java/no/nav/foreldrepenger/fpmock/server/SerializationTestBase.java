package no.nav.foreldrepenger.fpmock.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.vtp.testmodell.util.JacksonObjectMapperTestscenario;

public class SerializationTestBase {

    protected static final Logger LOG = LoggerFactory.getLogger(SerializationTestBase.class);

    protected static void test(Object obj) {
        test(obj, true);
    }

    private static void test(Object obj, boolean log) {
        try {
            if (log) {
                LOG.info("{}", obj);
            }
            String serialized = JacksonObjectMapperTestscenario.writeValueAsString(obj);
            if (log) {
                LOG.info("Serialized as {}", serialized);
            }
            Object deserialized = JacksonObjectMapperTestscenario.getJsonMapper().readValue(serialized, obj.getClass());
            if (log) {
                LOG.info("{}", deserialized);
            }
            assertEquals(obj, deserialized);
        } catch (Exception e) {
            LOG.error("Oops", e);
            fail(obj.getClass().getSimpleName() + " failed");
        }
    }
}
