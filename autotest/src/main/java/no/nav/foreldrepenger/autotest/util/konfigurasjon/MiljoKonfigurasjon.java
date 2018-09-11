package no.nav.foreldrepenger.autotest.util.konfigurasjon;

import java.io.File;
import java.util.Properties;

public class MiljoKonfigurasjon extends KonfigurasjonBase{
	
    public static MiljoKonfigurasjon konfigurasjon;
    
    public static String AUTOTEST_ENV = "AUTOTEST_ENV";
    
	public static String ENV_CONFIG_LOCATION_FORMAT = "%s//%s.properties";
    public static String AUTOTEST_EVN = "AUTOTEST_EVN";
	
	public static String PROPERTY_FPSAK_API_ROOT = "autotest.restservice.fpsak.baseUrl";
	
	public MiljoKonfigurasjon() {
		properties  = new Properties();
		loadEnv(hentMiljø());
		konfigurasjon = this;
	}
	
	public void loadEnv(String env) {
	    String resource = String.format(ENV_CONFIG_LOCATION_FORMAT, env, env);
	    File envFile = new File(MiljoKonfigurasjon.class.getClassLoader().getResource(resource).getFile());
	    loadFile(envFile);
	}
	
	public static String hentMiljø() {
	    String env = System.getenv(AUTOTEST_ENV);
	    return env == null ? "localhost" : env;
	}

    public Object hentRestRootUrl() {
        return hentProperty(PROPERTY_FPSAK_API_ROOT);
    }
}
