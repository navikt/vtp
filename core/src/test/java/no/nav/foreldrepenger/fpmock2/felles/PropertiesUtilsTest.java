package no.nav.foreldrepenger.fpmock2.felles;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.junit.Test;

public class PropertiesUtilsTest {

    @Test
    public void kanLeseGloApplicationsProperties() {
        System.clearProperty("server.port");
        PropertiesUtils.initProperties("../");
        
        if(null == System.getProperty("server.port")) {
            throw new RuntimeException("Kan ikke lese properties fil");
        }
        
    }
}
