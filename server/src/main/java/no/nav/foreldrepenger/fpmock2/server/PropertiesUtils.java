package no.nav.foreldrepenger.fpmock2.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class PropertiesUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);

    private static String TEMPLATE_FILNAVN = "application-local.properties";
    private static String JETTY_SCHEMAS_LOCAL = "jetty_web_server.json"; // ikke i bruk enda, tar med for senere

    private static String DEV_FILNAVN = "application.properties";
    private static String DEV_FILNAVN_LOCAL = "application-local.properties";

    public static void lagPropertiesFilFraTemplate() throws IOException {
        File devFil = new File(DEV_FILNAVN);

        ClassLoader classLoader = PropertiesUtils.class.getClassLoader();
        File templateFil = new File(classLoader.getResource(TEMPLATE_FILNAVN).getFile());

        copyTemplateFile(templateFil, devFil, true);

        // create local file
        File localProps = new File(DEV_FILNAVN_LOCAL);
        if (!localProps.exists()) {
            boolean fileCreated = localProps.createNewFile();
            if (!fileCreated) {
                LOGGER.error("Kunne ikke opprette properties-fil");
            }
        }
    }

    public static void copyTemplateFile(File templateFil, File targetFil, boolean backup) throws IOException {
        if (!targetFil.exists()) {
            Files.copy(templateFil.toPath(), targetFil.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } else if ((targetFil.lastModified() < templateFil.lastModified())) {
            if (backup) {
                File backupDev = new File(targetFil.getAbsolutePath() + ".backup");
                Files.copy(targetFil.toPath(), backupDev.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            Files.copy(templateFil.toPath(), targetFil.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static void initProperties() {
        File devFil = new File(DEV_FILNAVN);
        loadPropertyFile(devFil);
        loadPropertyFile(new File(DEV_FILNAVN_LOCAL));
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
