package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning;

import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.spberegning.kodeverk.dto.Kode;

public class BeregningsgrunnlagPrStatusOgAndelDto {
    Kode aktivitetStatus;
    Double beregnetPrAar;
    Double bruttoPrAar;
    String elementNavn;
    String orgNummer;
    String inntektsmeldingID;
    List<AktivitetsAvtaleDto> aktivitetsAvtaleDto;
    Boolean frilans;
    
    public BeregningsgrunnlagPrStatusOgAndelDto() {
    }
}
