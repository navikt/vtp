package no.nav.foreldrepenger.autotest.foreldrepenger.svangerskapspenger;

import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.SoekerErketyper.morSoeker;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.base.SvangerskapspengerTestBase;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ArbeidsforholdErketyper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.MedlemskapErketyper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.OpptjeningErketyper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.SvangerskapspengerYtelseErketyper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.TilretteleggingsErketyper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.Virksomhet;

@Tag("develop") //TODO (OL): Gjør til fpsak når klar
@Tag("svangerskapspenger")
public class Førstegangsbehandling extends SvangerskapspengerTestBase {

    @Test
    public void morSøkerSvangerskapspengerHelTilretteleggingFireUkerFørTermin() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");
        String morAktoerId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String fnrMor = testscenario.getPersonopplysninger().getSøkerIdent();
        String orgNrMor = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        Virksomhet morVirksomhet = ArbeidsforholdErketyper.virksomhet(orgNrMor);

        ForeldrepengesoknadBuilder soknad = ForeldrepengesoknadBuilder.startBuilding()
                .withSvangerskapspengeYtelse(
                        SvangerskapspengerYtelseErketyper.svangerskapspengerMedOpptjening(
                                LocalDate.now().plusWeeks(4),
                                MedlemskapErketyper.medlemskapNorge(),
                                OpptjeningErketyper.medEgenNaeringOpptjening(),
                           Arrays.asList(TilretteleggingsErketyper.helTilrettelegging(LocalDate.now(),LocalDate.now().plusWeeks(1),morVirksomhet))
                        ))
                .withSoeker(morSoeker(morAktoerId))
                ;
        soknad.build();

        long saksnummer = fordel.sendInnSøknad(soknad.build(), testscenario, DokumenttypeId.SØKNAD_SVANGERSKAPSPENGER);

        // Inntektsmelding
        // XSD feiler på svangerskapspenger.


        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);




    }


}
