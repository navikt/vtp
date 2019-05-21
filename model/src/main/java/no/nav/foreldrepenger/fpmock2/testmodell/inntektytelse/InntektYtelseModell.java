package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold.ArbeidsforholdModell;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arena.ArenaModell;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.InfotrygdModell;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.sigrun.SigrunModell;

@JsonInclude(Include.NON_EMPTY)
public class InntektYtelseModell {

    @JsonProperty("arena")
    private ArenaModell arenaModell;

    @JsonProperty("infotrygd")
    private InfotrygdModell infotrygdModell;

    @JsonProperty("inntektskomponent")
    private InntektskomponentModell inntektskomponentModell;

    @JsonProperty("aareg")
    private ArbeidsforholdModell arbeidsforholdModell;

    @JsonProperty("sigrun")
    private SigrunModell sigrunModell;

    public InntektYtelseModell() {
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
}
