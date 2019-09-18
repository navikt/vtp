package no.nav.foreldrepenger.vtp.testmodell.feed;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.nav.tjenester.person.feed.v2.Ident;

public class DødsmeldingOpprettetHendelseContent implements HendelseContent {
    private static final String TYPE = "DOEDSMELDINGOPPRETTET";

    private List<Ident> identer;
    private LocalDate doedsdato;

    private DødsmeldingOpprettetHendelseContent(Builder builder){
        this.identer = builder.identer;
        this.doedsdato = builder.doedsdato;
    }

    public List<Ident> getIdenter() {
        return identer;
    }

    public LocalDate getDoedsdato() {
        return doedsdato;
    }

    public void setIdenter(List<Ident> identer) {
        this.identer = identer;
    }

    public void setDoedsdato(LocalDate doedsdato) {
        this.doedsdato = doedsdato;
    }

    @Override
    public String hentType() {
        return TYPE;
    }

    public static class Builder{
        private List<Ident> identer;
        private LocalDate doedsdato;

        public Builder setIdenter(String fnr, String aktørId){
            identer = new ArrayList<>();
            identer.add(new Ident(fnr, FNR_IDENT_TYPE));
            identer.add(new Ident(aktørId, AKTØR_ID_IDENT_TYPE));
            return this;
        }
        public Builder setDoedsdato(LocalDate doedsdato){
            this.doedsdato = doedsdato;
            return this;
        }

        public DødsmeldingOpprettetHendelseContent build() {return new DødsmeldingOpprettetHendelseContent(this);}
    }

}
