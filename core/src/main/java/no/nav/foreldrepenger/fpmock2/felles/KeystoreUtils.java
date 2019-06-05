package no.nav.foreldrepenger.fpmock2.felles;

import static java.lang.System.getProperty;
import static java.lang.System.getenv;

public class KeystoreUtils {

    public static String getKeystoreFilePath() {
        if (null != getenv("NO_NAV_MODIG_SECURITY_APPCERT_KEYSTORE")) {
            return getenv("NO_NAV_MODIG_SECURITY_APPCERT_KEYSTORE");
        }
        return getProperty("user.home", ".") + "/.modig/keystore.jks";
    }

    public static String getKeyStorePassword() {
        if(null != getenv("NO_NAV_MODIG_SECURITY_APPCERT_PASSWORD")){
            return getenv("NO_NAV_MODIG_SECURITY_APPCERT_PASSWORD");
        }
        return "devillokeystore1234";
    }

    public static String getTruststoreFilePath() {
        if (null != getenv("JAVAX_NET_SSL_TRUSTSTORE")){
            return getenv("JAVAX_NET_SSL_TRUSTSTORE");
        }
        return getProperty("user.home", ".") + "/.modig/truststore.jks";
    }

    public static String getTruststorePassword() {
        if(null != getenv("JAVAX_NET_SSL_TRUSTSTOREPASSWORD")){
            return getenv("JAVAX_NET_SSL_TRUSTSTOREPASSWORD");
        }
        return "changeit";    }
}
