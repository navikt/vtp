package no.nav.foreldrepenger.vtp.server.auth.rest;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Issuers {
    ENTRA_ID("http://vtp/rest/AzureAd"),
    AZUREAD("http://vtp/rest/AzureAd"),
    IDPORTEN("http://vtp:8060/rest/idporten"),
    MASKINPORTEN("http://vtp/rest/maskinporten"),
    TOKENX("http://vtp:8060/rest/tokenx");

    private final String issuer;

    Issuers(String issuer) {
        this.issuer = issuer;
    }

    public String getIssuer() {
        return issuer;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
