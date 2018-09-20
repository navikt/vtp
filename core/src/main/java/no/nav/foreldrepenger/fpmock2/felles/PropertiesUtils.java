package no.nav.foreldrepenger.fpmock2.felles;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.nio.file.Paths;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);

    private static String DEV_FILNAVN = "application.properties";
    private static String DEV_FILNAVN_LOCAL = "application-local.properties";
    
    //Brukes av applikasjoner som kjører på root dir
    public static void initProperties() {
        initProperties("");
    }
    
    //Brukes av tester som bruker root application.properties
    public static void initProperties(String propertyDir) {
        File devFil = Paths.get(propertyDir, DEV_FILNAVN).toFile();
        loadPropertyFile(devFil);
        loadPropertyFile(Paths.get(propertyDir, DEV_FILNAVN_LOCAL).toFile());
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
        }else {
            LOGGER.warn("Kunne ikke finne properties-fil: " + devFil.getAbsolutePath());
        }
    }
}
