package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.beregningsgrunnlag;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.InfotrygdBehandlingstema;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.ytelse.InfotrygdYtelse;

/**
 * @see https://confluence.adeo.no/display/INFOTRYGD/Tjeneste+finnGrunnlag+-+Informasjonsmodell
 */
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY)
@JsonSubTypes({
        @Type(InfotrygdForeldrepengerBeregningsgrunnlag.class),
        @Type(InfotrygdSykepengerBeregningsgrunnlag.class),
        @Type(InfotrygdEngangsstønadBeregningsgrunnlag.class),
        @Type(InfotrygdPårørendeSykdomBeregningsgrunnlag.class),
        @Type(InfotrygdRammevedtaksGrunnlag.class)
})
public abstract class InfotrygdBeregningsgrunnlag {

    /** NB: skal matche iverksatt dato i {@link InfotrygdYtelse}. */
    private LocalDate startdato;

    /** Startdato for søkeperioden. */
    private LocalDate fom;

    /** Sluttdato for søkeperioden.  Hvis tom ikke har verdi settes maks dato 9999-12-31.*/
    private LocalDate tom;

    /**
     * Stønadsklasse 2. Påkrevd
     * <p>
     * For sykepenger - blankt eller SP
     * <p>
     * For foreldrepenger - AP, FØ, SV
     * <p>
     * For pårørende sykdom - OM, OP, PB, PI, PP
     * <p>
     * For engangsstønad - FE, AE
     */
    private InfotrygdBehandlingstema behandlingstema;

    private List<InfotrygdVedtak> vedtak= new ArrayList<>();

    public LocalDate getStartdato() {
        return startdato;
    }

    public void setStartdato(LocalDate startdato) {
        this.startdato = startdato;
    }

    public LocalDate getFom() {
        return fom;
    }

    public void setFom(LocalDate fom) {
        this.fom = fom;
    }

    public LocalDate getTom() {
        return tom;
    }

    public void setTom(LocalDate tom) {
        this.tom = tom;
    }

    public InfotrygdBehandlingstema getBehandlingTema() {
        return behandlingstema;
    }

    public void setBehandlingTema(InfotrygdBehandlingstema behandlingTema) {
        this.behandlingstema = behandlingTema;
    }

    public List<InfotrygdVedtak> getVedtak() {
        return vedtak;
    }

    public void setVedtak(List<InfotrygdVedtak> vedtak) {
        this.vedtak.clear();
        this.vedtak.addAll(vedtak);
    }
}
