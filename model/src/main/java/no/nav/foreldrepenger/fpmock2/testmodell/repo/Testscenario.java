package no.nav.foreldrepenger.fpmock2.testmodell.repo;

import no.nav.foreldrepenger.fpmock2.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelse;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.AdresseIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.fpmock2.testmodell.virksomhet.ScenarioVirksomheter;

public interface Testscenario {

    String getTemplateNavn();

    String getUnikScenarioId();

    LokalIdentIndeks getIdenter();

    AdresseIndeks getAdresseIndeks();

    Personopplysninger getPersonopplysninger();

    InntektYtelse getInntektYtelse();

    ScenarioVirksomheter getVirksomheter();

}