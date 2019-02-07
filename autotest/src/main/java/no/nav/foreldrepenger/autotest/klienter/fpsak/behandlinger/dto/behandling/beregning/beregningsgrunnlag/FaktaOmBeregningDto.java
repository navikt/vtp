package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.FaktaOmBeregningTilfelle;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FaktaOmBeregningDto {

    protected List<KortvarigeArbeidsforholdDto> kortvarigeArbeidsforhold;
    protected TilstøtendeYtelseDto tilstøtendeYtelse;
    protected FaktaOmBeregningAndelDto frilansAndel;
    protected EndringBeregningsgrunnlagDto endringBeregningsgrunnlag;
    protected KunYtelseDto kunYtelse;
    protected List<FaktaOmBeregningTilfelle> faktaOmBeregningTilfeller;
    protected List<ATogFLISammeOrganisasjonDto> arbeidstakerOgFrilanserISammeOrganisasjonListe;
    protected List<FaktaOmBeregningAndelDto> arbeidsforholdMedLønnsendringUtenIM;
    protected List<TilstøtendeYtelseAndelDto> besteberegningAndeler;
    protected VurderMottarYtelseDto vurderMottarYtelse;
    protected AvklarAktiviteterDto avklarAktiviteter;


    public List<KortvarigeArbeidsforholdDto> getKortvarigeArbeidsforhold() {
        return kortvarigeArbeidsforhold;
    }

    public TilstøtendeYtelseDto getTilstøtendeYtelse() {
        return tilstøtendeYtelse;
    }

    public FaktaOmBeregningAndelDto getFrilansAndel() {
        return frilansAndel;
    }

    public EndringBeregningsgrunnlagDto getEndringBeregningsgrunnlag() {
        return endringBeregningsgrunnlag;
    }

    public KunYtelseDto getKunYtelse() {
        return kunYtelse;
    }

    public List<FaktaOmBeregningTilfelle> getFaktaOmBeregningTilfeller() {
        return faktaOmBeregningTilfeller;
    }

    public List<ATogFLISammeOrganisasjonDto> getArbeidstakerOgFrilanserISammeOrganisasjonListe() {
        return arbeidstakerOgFrilanserISammeOrganisasjonListe;
    }

    public List<FaktaOmBeregningAndelDto> getArbeidsforholdMedLønnsendringUtenIM() {
        return arbeidsforholdMedLønnsendringUtenIM;
    }

    public List<TilstøtendeYtelseAndelDto> getBesteberegningAndeler() {
        return besteberegningAndeler;
    }

    public VurderMottarYtelseDto getVurderMottarYtelse() {
        return vurderMottarYtelse;
    }

    public AvklarAktiviteterDto getAvklarAktiviteter() {
        return avklarAktiviteter;
    }
}
