package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AvklarAktiviteterDto {


    protected List<AktivitetTomDatoMappingDto> aktiviteterTomDatoMapping;

    public List<AktivitetTomDatoMappingDto> getAktiviteterTomDatoMapping() {
        return aktiviteterTomDatoMapping;
    }
}
