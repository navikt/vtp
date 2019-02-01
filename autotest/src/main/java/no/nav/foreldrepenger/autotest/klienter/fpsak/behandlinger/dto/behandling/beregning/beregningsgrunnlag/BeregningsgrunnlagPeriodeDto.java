package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BeregningsgrunnlagPeriodeDto {
    protected LocalDate beregningsgrunnlagPeriodeFom;
    protected LocalDate beregningsgrunnlagPeriodeTom;
    protected double beregnetPrAar;
    protected double bruttoPrAar;
    protected double bruttoInkludertBortfaltNaturalytelsePrAar;
    protected double avkortetPrAar;
    protected double redusertPrAar;
    protected List<Kode> periodeAarsaker;
    protected int dagsats;
    protected List<BeregningsgrunnlagPrStatusOgAndelDto> beregningsgrunnlagPrStatusOgAndel;
    
    public List<BeregningsgrunnlagPrStatusOgAndelDto> getBeregningsgrunnlagPrStatusOgAndel(){
        return beregningsgrunnlagPrStatusOgAndel;
    }
}
