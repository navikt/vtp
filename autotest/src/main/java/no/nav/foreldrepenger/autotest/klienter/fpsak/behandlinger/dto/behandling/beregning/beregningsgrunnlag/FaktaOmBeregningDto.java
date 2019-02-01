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
}
