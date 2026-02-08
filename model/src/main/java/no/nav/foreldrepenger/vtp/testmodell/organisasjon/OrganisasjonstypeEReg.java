package no.nav.foreldrepenger.vtp.testmodell.organisasjon;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrganisasjonstypeEReg {
    JURIDISK_ENHET("JuridiskEnhet"),
    VIRKSOMHET("Virksomhet"),
    ORGLEDD("Organisasjonsledd");


    @JsonValue
    private final String kode;

    public String getKode() {
        return this.kode;
    }

    private OrganisasjonstypeEReg(String kode) {
        this.kode = kode;
    }
}
