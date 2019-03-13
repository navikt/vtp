package no.nav.foreldrepenger.fpmock2.testmodell.feed;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DødfødselOpprettetHendelseContent implements HendelseContent {
    private static final String TYPE = "DOEDFOEDSELOPPRETTET";

    private List<String> identer;
    private LocalDate doedfoedselsdato;

    private DødfødselOpprettetHendelseContent(DødfødselOpprettetHendelseContent.Builder builder) {
        this.identer = builder.identer;
        this.doedfoedselsdato = builder.doedfoedselsdato;
    }

    public List<String> getIdenter() {
        return identer;
    }

    public LocalDate getDoedfoedselsdato() {
        return doedfoedselsdato;
    }

    public void setIdenter(List<String> identer) {
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
        private List<String> identer;
        private LocalDate doedfoedselsdato;

        public DødfødselOpprettetHendelseContent.Builder setIdenter(String fnr, String aktørId) {
            identer = new ArrayList<>();
            identer.add(fnr);
            identer.add(aktørId);
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
