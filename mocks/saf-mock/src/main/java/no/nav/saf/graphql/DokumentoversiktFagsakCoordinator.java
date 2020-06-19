package no.nav.saf.graphql;


import no.nav.saf.Dokumentoversikt;

public interface DokumentoversiktFagsakCoordinator {
    Dokumentoversikt hentDokumentoversikt(String fagsakId, String fagsystem);
}