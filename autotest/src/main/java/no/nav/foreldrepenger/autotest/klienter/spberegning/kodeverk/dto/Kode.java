package no.nav.foreldrepenger.autotest.klienter.spberegning.kodeverk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Kode {
    
    public String kodeverk;
    public String kode;
    public String navn;
    
    public Kode() {
        
    }
    
    public Kode(String kodeverk, String kode, String navn) {
        super();
        this.kodeverk = kodeverk;
        this.kode = kode;
        this.navn = navn;
    }
    
    public static Kode lagBlankKode() {
        return new Kode(null, "-", null);
    }
}
