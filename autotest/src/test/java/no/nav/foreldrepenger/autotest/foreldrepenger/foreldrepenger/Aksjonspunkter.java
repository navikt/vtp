package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.base.ForeldrepengerTestBase;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold.Arbeidsforhold;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugLoggBehandling;

@Tag("util")
public class Aksjonspunkter  extends ForeldrepengerTestBase {

    @Test
    @DisplayName("Midlertidig test for å sjekke aksjonspunkter")
    public void stoppAksjonspunktFoedsel() throws Exception {

        TestscenarioDto testscenario = opprettScenario("160");

        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();

        Arbeidsforhold arbeidsforhold_1 = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0);
        Arbeidsforhold arbeidsforhold_2 = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(1);
        String arbeidsgiverOrgnr_1 = arbeidsforhold_1.getArbeidsgiverOrgnr();
        String arbeidsgiverOrgnr_2 = arbeidsforhold_2.getArbeidsgiverOrgnr();

        List<Integer> inntekter = sorterteInntektsbeløp(testscenario);


        InntektsmeldingBuilder inntektsmeldingBuilder_1 = lagInntektsmeldingBuilder(inntekter.get(0), fnr,
                fpStartdato, arbeidsgiverOrgnr_1, Optional.of(arbeidsforhold_1.getArbeidsforholdId()), Optional.empty());
        InntektsmeldingBuilder inntektsmeldingBuilder_2 = lagInntektsmeldingBuilder(inntekter.get(1), fnr,
                fpStartdato, arbeidsgiverOrgnr_2, Optional.of(arbeidsforhold_2.getArbeidsforholdId()), Optional.empty());

        inntektsmeldingBuilder_1.setArbeidsforhold(InntektsmeldingBuilder.createArbeidsforhold(
                inntektsmeldingBuilder_1.getArbeidsforhold().getArbeidsforholdId().getValue(),
                null,
                new BigDecimal(101230),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()));

        fordel.sendInnInntektsmeldinger(Arrays.asList(inntektsmeldingBuilder_1, inntektsmeldingBuilder_2), testscenario, saksnummer);

        debugLoggBehandling(saksbehandler.valgtBehandling);

        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilØkonomioppdragFerdigstilles();

        debugLoggBehandling(saksbehandler.valgtBehandling);



    }

}
