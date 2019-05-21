package no.nav.foreldrepenger.autotest.klienter.fpoppdrag.simulering.dto;

public class BehandlingIdDto {
    private Long behandlingId;

    public BehandlingIdDto(String behandlingId) {
        this.behandlingId = Long.valueOf(behandlingId);
    }

    public Long getBehandlingId() {
        return behandlingId;
    }
}
