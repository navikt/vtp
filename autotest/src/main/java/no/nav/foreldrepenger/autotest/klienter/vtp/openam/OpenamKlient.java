package no.nav.foreldrepenger.autotest.klienter.vtp.openam;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.impl.cookie.BasicClientCookie;

import no.nav.foreldrepenger.autotest.klienter.vtp.VTPKlient;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.fpmock2.felles.OidcTokenGenerator;
import no.nav.modig.testcertificates.TestCertificates;

public class OpenamKlient extends VTPKlient {

    private static final Map<String, BasicClientCookie> loginCookies = new ConcurrentHashMap<>();

    static {

        if (null == System.getenv("ENABLE_CUSTOM_TRUSTSTORE") || System.getenv("ENABLE_CUSTOM_TRUSTSTORE").equalsIgnoreCase("false")) {
            TestCertificates.setupKeyAndTrustStore();
        }
    }

    public OpenamKlient(HttpSession session) {
        super(session);
    }

    public void logInnMedRolle(String rolle) throws IOException {

        loginBypass(rolle);
    }

    private void loginBypass(String rolle) {
        BasicClientCookie cookie = loginCookies.computeIfAbsent(rolle, this::createCookie);
        session.leggTilCookie(cookie);
    }

    BasicClientCookie createCookie(String rolle) {
        String issuer;
        if(null != System.getenv("ENABLE_CUSTOM_TRUSTSTORE") && System.getenv("ENABLE_CUSTOM_TRUSTSTORE").equalsIgnoreCase("true")) {
            // @todo Hvor blir dette brukt. Kan det bruke samme instilling som
            issuer = System.getProperty("isso.oauth2.issuer", "https://fpmock2:8063/rest/isso/oauth2");
        } else {
            issuer = System.getProperty("isso.oauth2.issuer", "https://localhost:8063/rest/isso/oauth2"); //fixme med propertyutils
        }

        String token = new OidcTokenGenerator(rolle, "notcetnonce").withIssuer(issuer).create();

        BasicClientCookie cookie = new BasicClientCookie("ID_token", token);
        cookie.setPath("/");
        cookie.setDomain("");
        cookie.setExpiryDate(new Date(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        return cookie;
    }

}
