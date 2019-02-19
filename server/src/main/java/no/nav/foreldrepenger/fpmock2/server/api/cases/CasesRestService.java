package no.nav.foreldrepenger.fpmock2.server.api.cases;


import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_FEDREKVOTE;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_FELLESPERIODE;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE;

import java.time.LocalDate;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.base.FpsakTestBase;
import no.nav.foreldrepenger.autotest.base.TestScenarioTestBase;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.Fordeling;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.LukketPeriodeMedVedlegg;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.ObjectFactory;


@Api(tags = {"Cases"})
@Path("/api/cases")
public class CasesRestService extends FpsakTestBase {

    public CasesRestService() {
        setUp();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Mor og far søker etter fødselen, kant til kant.", notes = ("sender et case til FPSAK"))
    @Path("/mor-og-far-søker-etter-fødsel-kant-til-kant")
    public Response testcase_morOgFarSøkerEtterFødsel_kantTilKantsøknad(@Context UriInfo uriInfo) {

        try {
            TestscenarioDto testscenario = opprettScenario("82");

            String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
            LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
            LocalDate fpStartdatoMor = fødselsdato.minusWeeks(3);

            ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(morAktørId, fødselsdato);
            fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
            long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
            List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoMor);
            fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, testscenario, saksnummerMor);
            saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
            saksbehandler.hentFagsak(saksnummerMor);
            saksbehandler.velgBehandling("Førstegangsbehandling");
            saksbehandler.ventTilBehandlingsstatus("AVSLU");

            String farAktørId = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
            String farFnr = testscenario.getPersonopplysninger().getAnnenpartIdent();
            LocalDate fpStartDatoFar = fødselsdato.plusWeeks(10).plusDays(1);

            Fordeling fordeling = new ObjectFactory().createFordeling();
            fordeling.setAnnenForelderErInformert(true);
            List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
            perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FEDREKVOTE, fpStartDatoFar, fpStartDatoFar.plusWeeks(6).minusDays(1)));
            perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FELLESPERIODE, fpStartDatoFar.plusWeeks(6), fpStartDatoFar.plusWeeks(10)));

            ForeldrepengesoknadBuilder søknadFar = foreldrepengeSøknadErketyper.fodselfunnetstedFarMedMor(farAktørId, morAktørId, fødselsdato, LocalDate.now(), fordeling);
            fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);

            long saksnummerFar = fordel.sendInnSøknad(søknadFar.build(), farAktørId, farFnr, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
            List<InntektsmeldingBuilder> inntektsmeldingerFar = makeInntektsmeldingFromTestscenarioMedIdent(testscenario, farFnr, fpStartDatoFar, true);
            fordel.sendInnInntektsmeldinger(inntektsmeldingerFar, farAktørId, farFnr, saksnummerFar);
            saksbehandler.hentFagsak(saksnummerFar);
            saksbehandler.velgBehandling("Førstegangsbehandling");
            return Response.ok(saksnummerMor).build();
        } catch (Exception e) {
            String message = "Error: " + e.toString();
            return Response.ok(message).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Mor er tom for dager.", notes = ("sender et case til FPSAK"))
    @Path("/mor-er-tom-for-dager")
    public Response testcase_mor_tom_for_dager(@Context UriInfo uriInfo) {
        try {
            TestscenarioDto testscenario = opprettScenario("50");

            String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
            LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
            LocalDate fpStartdatoMor = fødselsdato.minusWeeks(3);

            Fordeling fordeling = new ObjectFactory().createFordeling();
            fordeling.setAnnenForelderErInformert(true);
            List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
            perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpStartdatoMor, fødselsdato.minusDays(1)));
            perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato, fødselsdato.plusWeeks(6).minusDays(1)));
            perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FELLESPERIODE, fødselsdato.plusWeeks(6), fødselsdato.plusWeeks(23).minusDays(1)));
            perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato.plusWeeks(23), fødselsdato.plusWeeks(33).minusDays(1)));
            perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FELLESPERIODE, fødselsdato.plusWeeks(33), fødselsdato.plusWeeks(34).minusDays(1)));

            ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(morAktørId, fordeling, fødselsdato);
            fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
            long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
            List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoMor);
            fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, testscenario, saksnummerMor);
            return Response.ok(saksnummerMor).build();
        } catch (Exception e) {
            String message = "Error: " + e.toString();
            return Response.ok(message).build();
        }
    }


}
