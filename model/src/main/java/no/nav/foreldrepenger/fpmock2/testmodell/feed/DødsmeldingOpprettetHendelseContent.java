package no.nav.foreldrepenger.fpmock2.testmodell.feed;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DødsmeldingOpprettetHendelseContent implements HendelseContent {
    private static final String TYPE = "DOEDSMELDINGOPPRETTET";

    private List<String> personIdenter;
    private LocalDate doedsdato;

    private DødsmeldingOpprettetHendelseContent(Builder builder){
        this.personIdenter = builder.personIdenter;
        this.doedsdato = builder.doedsdato;
    }

    public List<String> getPersonIdenter() {
        return personIdenter;
    }

    public LocalDate getDoedsdato() {
        return doedsdato;
    }

    public void setPersonIdenter(List<String> personIdenter) {
        this.personIdenter = personIdenter;
    }


    public void setDoedsdato(LocalDate doedsdato) {
        this.doedsdato = doedsdato;
    }


    @Override
    public String hentType() {
        return TYPE;
    }


    public static class Builder{
        private List<String> personIdenter;
        private LocalDate doedsdato;

        public Builder setPersonIdenter(String fnr, String aktørId){
            personIdenter = new ArrayList<>();
            personIdenter.add(fnr);
            personIdenter.add(aktørId);
            return this;
        }
        public Builder setDoedsdato(LocalDate doedsdato){
            this.doedsdato = doedsdato;
            return this;
        }

        public DødsmeldingOpprettetHendelseContent build() {return new DødsmeldingOpprettetHendelseContent(this);}
    }

}
