package no.nav.foreldrepenger.vtp.testmodell.feed;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.nav.tjenester.person.feed.v2.Ident;

public class DødfødselOpprettetHendelseContent implements HendelseContent {
    private static final String TYPE = "DOEDFOEDSELOPPRETTET";

    private List<Ident> identer;
    private LocalDate doedfoedselsdato;

    private DødfødselOpprettetHendelseContent(DødfødselOpprettetHendelseContent.Builder builder) {
        this.identer = builder.identer;
        this.doedfoedselsdato = builder.doedfoedselsdato;
    }

    public List<Ident> getIdenter() {
        return identer;
    }

    public LocalDate getDoedfoedselsdato() {
        return doedfoedselsdato;
    }

    public void setIdenter(List<Ident> identer) {
        this.identer = identer;
    }

    public void setDoedfoedselsdato(LocalDate doedfoedselsdato) {
        this.doedfoedselsdato = doedfoedselsdato;
    }

    @Override
    public String hentType() {
        return TYPE;
    }

    public static class Builder {
        private List<Ident> identer;
        private LocalDate doedfoedselsdato;

        public DødfødselOpprettetHendelseContent.Builder setIdenter(String fnr, String aktørId) {
            identer = new ArrayList<>();
            identer.add(new Ident(fnr, FNR_IDENT_TYPE));
            identer.add(new Ident(aktørId, AKTØR_ID_IDENT_TYPE));
            return this;
        }
        public DødfødselOpprettetHendelseContent.Builder setDoedfoedselsdato(LocalDate doedfoedselsdato) {
            this.doedfoedselsdato = doedfoedselsdato;
            return this;
        }

        public DødfødselOpprettetHendelseContent build() {
            return new DødfødselOpprettetHendelseContent(this);
        }
    }
}
