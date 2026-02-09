package no.nav.foreldrepenger.vtp.testmodell.jackson;

import static no.nav.foreldrepenger.vtp.testmodell.Feilkode.PERSON_IKKE_FUNNET;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.ArenaMeldekort;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.ArenaModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.ArenaSak;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.ArenaVedtak;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.RelatertYtelseTema;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.SakStatus;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.VedtakStatus;

class ArenaSeraliseringDeseraliseringsTest extends TestscenarioSerializationTestBase {

    @Test
    void ArenaMeldekortSeraliseringDeseraliseringTest() {
        test(lagArenaMeldekort());
    }

    @Test
    void ArenaVedtakSeraliseringDeseraliseringTest() {
        test(lagArenaVedtak());
    }

    @Test
    void ArenaSakSeraliseringDeseraliseringTest() {
        test(lagArenaSak());
    }

    @Test
    void SakStatusSeraliseringDeseraliseringTest() {
        test(SakStatus.AKTIV);
    }

    @Test
    void VedtakStatusSeraliseringDeseraliseringTest() {
        test(VedtakStatus.IVERK);
    }

    @Test
    void ArenaModellSeraliseringDeseraliseringTest() {
        test(lagArenaModell());
    }

    protected ArenaModell lagArenaModell() {
        return new ArenaModell(PERSON_IKKE_FUNNET, List.of(lagArenaSak()));
    }

    private ArenaMeldekort lagArenaMeldekort() {
        return new ArenaMeldekort(LocalDate.now(), LocalDate.now(), BigDecimal.valueOf(2331),BigDecimal.valueOf(50_000),
                BigDecimal.valueOf(100));
    }

    private ArenaVedtak lagArenaVedtak() {
        return new ArenaVedtak(LocalDate.now(), LocalDate.now(), LocalDate.now(), LocalDate.now(), VedtakStatus.MOTAT,
                BigDecimal.valueOf(2331), List.of(lagArenaMeldekort()));
    }

    private ArenaSak lagArenaSak() {
        return new ArenaSak("123456789", RelatertYtelseTema.FA, SakStatus.AKTIV, List.of(lagArenaVedtak()));
    }

}
