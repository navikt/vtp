package no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto;

import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Kodeverk{
    public KodeListe ArbeidType;
    public KodeListe OpptjeningAktivitetType;
    public Map<String, KodeListe> Avslagsårsak;
    public KodeListe BehandlingType;
    public KodeListe BehandlingÅrsakType;
    public KodeListe FagsakStatus;
    public KodeListe FagsakYtelseType;
    public KodeListe FagsakÅrsakType;
    public KodeListe ForeldreType;
    public KodeListe InnsynResultatType;
    public KodeListe KlageAvvistÅrsak;
    public KodeListe KlageMedholdÅrsak;
    public KodeListe Landkoder;
    public KodeListe MedlemskapManuellVurderingType;
    public KodeListe MorsAktivitet;
    public KodeListe NæringsvirksomhetType;
    public KodeListe OmsorgsovertakelseVilkårType;
    public KodeListe OverføringÅrsak;
    public KodeListe PersonstatusType;
    public KodeListe RelatertYtelseTilstand;
    public KodeListe RelatertYtelseType;
    public KodeListe SøknadtypeTillegg;
    public KodeListe TypeFiske;
    public KodeListe UtsettelseÅrsak;
    public KodeListe UttakPeriodeType;
    public KodeListe Venteårsak;
    public KodeListe VergeType;
    public KodeListe UttakPeriodeVurderingType;
    public KodeListe IkkeOppfyltÅrsak;
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KodeListe extends ArrayList<Kode>{
        
        public Kode getKode(String kodeverdi) {
            for (Kode kode : this) {
                if(kode.kode.equals(kodeverdi) || kode.navn.equals(kodeverdi)) { //Kan hente kode basert på kode eller navn
                    return kode;
                }
            }
            return null;
        }
    }
}
