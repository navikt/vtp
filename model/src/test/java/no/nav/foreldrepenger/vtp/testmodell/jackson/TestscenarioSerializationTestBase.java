package no.nav.foreldrepenger.vtp.testmodell.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.json.JsonMapper;

import no.nav.foreldrepenger.vtp.testmodell.util.JacksonObjectMapperTestscenario;

public class TestscenarioSerializationTestBase {

    protected static final Logger LOG = LoggerFactory.getLogger(TestscenarioSerializationTestBase.class);
    protected static JsonMapper jsonMapper;

    @BeforeAll
    static void beforeAll() {
        jsonMapper = JacksonObjectMapperTestscenario.getJsonMapper();
    }
    protected static void test(Object obj) {
        test(obj, true);
    }

    private static void test(Object obj, boolean log) {
        try {
            if (log) {
                LOG.info("{}", obj);
            }
            String serialized = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            if (log) {
                LOG.info("Serialized as {}", serialized);
            }
            Object deserialized = jsonMapper.readValue(serialized, obj.getClass());
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
