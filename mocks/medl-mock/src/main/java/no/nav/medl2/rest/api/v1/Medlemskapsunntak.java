package no.nav.medl2.rest.api.v1;

import java.time.LocalDate;

public record Medlemskapsunntak(Long unntakId,
                                LocalDate fraOgMed,
                                LocalDate tilOgMed,
                                String dekning,
                                String grunnlag,
                                String lovvalg,
                                String lovvalgsland,
                                Boolean helsedel,
                                Boolean medlem,
                                Sporingsinformasjon sporingsinformasjon,
                                Studieinformasjon studieinformasjon) {


    public String getKilde() {
        return sporingsinformasjon != null ? sporingsinformasjon.kilde() : null;
    }

    public LocalDate getBesluttet() {
        return sporingsinformasjon != null ? sporingsinformasjon.besluttet() : null;
    }

    public String getStudieland() {
        return studieinformasjon != null ? studieinformasjon.studieland() : null;
    }

    record Sporingsinformasjon( LocalDate besluttet, String kilde) { }

    record Studieinformasjon(String studieland) { }

}
