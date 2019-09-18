package no.nav.tjeneste.fpformidling.dto;

import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class BehandlingUuidDto {

    @NotNull
    @Valid
    private UUID behandlingUuid;

    public BehandlingUuidDto() { //For Jackson
    }

    public BehandlingUuidDto(UUID behandlingUuid) {
        this.behandlingUuid = behandlingUuid;
    }

    public UUID getBehandlingUuid() {
        return behandlingUuid;
    }
}
