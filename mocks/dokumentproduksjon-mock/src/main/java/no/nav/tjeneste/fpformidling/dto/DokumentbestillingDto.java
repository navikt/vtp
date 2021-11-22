package no.nav.tjeneste.fpformidling.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class DokumentbestillingDto {
    @NotNull
    private UUID behandlingUuid;
    @NotNull
    private String ytelseType;
    @Pattern(
            regexp = "[A-Z]{6}"
    )
    private String dokumentMal;
    @Pattern(
            regexp = "[A-ZÆØÅ0-9]{1,100}"
    )
    private String historikkAktør;
    private String tittel;
    private String fritekst;
    @Pattern(
            regexp = "[A-ZÆØÅ0-9]{1,100}"
    )
    private String arsakskode;
    private boolean gjelderVedtak;
    private boolean erOpphevetKlage;

    public DokumentbestillingDto() {
    }

    public UUID getBehandlingUuid() {
        return this.behandlingUuid;
    }

    public void setBehandlingUuid(UUID behandlingUuid) {
        this.behandlingUuid = behandlingUuid;
    }

    public String getYtelseType() {
        return this.ytelseType;
    }

    public void setYtelseType(String ytelseType) {
        this.ytelseType = ytelseType;
    }

    public String getDokumentMal() {
        return this.dokumentMal;
    }

    public void setDokumentMal(String dokumentMal) {
        this.dokumentMal = dokumentMal;
    }

    public String getHistorikkAktør() {
        return this.historikkAktør;
    }

    public void setHistorikkAktør(String historikkAktør) {
        this.historikkAktør = historikkAktør;
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

    public String getArsakskode() {
        return this.arsakskode;
    }

    public void setArsakskode(String arsakskode) {
        this.arsakskode = arsakskode;
    }

    public boolean isGjelderVedtak() {
        return this.gjelderVedtak;
    }

    public void setGjelderVedtak(boolean gjelderVedtak) {
        this.gjelderVedtak = gjelderVedtak;
    }

    public boolean isErOpphevetKlage() {
        return this.erOpphevetKlage;
    }

    public void setErOpphevetKlage(boolean erOpphevetKlage) {
        this.erOpphevetKlage = erOpphevetKlage;
    }
}
