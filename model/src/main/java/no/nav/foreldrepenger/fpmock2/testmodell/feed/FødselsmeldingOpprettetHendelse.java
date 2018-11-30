package no.nav.foreldrepenger.fpmock2.testmodell.feed;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FødselsmeldingOpprettetHendelse extends PersonHendelse {
    private static final String TYPE = "FOEDSELSMELDINGOPPRETTET";

    private List<String> personIdenterBarn;
    private List<String> personIdenterMor;
    private List<String> personIdenterFar;
    private LocalDate foedselsdato;

    private FødselsmeldingOpprettetHendelse(Builder builder){
        this.personIdenterBarn = builder.personIdenterBarn;
        this.personIdenterMor = builder.personIdenterMor;
        this.personIdenterFar = builder.personIdenterFar;
        this.foedselsdato = builder.foedselsdato;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public List<String> getPersonIdenterBarn() {
        return personIdenterBarn;
    }

    public List<String> getPersonIdenterMor() {
        return personIdenterMor;
    }

    public List<String> getPersonIdenterFar() {
        return personIdenterFar;
    }

    public LocalDate getFoedselsdato() {
        return foedselsdato;
    }

    public static class Builder{
        private List<String> personIdenterBarn;
        private List<String> personIdenterMor;
        private List<String> personIdenterFar;
        private LocalDate foedselsdato;


        public Builder setPersonIdenterBarn(String fnr, String aktørId){
            personIdenterBarn = new ArrayList<>();
            personIdenterBarn.add(fnr);
            personIdenterBarn.add(aktørId);
            return this;
        }

        public Builder setPersonIdenterMor(String fnr, String aktørId){
            personIdenterMor = new ArrayList<>();
            personIdenterMor.add(fnr);
            personIdenterMor.add(aktørId);
            return this;
        }

        public Builder setPersonIdenterFar(String fnr, String aktørId){
            personIdenterFar = new ArrayList<>();
            personIdenterFar.add(fnr);
            personIdenterFar.add(aktørId);
            return this;
        }

        public Builder setFoedselsdato(LocalDate foedselsdato){
            this.foedselsdato = foedselsdato;
            return this;
        }

        public FødselsmeldingOpprettetHendelse build(){
            return new FødselsmeldingOpprettetHendelse(this);
        }



    }

}
