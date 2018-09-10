package no.nav.foreldrepenger.fpmock2.testmodell.repo;

import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelse;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.fpmock2.testmodell.util.VariabelContainer;

public interface Testscenario {

    String getTemplateNavn();

    String getId();

    Personopplysninger getPersonopplysninger();

    InntektYtelse getInntektYtelse();

    VariabelContainer getVariabelContainer();

}