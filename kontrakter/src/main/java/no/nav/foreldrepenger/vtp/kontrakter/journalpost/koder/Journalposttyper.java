package no.nav.foreldrepenger.vtp.kontrakter.journalpost.koder;

/*
    Hentet fra: https://modapp.adeo.no/kodeverksklient/viskodeverk/Journalposttyper/1?5
*/

import com.fasterxml.jackson.annotation.JsonValue;

public enum Journalposttyper {
    INNGAAENDE_DOKUMENT("I"),
    NOTAT("N"),
    UTGAAENDE_DOKUMENT("U");

    @JsonValue
    private String kode;

    Journalposttyper(String kode) {
        this.kode = kode;
    }

    public String getKode() {
        return kode;
    }
}
