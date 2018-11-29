package no.nav.foreldrepenger.autotest.klienter.vtp.openam;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.http.impl.cookie.BasicClientCookie;

import no.nav.foreldrepenger.autotest.klienter.vtp.VTPKlient;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.fpmock2.server.rest.OidcTokenGenerator;
import no.nav.modig.testcertificates.TestCertificates;

public class OpenamKlient extends VTPKlient {

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
        String issuer = System.getProperty("isso.oauth2.issuer", "https://localhost:8063/isso/oauth2");
        String token = new OidcTokenGenerator(rolle).withIssuer(issuer).create();

        BasicClientCookie cookie = new BasicClientCookie("ID_token", token);
        cookie.setPath("/");
        cookie.setDomain("");
        cookie.setExpiryDate(new Date(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        session.leggTilCookie(cookie);
    }

}
