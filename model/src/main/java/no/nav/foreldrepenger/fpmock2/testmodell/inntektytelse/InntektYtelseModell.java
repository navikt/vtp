package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arena.ArenaModell;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.InfotrygdModell;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent.InntektKomponentModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.BrukerIdent;

@JsonInclude(Include.NON_EMPTY)
public class InntektYtelseModell {

    @JsonProperty("ident")
    private BrukerIdent ident;

    @JsonProperty("arena")
    private ArenaModell arenaModell;

    @JsonProperty("infotrygd")
    private InfotrygdModell infotrygdModell;

    @JsonProperty("inntektKomponent")
    private InntektKomponentModell inntektKomponentModell;

    public InntektYtelseModell() {
    }

    public InntektYtelseModell(BrukerIdent brukerIdent) {
        ident = brukerIdent;
    }

    public BrukerIdent getIdent() {
        return ident;
    }

    public void setIdent(BrukerIdent ident) {
        this.ident = ident;
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

    public InntektKomponentModell getInntektKomponentModell() {
        return inntektKomponentModell;
    }

    public void setInntektKomponentModell(InntektKomponentModell inntektKomponentModell) {
        this.inntektKomponentModell = inntektKomponentModell;
    }
}
