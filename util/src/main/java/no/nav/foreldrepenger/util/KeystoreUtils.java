package no.nav.foreldrepenger.util;

import static java.lang.System.getProperty;
import static java.lang.System.getenv;

public class KeystoreUtils {

    private KeystoreUtils() {
        // Skal ikke instansieres
    }

    public static String getKeystoreFilePath() {
        if (null != getenv("JAVAX_NET_SSL_KEYSTORE")) {
            return getenv("JAVAX_NET_SSL_KEYSTORE");
        }
        if (null != getenv("NO_NAV_MODIG_SECURITY_APPCERT_KEYSTORE")) {
            return getenv("NO_NAV_MODIG_SECURITY_APPCERT_KEYSTORE");
        }
        return getProperty("user.home", ".") + "/.modig/keystore.jks";
    }

    public static String getKeyStorePassword() {
        if (null != getenv("JAVAX_NET_SSL_KEYSTOREPASSWORD")) {
            return getenv("JAVAX_NET_SSL_KEYSTOREPASSWORD");
        }
        if (null != getenv("NO_NAV_MODIG_SECURITY_APPCERT_PASSWORD")) {
            return getenv("NO_NAV_MODIG_SECURITY_APPCERT_PASSWORD");
        }
        return "devillokeystore1234";
    }

    public static String getTruststoreFilePath() {
        if (null != getenv("JAVAX_NET_SSL_TRUSTSTORE")) {
            return getenv("JAVAX_NET_SSL_TRUSTSTORE");
        }
        if (null != getenv("NAV_TRUSTSTORE_PATH")) {
            return getenv("NAV_TRUSTSTORE_PATH");
        }
        return getProperty("user.home", ".") + "/.modig/truststore.jks";
    }

    public static String getTruststorePassword() {
        if (null != getenv("JAVAX_NET_SSL_TRUSTSTOREPASSWORD")) {
            return getenv("JAVAX_NET_SSL_TRUSTSTOREPASSWORD");
        }
        if (null != getenv("NAV_TRUSTSTORE_PASSWORD")) {
            return getenv("NAV_TRUSTSTORE_PASSWORD");
        }
        return "changeit";
    }

}
