package no.nav.foreldrepenger.autotest.util.konfigurasjon;

import java.io.File;

public class TestKonfigurasjon extends KonfigurasjonBase{
    
    private static String KONFIGURASJONS_FIL = "../application.properties";
    private static String KONFIGURASJONS_FIL_LOKAL = "../application-local.properties";
	
	public static String ENV_USERNAME_FORMAT = "autotest.openam.%s.username";
    public static String ENV_PASSWORD_FORMAT = "autotest.openam.%s.password";
    
    public TestKonfigurasjon() {
	}
    
    public static Bruker hentBruker(String role){
        return new Bruker(System.getProperty(String.format(ENV_USERNAME_FORMAT, role.toLowerCase())),
                        System.getProperty(String.format(ENV_PASSWORD_FORMAT, role.toLowerCase())));
    }
    
    public static String hentOICDSystemBruker() {
        return System.getProperty("autotest.oicd.username");
    }
    
    public static String hentOICDSystemPassord() {
		return System.getProperty("autotest.oicd.password");
    }
    
    public static String hentOICDUrl() {
        return System.getProperty("autotest.oicd.url");
    }
    
    public static class Bruker{
        public String brukernavn;
        public String passord;
        
        public Bruker(String username, String password){
            this.brukernavn = username;
            this.passord = password;
        }
    }
    
    public static void init() {
        TestKonfigurasjon konfigurasjon = new TestKonfigurasjon();
        konfigurasjon.loadFile(new File(KONFIGURASJONS_FIL));
        try {
            konfigurasjon.loadFile(new File(KONFIGURASJONS_FIL_LOKAL));
        } catch (Exception e) {
            System.out.println("Kunne ikke laste inn lokal konfigurasjon");
        }
    }
    
}
