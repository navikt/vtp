package no.nav.foreldrepenger.autotest.util.konfigurasjon;

import java.io.File;

public class MiljoKonfigurasjon extends KonfigurasjonBase{
    
    public static String AUTOTEST_ENV = "AUTOTEST_ENV";
    
    public static String ENV_PROPERTY_LOCATION_FORMAT = "%s//%s.properties";
    
    public static String PROPERTY_FPSAK_HTTP_PROTOCOL = "autotest.fpsak.http.protocol";
    public static String PROPERTY_FPSAK_HTTP_HOSTNAME = "autotest.fpsak.http.hostname";
    public static String PROPERTY_FPSAK_HTTP_PORT = "autotest.fpsak.http.port";
    public static String PROPERTY_FPSAK_HTTP_ROUTING_INDEX = "autotest.fpsak.http.routing.index";
    public static String PROPERTY_FPSAK_HTTP_ROUTING_SELFTEST = "autotest.fpsak.http.routing.selftest";
    public static String PROPERTY_FPSAK_HTTP_ROUTING_METRICS = "autotest.fpsak.http.routing.metrics";
    public static String PROPERTY_FPSAK_HTTP_ROUTING_API = "autotest.fpsak.http.routing.api";

    
    /*
     * Load env
     */
    public void loadEnv(String env) {
        String resource = String.format(ENV_PROPERTY_LOCATION_FORMAT, env, env);
        File envFile = new File(MiljoKonfigurasjon.class.getClassLoader().getResource(resource).getFile());
        loadFile(envFile);
    }

    public static String hentMiljø() {
        String env = System.getenv(AUTOTEST_ENV);
        if(null == env){
            env = System.getProperty(AUTOTEST_ENV);
        }
        return env == null ? "localhost" : env;
    }
        
    public static void initProperties() {
        new MiljoKonfigurasjon().loadEnv(hentMiljø());
    }
    
    /*
     * Routing
     */
    public static String getProtocol() {
        return System.getProperty(PROPERTY_FPSAK_HTTP_PROTOCOL);
    }
    
    public static String getHostname() {
        return System.getProperty(PROPERTY_FPSAK_HTTP_HOSTNAME);
    }
    
    public static String getPort() {
        return System.getProperty(PROPERTY_FPSAK_HTTP_PORT);
    }
    
    public static String getRootUrl() {
        return String.format("%s://%s:%s", getProtocol(), getHostname(), getPort());
    }
    
    public static String getRouteIndex() {
        return getRoute(PROPERTY_FPSAK_HTTP_ROUTING_INDEX);
    }
    
    public static String getRouteSelfTest() {
        return getRoute(PROPERTY_FPSAK_HTTP_ROUTING_SELFTEST);
    }

    public static String getRouteApi() {
        return getRoute(PROPERTY_FPSAK_HTTP_ROUTING_API);
    }

    public static String getRouteMetrics() {
        return getRoute(PROPERTY_FPSAK_HTTP_ROUTING_METRICS);
    }
    
    
    /*
     * Private
     */
    private static String getRoute(String routeProperty) {
        String test1 = getRootUrl();
        String test2 = System.getProperty(routeProperty);
        return test1 + test2;
    }
}
