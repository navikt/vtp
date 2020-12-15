package no.nav.foreldrepenger.vtp.testmodell.inntektytelse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.ArbeidsforholdModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.ArenaModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.InfotrygdModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger.OmsorgspengerModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.sigrun.SigrunModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.TRexModell;

@JsonInclude(Include.NON_EMPTY)
public class InntektYtelseModell {

    @JsonProperty("arena")
    private ArenaModell arenaModell;

    @JsonProperty("infotrygd")
    private InfotrygdModell infotrygdModell;

    @JsonProperty("trex")
    private TRexModell tRexModell;

    @JsonProperty("inntektskomponent")
    private InntektskomponentModell inntektskomponentModell;

    @JsonProperty("aareg")
    private ArbeidsforholdModell arbeidsforholdModell;

    @JsonProperty("sigrun")
    private SigrunModell sigrunModell;

    @JsonProperty("omsorgspenger")
    private OmsorgspengerModell omsorgspengerModell;

    public InntektYtelseModell() {
    }

    public InntektYtelseModell(ArenaModell arenaModell, InfotrygdModell infotrygdModell, TRexModell tRexModell,
                               InntektskomponentModell inntektskomponentModell, ArbeidsforholdModell arbeidsforholdModell,
                               SigrunModell sigrunModell, OmsorgspengerModell omsorgspengerModell) {
        this.arenaModell = arenaModell;
        this.infotrygdModell = infotrygdModell;
        this.tRexModell = tRexModell;
        this.inntektskomponentModell = inntektskomponentModell;
        this.arbeidsforholdModell = arbeidsforholdModell;
        this.sigrunModell = sigrunModell;
        this.omsorgspengerModell = omsorgspengerModell;
    }

    public ArenaModell getArenaModell() {
        if (arenaModell == null) {
            this.arenaModell = new ArenaModell();
        }
        return arenaModell;
    }

    public void setArenaModell(ArenaModell arenaModell) {
        this.arenaModell = arenaModell;
    }

    public InfotrygdModell getInfotrygdModell() {
        if (infotrygdModell == null) {
            this.infotrygdModell = new InfotrygdModell();
        }
        return infotrygdModell;
    }

    public void setInfotrygdModell(InfotrygdModell infotrygdModell) {
        this.infotrygdModell = infotrygdModell;
    }

    public TRexModell gettRexModell() {
        return tRexModell;
    }

    public void settRexModell(TRexModell tRexModell) {
        this.tRexModell = tRexModell;
    }

    public InntektskomponentModell getInntektskomponentModell() {
        if (inntektskomponentModell == null) {
            this.inntektskomponentModell = new InntektskomponentModell();
        }
        return inntektskomponentModell;
    }

    public void setInntektskomponentModell(InntektskomponentModell inntektskomponentModell) {
        this.inntektskomponentModell = inntektskomponentModell;
    }

    public ArbeidsforholdModell getArbeidsforholdModell() {
        if(null == arbeidsforholdModell){
            this.arbeidsforholdModell = new ArbeidsforholdModell();
        }
        return arbeidsforholdModell;
    }

    public void setArbeidsforholdModell(ArbeidsforholdModell arbeidsforholdModell) {
        this.arbeidsforholdModell = arbeidsforholdModell;
    }

    public SigrunModell getSigrunModell() {
        if (sigrunModell == null) {
            this.sigrunModell = new SigrunModell();
        }
        return sigrunModell;
    }

    public void setSigrunModell(SigrunModell sigrunModell) {
        this.sigrunModell = sigrunModell;
    }

    public OmsorgspengerModell getOmsorgspengerModell() {
        if(omsorgspengerModell == null) {
            this.omsorgspengerModell = new OmsorgspengerModell();
        }
        return omsorgspengerModell;
    }

    public void setOmsorgspengerModell(OmsorgspengerModell omsorgspengerModell) {
        this.omsorgspengerModell = omsorgspengerModell;
    }
}
