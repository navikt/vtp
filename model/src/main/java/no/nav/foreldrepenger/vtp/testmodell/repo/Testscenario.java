package no.nav.foreldrepenger.vtp.testmodell.repo;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.vtp.testmodell.util.VariabelContainer;

public interface Testscenario {

    String getTemplateNavn();

    String getId();

    Personopplysninger getPersonopplysninger();

    VariabelContainer getVariabelContainer();

    InntektYtelseModell getSøkerInntektYtelse();

    InntektYtelseModell getAnnenpartInntektYtelse();

}
