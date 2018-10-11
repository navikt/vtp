package no.nav.foreldrepenger.fpmock2.testmodell.repo;

import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.fpmock2.testmodell.util.VariabelContainer;

public interface Testscenario {

    String getTemplateNavn();

    String getId();

    Personopplysninger getPersonopplysninger();

    VariabelContainer getVariabelContainer();

    InntektYtelseModell getSøkerInntektYtelse();

    InntektYtelseModell getAnnenpartInntektYtelse();

}
