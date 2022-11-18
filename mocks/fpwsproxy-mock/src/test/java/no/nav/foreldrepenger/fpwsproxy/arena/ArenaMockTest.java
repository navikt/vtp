package no.nav.foreldrepenger.fpwsproxy.arena;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.kontrakter.arena.request.ArenaRequestDto;
import no.nav.foreldrepenger.vtp.testmodell.TestscenarioHenter;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.ArenaMeldekort;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.ArenaSak;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.ArenaVedtak;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.RelatertYtelseTema;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.SakStatus;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.VedtakStatus;
import no.nav.foreldrepenger.vtp.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.DelegatingTestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;

class ArenaMockTest {

    private static Testscenario testscenario;


    @BeforeAll
    public static void setup() {
        var testScenarioRepository = new DelegatingTestscenarioRepository(TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance()));
        var testscenarioHenter = TestscenarioHenter.getInstance();
        var testscenarioObjekt = testscenarioHenter.hentScenario("1");
        var testscenarioJson = testscenarioObjekt == null ? "{}" : testscenarioHenter.toJson(testscenarioObjekt);
        testscenario = testScenarioRepository.opprettTestscenario(testscenarioJson, Collections.emptyMap());


    }

    @Test
    void testerAtMappingAvScenarioMedAAPProdusereRiktigAntallMeldekort() {
        var arenaReqeustDto = new ArenaRequestDto(testscenario.getPersonopplysninger().getSøker().getIdent(), LocalDate.now().minusYears(3), LocalDate.now().plusYears(3));
        var meldekortUtbetalingsgrunnlagSakDtos = ArenaSakerTilMeldekortMapper.tilMeldekortUtbetalingsgrunnlagSakDto(arenaReqeustDto, testscenario.getSøkerInntektYtelse().arenaModell().saker());
        assertThat(meldekortUtbetalingsgrunnlagSakDtos)
                .hasSameSizeAs(testscenario.getSøkerInntektYtelse().arenaModell().saker())
                .hasSize(1);
    }


    @Test
    void toVedtakPåSammeSakMappesTilToMeldekort () {
        var arenaReqeustDto = new ArenaRequestDto("12345678910", LocalDate.now().minusYears(3), LocalDate.now().plusYears(3));
        var arenaSaker = List.of(
                new ArenaSak("123456789", RelatertYtelseTema.DAG, SakStatus.AKTIV, List.of(lagArenaVedtak(), lagArenaVedtak()))
        );
        var meldekortUtbetalingsgrunnlagSakDtos = ArenaSakerTilMeldekortMapper.tilMeldekortUtbetalingsgrunnlagSakDto(arenaReqeustDto, arenaSaker);
        assertThat(meldekortUtbetalingsgrunnlagSakDtos).hasSize(2);
    }

    @Test
    void toArenaSakerMenBareEnErAAPEllerDAG() {
        var arenaReqeustDto = new ArenaRequestDto("12345678910", LocalDate.now().minusYears(3), LocalDate.now().plusYears(3));
        var arenaSaker = List.of(
                new ArenaSak("123456789", RelatertYtelseTema.FA, SakStatus.AKTIV, List.of(lagArenaVedtak())),
                new ArenaSak("123456789", RelatertYtelseTema.DAG, SakStatus.AKTIV, List.of(lagArenaVedtak()))
        );
        var meldekortUtbetalingsgrunnlagSakDtos = ArenaSakerTilMeldekortMapper.tilMeldekortUtbetalingsgrunnlagSakDto(arenaReqeustDto, arenaSaker);
        assertThat(meldekortUtbetalingsgrunnlagSakDtos).hasSize(1);
    }


    @Test
    void ingenVedtakPåSakGirTomListeOgSkalIkkeFeile() {
        var arenaReqeustDto = new ArenaRequestDto("12345678910", LocalDate.now().minusYears(3), LocalDate.now().plusYears(3));
        var arenaSaker = List.of(
                new ArenaSak("123456789", RelatertYtelseTema.DAG, SakStatus.AKTIV, null)
        );
        var meldekortUtbetalingsgrunnlagSakDtos = ArenaSakerTilMeldekortMapper.tilMeldekortUtbetalingsgrunnlagSakDto(arenaReqeustDto, arenaSaker);
        assertThat(meldekortUtbetalingsgrunnlagSakDtos).isEmpty();
    }

    @Test
    void ingenMeldekortPåVedtakReturereMeldekortUtenMeldekortperiode() {
        var arenaReqeustDto = new ArenaRequestDto("12345678910", LocalDate.now().minusYears(3), LocalDate.now().plusYears(3));
        var arenaSaker = List.of(
                new ArenaSak("123456789", RelatertYtelseTema.DAG, SakStatus.AKTIV, List.of(lagArenaVedtakUtenMeldekort()))
        );
        var meldekortUtbetalingsgrunnlagSakDtos = ArenaSakerTilMeldekortMapper.tilMeldekortUtbetalingsgrunnlagSakDto(arenaReqeustDto, arenaSaker);
        assertThat(meldekortUtbetalingsgrunnlagSakDtos).hasSize(1);
        assertThat(meldekortUtbetalingsgrunnlagSakDtos.get(0).meldekortene()).isEmpty();
    }


    private static ArenaVedtak lagArenaVedtakUtenMeldekort() {
        var now = LocalDate.now();
        return new ArenaVedtak(now, now, now.plusYears(3), now, VedtakStatus.GODKJ, BigDecimal.TEN, null);
    }

    private static ArenaVedtak lagArenaVedtak() {
        var now = LocalDate.now();
        return new ArenaVedtak(now, now, now.plusYears(3), now, VedtakStatus.GODKJ, BigDecimal.TEN, List.of(lagMeldekort()));
    }

    private static ArenaMeldekort lagMeldekort() {
        var now = LocalDate.now();
        return new ArenaMeldekort(now, now.plusYears(3), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.valueOf(100));
    }
}
