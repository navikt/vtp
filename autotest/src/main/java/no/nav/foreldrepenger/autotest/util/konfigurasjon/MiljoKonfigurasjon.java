package no.nav.foreldrepenger.autotest.util.konfigurasjon;

import java.io.File;
import java.util.Properties;

public class MiljoKonfigurasjon extends KonfigurasjonBase{
    
    public static String AUTOTEST_ENV = "AUTOTEST_ENV";
    
	public static String ENV_CONFIG_LOCATION_FORMAT = "%s//%s.properties";
    public static String AUTOTEST_EVN = "AUTOTEST_EVN";
	
	public static String PROPERTY_FPSAK_API_ROOT = "autotest.fpsak.restservice.baseUrl";
	public static String PROPERTY_FPSAK_SELFTEST_URL = "autotest.fpsak.selftest.url";
	
	public void loadEnv(String env) {
	    String resource = String.format(ENV_CONFIG_LOCATION_FORMAT, env, env);
	    File envFile = new File(MiljoKonfigurasjon.class.getClassLoader().getResource(resource).getFile());
	    loadFile(envFile);
	}
	
	public static String hentMiljø() {
	    String env = System.getenv(AUTOTEST_ENV);
	    return env == null ? "localhost" : env;
	}

    public static String hentRestRootUrl() {
        return System.getProperty(PROPERTY_FPSAK_API_ROOT);
    }

    public static String hentSelftestUrl() {
        return System.getProperty(PROPERTY_FPSAK_SELFTEST_URL);
    }
    
    public static void initProperties() {
        new MiljoKonfigurasjon().loadEnv(hentMiljø());
    }
}
