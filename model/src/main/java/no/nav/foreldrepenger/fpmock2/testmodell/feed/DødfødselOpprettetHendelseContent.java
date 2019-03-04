package no.nav.foreldrepenger.fpmock2.testmodell.feed;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DødfødselOpprettetHendelseContent implements HendelseContent {
    private static final String TYPE = "DOEDFOEDSELOPPRETTET";

    private List<String> personIdenter;
    private LocalDate doedfoedselsdato;

    private DødfødselOpprettetHendelseContent(DødfødselOpprettetHendelseContent.Builder builder) {
        this.personIdenter = builder.personIdenter;
        this.doedfoedselsdato = builder.doedfoedselsdato;
    }

    public List<String> getPersonIdenter() {
        return personIdenter;
    }

    public LocalDate getDoedfoedselsdato() {
        return doedfoedselsdato;
    }

    public void setPersonIdenter(List<String> personIdenter) {
        this.personIdenter = personIdenter;
    }

    public void setDoedfoedselsdato(LocalDate doedfoedselsdato) {
        this.doedfoedselsdato = doedfoedselsdato;
    }

    @Override
    public String hentType() {
        return TYPE;
    }

    public static class Builder {
        private List<String> personIdenter;
        private LocalDate doedfoedselsdato;

        public DødfødselOpprettetHendelseContent.Builder setPersonIdenter(String fnr, String aktørId) {
            personIdenter = new ArrayList<>();
            personIdenter.add(fnr);
            personIdenter.add(aktørId);
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
