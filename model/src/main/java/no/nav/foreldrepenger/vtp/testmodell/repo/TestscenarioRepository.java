package no.nav.foreldrepenger.vtp.testmodell.repo;

import java.util.Map;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModeller;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonArbeidsgiver;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;

public interface TestscenarioRepository extends TestscenarioBuilderRepository {

    @Override
    Map<String, Testscenario> getTestscenarios();

    @Override
    Testscenario getTestscenario(String id);

    Testscenario opprettTestscenario(String testscenarioJson, Map<String, String> userSuppliedVariables);

    TestscenarioImpl oppdaterTestscenario(String id, String testscenarioJson, Map<String, String> variables);

    TestscenarioImpl opprettTestscenario(Personopplysninger personopplysninger,
                                         InntektYtelseModell inntektytelseSøker,
                                         InntektYtelseModell inntektytelseAnnenpart,
                                         OrganisasjonModeller organisasjonsmodeller);

    TestscenarioImpl opprettTestscenario(Personopplysninger personopplysninger,
                                         InntektYtelseModell inntektytelseSøker,
                                         InntektYtelseModell inntektytelseAnnenpart,
                                         OrganisasjonModeller organisasjonsmodeller,
                                         PersonArbeidsgiver privatArbeidsgiver);

}
