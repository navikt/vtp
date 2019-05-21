package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EndringBeregningsgrunnlagAndelDto {

    protected double fordelingForrigeBehandling;
    protected double fordelingForrigeBehandlingPrAar;
    protected double refusjonskrav;
    protected double beregnetPrMnd;
    protected double beregnetPrAar;
    protected double belopFraInntektsmelding;
    protected double fastsattForrige;
    protected double fastsattForrigePrAar;
    protected double refusjonskravFraInntektsmelding;
    protected double snittIBeregningsperiodenPrMnd;
    protected int andelsnr;
    protected BeregningsgrunnlagArbeidsforholdDto arbeidsforhold;
    protected Kode inntektskategori;
    protected Kode aktivitetStatus;
    protected boolean lagtTilAvSaksbehandler;
    protected boolean fastsattAvSaksbehandler;
    protected List<Double> andelIArbeid;
}
