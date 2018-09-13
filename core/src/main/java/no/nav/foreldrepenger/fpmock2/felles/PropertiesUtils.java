package no.nav.foreldrepenger.fpmock2.felles;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);


    private static String DEV_FILNAVN = "application.properties";
    private static String DEV_FILNAVN_LOCAL = "application-local.properties";

    public static void initProperties() {
        File devFil = new File(DEV_FILNAVN);
        loadPropertyFile(devFil);
        loadPropertyFile(new File(DEV_FILNAVN_LOCAL));
        LOGGER.info("PROPERTIES LASTET");
    }

    private static void loadPropertyFile(File devFil) {
        if (devFil.exists()) {
            Properties prop = new Properties();
            try (InputStream inputStream = new FileInputStream(devFil)) {
                prop.load(inputStream);
            } catch (IOException e) {
                LOGGER.error("Kunne ikke finne properties-fil", e);
            }
            System.getProperties().putAll(prop);
        }
    }

}
