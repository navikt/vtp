package no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.dto;

import java.util.List;

import no.nav.inntektsmelding.xml.kodeliste._20180702.BegrunnelseIngenEllerRedusertUtbetalingKodeliste;
@Deprecated
public class InntektsmeldingSykepengerIArbeidsgiverperiodenDTO {

    private Integer bruttoUtbetaltSykepenger;

    private BegrunnelseIngenEllerRedusertUtbetalingKodeliste begrunnelseForReduksjon;

    private List<InntektsmeldingPeriodeDTO> arbeidsgiverperiode;


    public Integer getBruttoUtbetaltSykepenger() {
        return bruttoUtbetaltSykepenger;
    }

    public void setBruttoUtbetaltSykepenger(Integer bruttoUtbetaltSykepenger) {
        this.bruttoUtbetaltSykepenger = bruttoUtbetaltSykepenger;
    }

    public BegrunnelseIngenEllerRedusertUtbetalingKodeliste getBegrunnelseForReduksjon() {
        return begrunnelseForReduksjon;
    }

    public void setBegrunnelseForReduksjon(BegrunnelseIngenEllerRedusertUtbetalingKodeliste begrunnelseForReduksjon) {
        this.begrunnelseForReduksjon = begrunnelseForReduksjon;
    }

    public List<InntektsmeldingPeriodeDTO> getArbeidsgiverperiode() {
        return arbeidsgiverperiode;
    }

    public void setArbeidsgiverperiode(List<InntektsmeldingPeriodeDTO> arbeidsgiverperiode) {
        this.arbeidsgiverperiode = arbeidsgiverperiode;
    }
}
