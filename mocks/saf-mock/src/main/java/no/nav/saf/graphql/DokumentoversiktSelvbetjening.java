package no.nav.saf.graphql;


import no.nav.saf.selvbetjening.Dokumentoversikt;

public interface DokumentoversiktSelvbetjening {

    Dokumentoversikt hentDokumentoversikt(String fødselsnummer);
}
