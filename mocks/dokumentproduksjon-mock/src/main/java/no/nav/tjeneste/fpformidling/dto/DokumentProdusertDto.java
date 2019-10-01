package no.nav.tjeneste.fpformidling.dto;


import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DokumentProdusertDto {

    @Valid
    @NotNull
    private UUID behandlingUuid;

    @NotNull
    @Size(min = 1, max = 100)
    private String dokumentMal;

    public DokumentProdusertDto() {
        // trengs for deserialisering av JSON
    }

    public UUID getBehandlingUuid() {
        return behandlingUuid;
    }

    public void setBehandlingUuid(UUID behandlingUuid) {
        this.behandlingUuid = behandlingUuid;
    }

    public String getDokumentMal() {
        return dokumentMal;
    }

    public void setDokumentMal(String dokumentMal) {
        this.dokumentMal = dokumentMal;
    }

}
