package no.nav.foreldrepenger.autotest.tests;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl;
import no.nav.vedtak.felles.xml.soeknad.v1.Soeknad;

public class ProofOfConceptTest extends FpsakTestBase{

    @Test
    public void foreldrepengesøknadTermindatoKunMor() throws Exception {
        TestscenarioImpl testscenario = testscenarioRepository.opprettTestscenario(TestscenarioTemplateRepositoryImpl.getInstance().finn("50"));
        Soeknad søknad = foreldrepengeSøknadErketyper.termindatoUttakKunMor(testscenario.getPersonopplysninger().getSøker().getAktørIdent());
        
        fordel.erLoggetInnUtenRolle();
        fordel.sendInnSøknad(søknad, testscenario);
    }


    @Test
    public void engangsttønadFødselFunnetStedKunMor() throws Exception {
        TestscenarioImpl testscenario = testscenarioRepository.opprettTestscenario(TestscenarioTemplateRepositoryImpl.getInstance().finn("50"));
        Soeknad søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEngangstonad(testscenario.getPersonopplysninger().getSøker().getAktørIdent());

        fordel.erLoggetInnUtenRolle();
        fordel.sendInnSøknad(søknad, testscenario);
    }
}
