package no.nav.foreldrepenger.fpmock2.felles;

public class KeystoreUtils {

    public static String getKeystoreFilePath() {
        if (null != System.getenv("NO_NAV_MODIG_SECURITY_APPCERT_KEYSTORE")){
            return System.getenv("NO_NAV_MODIG_SECURITY_APPCERT_KEYSTORE");
        }
        throw new IllegalStateException("Keystore path mangler. Konfigurer systemvariabel: NO_NAV_MODIG_SECURITY_APPCERT_KEYSTORE");
    }

    public static String getKeyStorePassword() {
        if(null != System.getenv("NO_NAV_MODIG_SECURITY_APPCERT_PASSWORD")){
            return System.getenv("NO_NAV_MODIG_SECURITY_APPCERT_PASSWORD");
        }
        throw new IllegalStateException("Keystore passord mangler. Konfigurer systemvariabel: NO_NAV_MODIG_SECURITY_APPCERT_PASSWORD");
    }

    public static String getTruststoreFilePath() {
        if (null != System.getenv("JAVAX_NET_SSL_TRUSTSTORE")){
            return System.getenv("JAVAX_NET_SSL_TRUSTSTORE");
        }
        throw new IllegalStateException("Keystore path mangler. Konfigurer systemvariabel: JAVAX_NET_SSL_TRUSTSTORE");
    }

    public static String getTruststorePassword() {
        if(null != System.getenv("JAVAX_NET_SSL_TRUSTSTOREPASSWORD")){
            return System.getenv("JAVAX_NET_SSL_TRUSTSTOREPASSWORD");
        }
        throw new IllegalStateException("Keystore passord mangler. Konfigurer systemvariabel: JAVAX_NET_SSL_TRUSTSTOREPASSWORD");
    }
}
