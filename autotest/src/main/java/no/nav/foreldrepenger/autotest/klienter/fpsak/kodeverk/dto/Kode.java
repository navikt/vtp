package no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Kode {
    
    public String kodeverk;
    public String kode;
    public String navn;
    
    public Kode() {
        
    }
    
    public Kode(String kodeverk, String kode, String navn) {
        this.kodeverk = kodeverk;
        this.kode = kode;
        this.navn = navn;
    }
    
    public static Kode lagBlankKode() {
        return new Kode(null, "-", null);
    }

    @Override
    public boolean equals(Object obj) {

        //System.out.println(((Kode) obj).navn + " : " + navn + " - " + ((Kode) obj).kode + " : " + kode);
        return ((Kode) obj).navn.equals(navn) || ((Kode) obj).kode.equals(kode);
    }

    @Override
    public String toString() {
        return kodeverk + " - " + kode + " - " + navn;
    }
}
