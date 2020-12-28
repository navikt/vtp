package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent;

import java.time.LocalDate;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonArbeidsgiver;

public record FrilansArbeidsforholdsperiode(LocalDate frilansFom,
                                            LocalDate frilansTom,
                                            String orgnr,
                                            Integer stillingsprosent,
                                            String aktorId,
                                            PersonArbeidsgiver arbeidsgiver) {

    private static final String FRILANS_ARBEIDSFORHOLDSTYPE = "frilanserOppdragstakerHonorarPersonerMm";

    public String getArbeidsforholdstype() {
        return FRILANS_ARBEIDSFORHOLDSTYPE;
    }
}
