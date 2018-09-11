package no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Kodeverk{
    public List<Kode> ArbeidType;
    public List<Kode> OpptjeningAktivitetType;
    public Map<String, ArrayList<Kode>> Avslagsårsak;
    public List<Kode> BehandlingType;
    public List<Kode> BehandlingÅrsakType;
    public List<Kode> FagsakStatus;
    public List<Kode> FagsakYtelseType;
    public List<Kode> FagsakÅrsakType;
    public List<Kode> ForeldreType;
    public List<Kode> InnsynResultatType;
    public List<Kode> KlageAvvistÅrsak;
    public List<Kode> KlageMedholdÅrsak;
    public List<Kode> Landkoder;
    public List<Kode> MedlemskapManuellVurderingType;
    public List<Kode> MorsAktivitet;
    public List<Kode> NæringsvirksomhetType;
    public List<Kode> OmsorgsovertakelseVilkårType;
    public List<Kode> OverføringÅrsak;
    public List<Kode> PersonstatusType;
    public List<Kode> RelatertYtelseTilstand;
    public List<Kode> RelatertYtelseType;
    public List<Kode> SøknadtypeTillegg;
    public List<Kode> TypeFiske;
    public List<Kode> UtsettelseÅrsak;
    public List<Kode> UttakPeriodeType;
    public List<Kode> Venteårsak;
    public List<Kode> VergeType;
    public List<Kode> UttakPeriodeVurderingType;
    public List<Kode> IkkeOppfyltÅrsak;
    
    public Kode hentKode(String navn, List<Kode> kodegruppe) {
        for (Kode kode : kodegruppe) {
            if (kode.navn.equals(navn)) {
                return kode;
            }
        }
        throw new RuntimeException("Finner ikke kode i kodeverk: " + navn);
    }
}
