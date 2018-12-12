package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.spberegning.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BeregningsgrunnlagPrStatusOgAndelDto {
    protected Kode aktivitetStatus;
    protected Double beregnetPrAar;
    protected Double bruttoPrAar;
    protected String elementNavn;
    protected String orgNummer;
    protected String inntektsmeldingID;
    protected List<AktivitetsAvtaleDto> aktivitetsAvtaleDto;
    protected Boolean frilans;
    
    public BeregningsgrunnlagPrStatusOgAndelDto() {
    }
}
