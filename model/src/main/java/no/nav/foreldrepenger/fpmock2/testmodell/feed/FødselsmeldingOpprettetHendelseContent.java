package no.nav.foreldrepenger.fpmock2.testmodell.feed;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.nav.tjenester.person.feed.v2.Ident;

public class FødselsmeldingOpprettetHendelseContent implements HendelseContent {
    private static final String TYPE = "FOEDSELSMELDINGOPPRETTET";

    private List<Ident> personIdenterBarn;
    private List<Ident> personIdenterMor;
    private List<Ident> personIdenterFar;
    private LocalDate foedselsdato;

    private FødselsmeldingOpprettetHendelseContent(Builder builder){
        this.personIdenterBarn = builder.personIdenterBarn;
        this.personIdenterMor = builder.personIdenterMor;
        this.personIdenterFar = builder.personIdenterFar;
        this.foedselsdato = builder.foedselsdato;
    }

    @Override
    public String hentType() {
        return TYPE;
    }

    public List<Ident> getPersonIdenterBarn() {
        return personIdenterBarn;
    }

    public List<Ident> getPersonIdenterMor() {
        return personIdenterMor;
    }

    public List<Ident> getPersonIdenterFar() {
        return personIdenterFar;
    }

    public LocalDate getFoedselsdato() {
        return foedselsdato;
    }

    public static class Builder{
        private List<Ident> personIdenterBarn;
        private List<Ident> personIdenterMor;
        private List<Ident> personIdenterFar;
        private LocalDate foedselsdato;


        public Builder setPersonIdenterBarn(String fnr, String aktørId) {
            personIdenterBarn = new ArrayList<>();
            personIdenterBarn.add(new Ident(fnr, FNR_IDENT_TYPE));
            personIdenterBarn.add(new Ident(aktørId, AKTØR_ID_IDENT_TYPE));
            return this;
        }

        public Builder setPersonIdenterMor(String fnr, String aktørId) {
            personIdenterMor = new ArrayList<>();
            personIdenterMor.add(new Ident(fnr, FNR_IDENT_TYPE));
            personIdenterMor.add(new Ident(aktørId, AKTØR_ID_IDENT_TYPE));
            return this;
        }

        public Builder setPersonIdenterFar(String fnr, String aktørId) {
            personIdenterFar = new ArrayList<>();
            personIdenterFar.add(new Ident(fnr, FNR_IDENT_TYPE));
            personIdenterFar.add(new Ident(aktørId, AKTØR_ID_IDENT_TYPE));
            return this;
        }

        public Builder setFoedselsdato(LocalDate foedselsdato){
            this.foedselsdato = foedselsdato;
            return this;
        }

        public FødselsmeldingOpprettetHendelseContent build(){
            return new FødselsmeldingOpprettetHendelseContent(this);
        }
    }
}
