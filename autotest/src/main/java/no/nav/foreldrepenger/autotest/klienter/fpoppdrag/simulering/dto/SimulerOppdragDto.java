package no.nav.foreldrepenger.autotest.klienter.fpoppdrag.simulering.dto;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class SimulerOppdragDto {

    @JsonProperty
    private Long behandlingId;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> oppdragPrMottaker;

    @JsonProperty
    private String behandlingÅrsakKode;

    public SimulerOppdragDto(Long behandlingId, List<String> oppdragPrMottaker) {
        this.behandlingId = behandlingId;
        this.oppdragPrMottaker = oppdragPrMottaker;
    }

    public SimulerOppdragDto(Long behandlingId, List<String> oppdragPrMottaker, String behandlingÅrsakKode) {
        this(behandlingId, oppdragPrMottaker);
        this.behandlingÅrsakKode = behandlingÅrsakKode;
    }

    public Long getBehandlingId() {
        return behandlingId;
    }

    public List<String> getOppdragPrMottaker() {
        return oppdragPrMottaker;
    }

    public String getBehandlingÅrsakKode() {
        return behandlingÅrsakKode;
    }

    @JsonIgnore
    public static SimulerOppdragDto lagDto(Long behandlingId, List<String> råXml) {
        Objects.requireNonNull(råXml, "Rå XML kan ikke være null");
        List<String> encoded = råXml.stream()
            .map(str -> Base64.getEncoder()
                .encodeToString(str.getBytes(Charset.forName("UTF-8"))))
            .collect(Collectors.toList());
        return new SimulerOppdragDto(behandlingId, encoded);
    }
}
