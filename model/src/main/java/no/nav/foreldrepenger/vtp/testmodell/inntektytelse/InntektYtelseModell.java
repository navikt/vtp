package no.nav.foreldrepenger.vtp.testmodell.inntektytelse;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.ArbeidsforholdModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.ArenaModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.InfotrygdModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger.OmsorgspengerModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.sigrun.SigrunModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.TRexModell;

public record InntektYtelseModell(@JsonProperty("arena") ArenaModell arenaModell,
                                  @JsonProperty("infotrygd") InfotrygdModell infotrygdModell,
                                  @JsonProperty("trex") TRexModell tRexModell,
                                  @JsonProperty("inntektskomponent") InntektskomponentModell inntektskomponentModell,
                                  @JsonProperty("aareg") ArbeidsforholdModell arbeidsforholdModell,
                                  @JsonProperty("sigrun") SigrunModell sigrunModell,
                                  @JsonProperty("omsorgspenger") OmsorgspengerModell omsorgspengerModell) {
    @JsonCreator
    public InntektYtelseModell(ArenaModell arenaModell, InfotrygdModell infotrygdModell, TRexModell tRexModell,
                               InntektskomponentModell inntektskomponentModell, ArbeidsforholdModell arbeidsforholdModell,
                               SigrunModell sigrunModell, OmsorgspengerModell omsorgspengerModell) {
        this.arenaModell = Optional.ofNullable(arenaModell).orElse(new ArenaModell());
        this.infotrygdModell = Optional.ofNullable(infotrygdModell).orElse(new InfotrygdModell());
        this.tRexModell = Optional.ofNullable(tRexModell).orElse(new TRexModell());
        this.inntektskomponentModell = Optional.ofNullable(inntektskomponentModell).orElse(new InntektskomponentModell());
        this.arbeidsforholdModell = Optional.ofNullable(arbeidsforholdModell).orElse(new ArbeidsforholdModell());
        this.sigrunModell = Optional.ofNullable(sigrunModell).orElse(new SigrunModell());
        this.omsorgspengerModell = Optional.ofNullable(omsorgspengerModell).orElse(new OmsorgspengerModell());
    }
}
