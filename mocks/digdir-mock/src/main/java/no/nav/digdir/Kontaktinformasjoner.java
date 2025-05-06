package no.nav.digdir;

import java.util.Map;

public record Kontaktinformasjoner(Map<String, Kontaktinformasjon> personer, Map<String, FeilKode> feil) {

    public record Kontaktinformasjon(boolean aktiv,
                                     String epostadresse,
                                     boolean kanVarsles,
                                     String mobiltelefonnummer,
                                     String personident,
                                     boolean reservert,
                                     String spraak) {
        public Kontaktinformasjon(String spraak) {
            this(true, "noreply@nav.no", true, "99999999", null, false, spraak);
        }
    }

    public enum FeilKode {
        person_ikke_funnet,
        skjermet,
        fortrolig_adresse,
        strengt_fortrolig_adresse,
        strengt_fortrolig_utenlandsk_adresse,
        noen_andre
    }
}
