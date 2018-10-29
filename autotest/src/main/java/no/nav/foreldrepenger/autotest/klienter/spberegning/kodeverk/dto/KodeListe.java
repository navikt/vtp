package no.nav.foreldrepenger.autotest.klienter.spberegning.kodeverk.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KodeListe extends ArrayList<Kode>{
    
    public Kode getKode(String kodeverdi) {
        for (Kode kode : this) {
            if(kode.kode.equals(kodeverdi) || kode.navn.equals(kodeverdi)) { //Kan hente kode basert på kode eller navn
                return kode;
            }
        }
        return null;
    }
}
