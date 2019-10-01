package no.nav.tjeneste.fpformidling.dto;


import java.util.UUID;
import javax.validation.constraints.NotNull;

public class TekstFraSaksbehandlerDto {
    @NotNull
    private UUID behandlingUuid;
    private String vedtaksbrev;
    private String avklarFritekst;
    private String tittel;
    private String fritekst;

    public TekstFraSaksbehandlerDto() {
    }

    public UUID getBehandlingUuid() {
        return this.behandlingUuid;
    }

    public void setBehandlingUuid(UUID behandlingUuid) {
        this.behandlingUuid = behandlingUuid;
    }

    public String getVedtaksbrev() {
        return this.vedtaksbrev;
    }

    public void setVedtaksbrev(String vedtaksbrev) {
        this.vedtaksbrev = vedtaksbrev;
    }

    public String getAvklarFritekst() {
        return this.avklarFritekst;
    }

    public void setAvklarFritekst(String avklarFritekst) {
        this.avklarFritekst = avklarFritekst;
    }

    public String getTittel() {
        return this.tittel;
    }

    public void setTittel(String tittel) {
        this.tittel = tittel;
    }

    public String getFritekst() {
        return this.fritekst;
    }

    public void setFritekst(String fritekst) {
        this.fritekst = fritekst;
    }
}
