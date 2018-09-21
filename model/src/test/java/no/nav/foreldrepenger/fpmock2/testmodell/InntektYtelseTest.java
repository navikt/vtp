package no.nav.foreldrepenger.fpmock2.testmodell;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.RelatertYtelseTema;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arena.ArenaMeldekort;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arena.ArenaSak;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arena.ArenaVedtak;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arena.SakStatus;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arena.VedtakStatus;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.InfotrygdModell;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.InfotrygdSakBehandlingtema;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.InfotrygdSakStatus;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.InfotrygdSakType;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.InfotrygdTema;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdArbeidsforhold;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdArbeidskategori;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdBeregningsgrunnlag;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdBeregningsgrunnlagBehandlingtema;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdForeldrepengerBeregningsgrunnlag;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdInntektsperiodeType;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdVedtak;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.ytelse.InfotrygdYtelse;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent.Inntektsperiode;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioTilTemplateMapper;
import no.nav.foreldrepenger.fpmock2.testmodell.util.JsonMapper;

public class InntektYtelseTest {
    private static final JsonMapper jsonMapper =  new JsonMapper();
    
    @Test
    public void skal_skrive_scenario_til_inntektytelse_json() throws Exception {
        TestscenarioRepositoryImpl testScenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
        TestscenarioTilTemplateMapper mapper = new TestscenarioTilTemplateMapper();

        InntektYtelseModell inntektYtelse = new InntektYtelseModell();
        initArenaModell(inntektYtelse);
        initInfotrygdModell(inntektYtelse);
        initInntektskomponentModell(inntektYtelse);
        
        TestscenarioImpl scenario = new TestscenarioImpl("test3", "test3-123", testScenarioRepository);
        scenario.setSøkerInntektYtelse(inntektYtelse);
        
        String json = skrivInntektYtelse(scenario, mapper);
        
        System.out.println(json);
    }

    private void initInntektskomponentModell(InntektYtelseModell inntektYtelse){
        InntektskomponentModell inntektskomponentModell = inntektYtelse.getInntektskomponentModell();
        List<Inntektsperiode> inntektsperioder = lagInntektsperioder();
        inntektskomponentModell.setInntektsperioder(inntektsperioder);
    }

    private List<Inntektsperiode> lagInntektsperioder() {
        List<Inntektsperiode> resultat = new ArrayList<>();
        resultat.add(lagInntektsperiode());
        return resultat;
    }

    private Inntektsperiode lagInntektsperiode() {
        LocalDateTime now = LocalDateTime.now();
        Inntektsperiode inntektsperiode = new Inntektsperiode();
        inntektsperiode.setBeløp(5000);
        inntektsperiode.setFom(now.minusMonths(10));
        inntektsperiode.setTom(now.minusMonths(1));
        return inntektsperiode;
    }

    private void initInfotrygdModell(InntektYtelseModell inntektYtelse) {
        InfotrygdModell infotrygdModell = inntektYtelse.getInfotrygdModell();
        List<InfotrygdBeregningsgrunnlag> infotrygdBeregningsgrunnlag = lagInfotrygdBeregningsgrunnlag();
        infotrygdModell.setGrunnlag(infotrygdBeregningsgrunnlag);
        infotrygdModell.setYtelser(lagInfotrygdYtelser());
    }

    private List<InfotrygdYtelse> lagInfotrygdYtelser() {
        List<InfotrygdYtelse> resultat = new ArrayList<>();
        resultat.add(lagInfotrygdYtelse());
        return resultat;
    }

    private InfotrygdYtelse lagInfotrygdYtelse() {
        LocalDateTime now = LocalDateTime.now();
        InfotrygdYtelse yt = new InfotrygdYtelse();
        yt.setBehandlingTema(InfotrygdSakBehandlingtema.FORELDREPENGER_FØDSEL);
        yt.setTema(InfotrygdTema.FA);
        yt.setRegistrert(now);
        yt.setIverksatt(now);
        yt.setEndret(now);
        yt.setOpphør(now.plusDays(100));
        yt.setSakId("9999");
        yt.setSaksbehandlerId("helloworld");
        yt.setSakStatus(InfotrygdSakStatus.FERDIG_IVERKSATT);
        yt.setResultat(null);
        yt.setSakType(InfotrygdSakType.SØKNAD);
        return yt;
    }

    private void initArenaModell(InntektYtelseModell inntektYtelse) {
        ArenaSak arenaSak = lagArenaSak();
        inntektYtelse.getArenaModell().leggTil(arenaSak);
    }

    private List<InfotrygdBeregningsgrunnlag> lagInfotrygdBeregningsgrunnlag() {
        List<InfotrygdBeregningsgrunnlag> resultat = new ArrayList<>();
        LocalDate today = LocalDate.now();
        
        InfotrygdForeldrepengerBeregningsgrunnlag fbg = new InfotrygdForeldrepengerBeregningsgrunnlag();
        fbg.setBehandlingTema(InfotrygdBeregningsgrunnlagBehandlingtema.FORELDREPENGER_FØDSEL);
        fbg.setFom(today);
        fbg.setDekningsgrad(100);
        fbg.setFødselsdatoBarn(today);
        fbg.setGradering(100);
        fbg.setStartdato(today);
        fbg.setArbeidskategori(InfotrygdArbeidskategori.ARBEIDSTAKER);
        
        InfotrygdArbeidsforhold arbeidsforhold = new InfotrygdArbeidsforhold("1");
        arbeidsforhold.setBeløp(new BigDecimal(10000));
        arbeidsforhold.setInntektsPeriodeType(InfotrygdInntektsperiodeType.MÅNEDLIG);
        fbg.setArbeidsforhold(arbeidsforhold);
        
        InfotrygdVedtak vedtak = new InfotrygdVedtak();
        vedtak.setFom(today);
        vedtak.setUtbetalingsgrad(100);
        fbg.setVedtak(vedtak);
        
        resultat.add(fbg);
        return resultat;
    }

    private ArenaSak lagArenaSak() {
        ArenaSak arenaSak = new ArenaSak();
        arenaSak.setSaksnummer("999");
        arenaSak.setStatus(SakStatus.AKTIV);
        arenaSak.setTema(RelatertYtelseTema.AAP);
        
        ArenaVedtak arenaVedtak = new ArenaVedtak();
        arenaVedtak.setStatus(VedtakStatus.IVERK);
        ArenaMeldekort meldekort = new ArenaMeldekort();
        meldekort.setBeløp(BigDecimal.TEN);
        meldekort.setDagsats(BigDecimal.valueOf(10000L));
        meldekort.setUtbetalingsgrad(BigDecimal.ONE);
        
        arenaVedtak.leggTil(meldekort);
        arenaSak.leggTil(arenaVedtak);
        return arenaSak;
    }

    private String skrivInntektYtelse(Testscenario scenario, TestscenarioTilTemplateMapper mapper) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream buf = new BufferedOutputStream(baos);
        mapper.skrivInntektYtelse(jsonMapper.canonicalMapper(), buf, scenario, scenario.getSøkerInntektYtelse());
        buf.flush();
        return baos.toString("UTF8");
    }
}
