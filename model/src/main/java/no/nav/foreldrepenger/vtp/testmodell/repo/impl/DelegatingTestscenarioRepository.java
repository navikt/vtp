package no.nav.foreldrepenger.vtp.testmodell.repo.impl;

import java.util.Map;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModeller;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonArbeidsgiver;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.vtp.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;

/** Delegerer alle kall til en 'reell' implementasjon. Denne kan således endres under kjøring. */
public class DelegatingTestscenarioRepository extends DelegatingTestscenarioBuilderRepository implements TestscenarioRepository {

    private final TestscenarioRepository delegate;

    public DelegatingTestscenarioRepository(TestscenarioRepository delegate) {
        super(delegate);
        this.delegate = delegate;
    }

    @Override
    public Map<String, Testscenario> getTestscenarios() {
        return delegate.getTestscenarios();
    }

    @Override
    public Testscenario getTestscenario(String id) {
        return delegate.getTestscenario(id);
    }

    @Override
    public Testscenario opprettTestscenario(String testscenarioJson, Map<String, String> userSuppliedVariables) {
        return delegate.opprettTestscenario(testscenarioJson, userSuppliedVariables);
    }

    @Override
    public TestscenarioImpl oppdaterTestscenario(String id, String testscenarioJson, Map<String, String> variables) {
        return delegate.oppdaterTestscenario(id, testscenarioJson, variables);
    }

    @Override
    public TestscenarioImpl opprettTestscenario(Personopplysninger personopplysninger,
                                                InntektYtelseModell inntektytelseSøker,
                                                InntektYtelseModell inntektytelseAnnenpart,
                                                OrganisasjonModeller organisasjonsmodeller,
                                                PersonArbeidsgiver privatArbeidsgiver) {
        return delegate.opprettTestscenario(personopplysninger, inntektytelseSøker, inntektytelseAnnenpart, organisasjonsmodeller, privatArbeidsgiver);
    }

    @Override
    public TestscenarioImpl opprettTestscenario(Personopplysninger personopplysninger,
                                                InntektYtelseModell inntektytelseSøker,
                                                InntektYtelseModell inntektytelseAnnenpart,
                                                OrganisasjonModeller organisasjonsmodeller) {
        return delegate.opprettTestscenario(personopplysninger, inntektytelseSøker, inntektytelseAnnenpart, organisasjonsmodeller);
    }

    @Override
    public Boolean slettScenario(String id) {
        return delegate.slettScenario(id);
    }
}
