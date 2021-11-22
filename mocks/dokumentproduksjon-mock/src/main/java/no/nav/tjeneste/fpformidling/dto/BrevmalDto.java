package no.nav.tjeneste.fpformidling.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import no.nav.tjeneste.virksomhet.kodeverk.v2.informasjon.Kodeverk;

public class BrevmalDto {
    @NotNull
    @Pattern(regexp = "[A-Z]{6}")
    private String kode;
    @NotNull
    private String navn;
    @NotNull
    private Kodeverk restriksjon;
    private boolean tilgjengelig;

    public BrevmalDto() {//For Jackson
    }

    public BrevmalDto(String kode, String navn, Kodeverk restriksjon, boolean tilgjengelig) {
        this.kode = kode;
        this.navn = navn;
        this.restriksjon = restriksjon;
        this.tilgjengelig = tilgjengelig;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public Kodeverk getRestriksjon() {
        return restriksjon;
    }

    public void setRestriksjon(Kodeverk restriksjon) {
        this.restriksjon = restriksjon;
    }

    public boolean getTilgjengelig() {
        return tilgjengelig;
    }

    public void setTilgjengelig(boolean tilgjengelig) {
        this.tilgjengelig = tilgjengelig;
    }
}
