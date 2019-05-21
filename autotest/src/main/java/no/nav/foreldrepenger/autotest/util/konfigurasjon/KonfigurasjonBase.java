package no.nav.foreldrepenger.autotest.util.konfigurasjon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class KonfigurasjonBase {
    
    public Properties properties;

    public void loadFile(File file) {
        properties = new Properties();
    InputStream stream;
    
    try {
        stream = new FileInputStream(file);
    } catch (FileNotFoundException e) {
        throw new RuntimeException(file.getAbsolutePath() + " returns null from file stream " + e.getMessage());
    }
        
    try {
        properties.load(stream);
        
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        
        System.getProperties().putAll(properties);
    }
}
