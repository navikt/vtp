package no.nav.foreldrepenger.vtp.felles;

import static java.lang.System.getProperty;
import static java.lang.System.getenv;

public class KeystoreUtils {
    public static Boolean sslDisabled() {
        if (null != getenv("DISABLE_SSL")) {
            return getenv("DISABLE_SSL") == "true";
        }
        if (null != getProperty("disable.ssl")) {
            return getProperty("disable.ssl") == "true";
        }
        return false;
    }

    public static String getKeystoreFilePath() {
        if (null != getenv("NO_NAV_MODIG_SECURITY_APPCERT_KEYSTORE")) {
            return getenv("NO_NAV_MODIG_SECURITY_APPCERT_KEYSTORE");
        }
        return getProperty("user.home", ".") + "/.modig/keystore.jks";
    }

    public static String getKeyStorePassword() {
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
