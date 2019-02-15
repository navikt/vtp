package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import java.time.LocalDate;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Tag("smoke")
@Tag("foreldrepenger")
public class Ytelser extends ForeldrepengerTestBase {

    @Test
    public void morSøkerFødselMottarSykepenger() throws Exception {
        TestscenarioDto testscenario = opprettScenario("70");
        
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate startDatoForeldrepenger = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
    }
    
    @Test
    public void morSøkerFødselMottarForLite() throws Exception {
        TestscenarioDto testscenario = opprettScenario("71");
    }
    
    @Test
    public void morSøkerFødselMottarSykepengerOgInntekter() throws Exception {
        TestscenarioDto testscenario = opprettScenario("72");
    }
    
    @Test
    public void morSøkerFødselMilitærtjenesteGodkjent() throws Exception {
        TestscenarioDto testscenario = opprettScenario("73");
    }
    
    @Test
    public void morSøkerFødselMilitærtjenesteAvvist() throws Exception {
        TestscenarioDto testscenario = opprettScenario("70");
    }
}
