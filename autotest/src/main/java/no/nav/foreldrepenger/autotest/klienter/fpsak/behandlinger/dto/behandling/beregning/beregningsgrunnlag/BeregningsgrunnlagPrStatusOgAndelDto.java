package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BeregningsgrunnlagPrStatusOgAndelDto {

    protected LocalDate beregningsgrunnlagTom;
    protected LocalDate beregningsgrunnlagFom;
    protected Kode aktivitetStatus;
    protected LocalDate beregningsperiodeFom;
    protected LocalDate beregningsperiodeTom;
    protected double beregnetPrAar;
    protected double overstyrtPrAar;
    protected double bruttoPrAar;
    protected double avkortetPrAar;
    protected double redusertPrAar;
    protected boolean erTidsbegrensetArbeidsforhold;
    protected boolean erNyIArbeidslivet;
    protected boolean lonnsendringIBeregningsperioden;
    protected int andelsnr;
    protected double besteberegningPrAar;
    protected Kode inntektskategori;
    protected BeregningsgrunnlagArbeidsforholdDto arbeidsforhold;
    protected boolean fastsattAvSaksbehandler;
    
    public Kode getAktivitetStatus() {
        return aktivitetStatus;
    }
}
