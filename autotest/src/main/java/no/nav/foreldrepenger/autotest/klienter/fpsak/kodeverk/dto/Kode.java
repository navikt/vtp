package no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Kode {

    public String kodeverk;
    public String kode;
    
    /** 
     * @deprecated Ikke bruk for i logikk, bruk heller {@link #kode}
     */
    @Deprecated
    private String navn;

    protected Kode() {
        // for deserialisering
    }

    public Kode(String kode) {
        this.kode = kode;
    }

    public Kode(String kodeverk, String kode) {
        this.kodeverk = kodeverk;
        this.kode = kode;
    }

    public Kode(String kodeverk, String kode, String navn) {
        this.kodeverk = kodeverk;
        this.kode = kode;
        this.navn = navn;
    }

    public String getNavn() {
        return navn;
    }
    
    public static Kode lagBlankKode() {
        return new Kode(null, "-", null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kode kode1 = (Kode) o;
        return Objects.equals(kode, kode1.kode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kodeverk, kode);
    }

    @Override
    public String toString() {
        return kodeverk + " - " + kode + (navn==null?"": " - " + navn);
    }
}
