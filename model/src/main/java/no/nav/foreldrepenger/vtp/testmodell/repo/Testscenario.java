package no.nav.foreldrepenger.vtp.testmodell.repo;

import no.nav.foreldrepenger.vtp.autotest.testscenario.util.VariabelContainer;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;

public interface Testscenario {

    String getTemplateNavn();

    String getId();

    Personopplysninger getPersonopplysninger();

    VariabelContainer getVariabelContainer();

    InntektYtelseModell getSøkerInntektYtelse();

    InntektYtelseModell getAnnenpartInntektYtelse();

}
