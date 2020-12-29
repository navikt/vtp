package no.nav.foreldrepenger.vtp.kontrakter;

import java.time.LocalDate;
import java.util.List;

public record TestscenarioPersonopplysningDto(
        String søkerIdent,
        String søkerAktørIdent,
        String annenpartIdent,
        String annenpartAktørIdent,
        LocalDate fødselsdato,
        List<String> barnIdenter) {
}
