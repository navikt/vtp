package no.nav.foreldrepenger.fpmock2.felles;

import org.junit.Test;

public class PropertiesUtilsTest {

    @Test
    public void kanLeseGloApplicationsProperties() {
        System.clearProperty("autotest.vtp.port");
        PropertiesUtils.initProperties("../");

        if(null == System.getProperty("autotest.vtp.port")) {
            throw new RuntimeException("Kan ikke lese properties fil");
        }

    }
}
