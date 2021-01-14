package no.nav.foreldrepenger.vtp.testmodell.jackson;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.testmodell.felles.Orgnummer;
import no.nav.foreldrepenger.vtp.testmodell.felles.Prosent;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.Arbeidsforhold;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.Arbeidskategori;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.ArbeidskategoriKode;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.Behandlingstema;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.BehandlingstemaKode;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.Grunnlag;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.Inntektsperiode;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.InntektsperiodeKode;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.Periode;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.Status;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.StatusKode;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.TRexModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.Tema;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.TemaKode;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.Vedtak;

class TrexModellSeraliseringDeseralseringsTest extends TestscenarioSerializationTestBase {

    @Test
    public void OrgnummerSeraliseringDeseraliseringTest() {
        test(new Orgnummer("90802020"));
    }

    @Test
    public void ProsentSeraliseringDeseraliseringTest() {
        test(new Prosent(90));
    }

    @Test
    public void StatusSeraliseringDeseraliseringTest() {
        test(new Status(StatusKode.A, "termnavn"));
    }

    @Test
    public void TemaSeraliseringDeseraliseringTest() {
        test(new Tema(TemaKode.FA, "termnavn"));
    }

    @Test
    public void ArbeidskategoriSeraliseringDeseraliseringTest() {
        test(new Arbeidskategori(ArbeidskategoriKode.K02, "termnavn"));
    }

    @Test
    public void InntektsperiodeSeraliseringDeseraliseringTest() {
        test(new Inntektsperiode(InntektsperiodeKode.M, "termnavn"));
    }

    @Test
    public void ArbeidsforholdSeraliseringDeseraliseringTest() {
        test(new Arbeidsforhold(new Orgnummer("90807060"), 520_000, new Inntektsperiode(InntektsperiodeKode.M, "termnavn"), false));
    }

    @Test
    public void PeriodeSeraliseringDeseraliseringTest() {
        test(new Periode(LocalDate.now(), LocalDate.now().plusDays(5)));
    }

    @Test
    public void BehandlingstemaSeraliseringDeseraliseringTest() {
        test(new Behandlingstema(BehandlingstemaKode.AP, "termnavn"));
    }

    @Test
    public void VedtakSeraliseringDeseraliseringTest() {
        test(new Vedtak(new Periode(LocalDate.now(), LocalDate.now().plusDays(5)), 100));
    }

    @Test
    public void GrunnlagSeraliseringDeseraliseringTest() {
        test(lagGrunnlag());
    }


    @Test
    public void TRexModellSeraliseringDeseraliseringTest() {
        test(lagTRexModell());
    }

    protected TRexModell lagTRexModell() {
        return new TRexModell(
                List.of(lagGrunnlag(), lagGrunnlag()),
                List.of(lagGrunnlag(), lagGrunnlag()),
                null,
                null
        );
    }


    private Grunnlag lagGrunnlag() {
        return new Grunnlag(
                new Status(StatusKode.A, "termnavn"),
                new Tema(TemaKode.FA, "termnavn"),
                new Prosent(100),
                LocalDate.now(),
                new Arbeidskategori(ArbeidskategoriKode.K02, "termnavn"),
                List.of(new Arbeidsforhold(new Orgnummer("90807060"), 520_000, new Inntektsperiode(InntektsperiodeKode.M, "termnavn"), false)),
                new Periode(LocalDate.now(), LocalDate.now().plusDays(5)),
                new Behandlingstema(BehandlingstemaKode.AP, "termnavn"),
                LocalDate.now(),
                LocalDate.now(),
                LocalDate.now(),
                50,
                LocalDate.now(),
                LocalDate.now(),
                "S123567",
                List.of(new Vedtak(new Periode(LocalDate.now(), LocalDate.now().plusDays(5)), 100)));
    }
}
