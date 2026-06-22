package no.nav.foreldrepenger.vtp.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);

    private static final String DEV_FILNAVN = "application.properties";

    private static Properties PROPS;

    private PropertiesUtils() {
    }

    public static synchronized void initProperties() {
        if (PROPS == null) {
            Properties prop = new Properties();
            var devFil = Paths.get("", DEV_FILNAVN).toFile();
            if (devFil.exists()) {
                loadPropertyFile(prop, devFil);
            } else {
                LOGGER.warn("Kunne ikke finne properties-fil: {}", devFil.getAbsolutePath());
            }
            prop.putAll(System.getenv());
            LOGGER.info("PROPERTIES LASTET: ");
            PROPS = prop;
        }
    }

    private static void loadPropertyFile(Properties properties, File devFil) {
        try (InputStream inputStream = new FileInputStream(devFil)) {
            properties.load(inputStream);
        } catch (IOException e) {
            LOGGER.error("Kunne ikke finne properties-fil", e);
        }
    }

    public static String get(String key) {
        // Prioritet: System.getProperty, System.getenv, properties-fil
        if (PROPS == null) {
            throw new IllegalStateException("Properties not initialized");
        }
        return Optional.ofNullable(System.getProperty(key))
                .or(() -> Optional.ofNullable(PROPS.getProperty(key.toUpperCase().replace(".", "_"))))
                .orElseGet(() -> PROPS.getProperty(key));
    }

    public static String get(String key, String defaultValue) {
        return Optional.ofNullable(get(key)).orElse(defaultValue);
    }


}
