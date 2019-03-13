package no.nav.foreldrepenger.fpmock2.testmodell.feed;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DødsmeldingOpprettetHendelseContent implements HendelseContent {
    private static final String TYPE = "DOEDSMELDINGOPPRETTET";

    private List<String> identer;
    private LocalDate doedsdato;

    private DødsmeldingOpprettetHendelseContent(Builder builder){
        this.identer = builder.identer;
        this.doedsdato = builder.doedsdato;
    }

    public List<String> getIdenter() {
        return identer;
    }

    public LocalDate getDoedsdato() {
        return doedsdato;
    }

    public void setIdenter(List<String> identer) {
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
        private List<String> identer;
        private LocalDate doedsdato;

        public Builder setIdenter(String fnr, String aktørId){
            identer = new ArrayList<>();
            identer.add(fnr);
            identer.add(aktørId);
            return this;
        }
        public Builder setDoedsdato(LocalDate doedsdato){
            this.doedsdato = doedsdato;
            return this;
        }

        public DødsmeldingOpprettetHendelseContent build() {return new DødsmeldingOpprettetHendelseContent(this);}
    }

}
