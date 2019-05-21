package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KortvarigeArbeidsforholdDto {

    protected boolean erTidsbegrensetArbeidsforhold;
    protected int andelsnr;
    protected BeregningsgrunnlagArbeidsforholdDto arbeidsforhold;
    protected Kode inntektskategori;
    protected Kode aktivitetStatus;
    protected boolean lagtTilAvSaksbehandler;
    protected boolean fastsattAvSaksbehandler;
    protected List<Double> andelIArbeid;

    public boolean isErTidsbegrensetArbeidsforhold() {
        return erTidsbegrensetArbeidsforhold;
    }

    public int getAndelsnr() {
        return andelsnr;
    }

    public BeregningsgrunnlagArbeidsforholdDto getArbeidsforhold() {
        return arbeidsforhold;
    }

    public Kode getInntektskategori() {
        return inntektskategori;
    }

    public Kode getAktivitetStatus() {
        return aktivitetStatus;
    }

    public boolean isLagtTilAvSaksbehandler() {
        return lagtTilAvSaksbehandler;
    }

    public boolean isFastsattAvSaksbehandler() {
        return fastsattAvSaksbehandler;
    }

    public List<Double> getAndelIArbeid() {
        return andelIArbeid;
    }
}
