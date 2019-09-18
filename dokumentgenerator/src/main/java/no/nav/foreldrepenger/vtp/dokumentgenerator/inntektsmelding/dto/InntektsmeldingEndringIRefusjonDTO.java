package no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InntektsmeldingEndringIRefusjonDTO {

    LocalDate endringsdato;

    BigDecimal refusjonsbeloepPrMnd;

    public LocalDate getEndringsdato() {
        return endringsdato;
    }

    public void setEndringsdato(LocalDate endringsdato) {
        this.endringsdato = endringsdato;
    }

    public BigDecimal getRefusjonsbeloepPrMnd() {
        return refusjonsbeloepPrMnd;
    }

    public void setRefusjonsbeloepPrMnd(BigDecimal refusjonsbeloepPrMnd) {
        this.refusjonsbeloepPrMnd = refusjonsbeloepPrMnd;
    }
}
