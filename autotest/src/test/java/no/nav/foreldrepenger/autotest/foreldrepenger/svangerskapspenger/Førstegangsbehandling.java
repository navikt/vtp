package no.nav.foreldrepenger.autotest.foreldrepenger.svangerskapspenger;

import java.time.LocalDate;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.base.SvangerskapspengerTestBase;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.MedlemskapErketyper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.SvangerskapspengerYtelseErketyper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Tag("develop") //TODO (OL): Gjør til fpsak når klar
@Tag("svangerskapspenger")
public class Førstegangsbehandling extends SvangerskapspengerTestBase {

    @Test
    public void morSøkerSvangerskapspengerHelTilretteleggingFireUkerFørTermin() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");

        String fnrMor = testscenario.getPersonopplysninger().getSøkerIdent();

        ForeldrepengesoknadBuilder soknad = ForeldrepengesoknadBuilder.startBuilding()
                .withAndreVedlegg(null)
                .withSvangerskapspengeYtelse(
                        SvangerskapspengerYtelseErketyper.svangerskapspengerMedOpptjening(
                                LocalDate.now().plusWeeks(4),
                                MedlemskapErketyper.medlemskapNorge(),
                                null,
                                 null
                        ));

        long saksnummer = fordel.sendInnSøknad(soknad.build(), testscenario, DokumenttypeId.SØKNAD_SVANGERSKAPSPENGER);

        // Inntektsmelding
        // XSD feiler på svangerskapspenger.


        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);












    }


}
