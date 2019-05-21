package no.nav.foreldrepenger.autotest.klienter.spberegning.kodeverk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Kodeverk {

    public KodeListe UtsettelseÅrsak;
    public KodeListe ArkivFilType;
    public KodeListe Tema;
    public KodeListe ArbeidType;
    public KodeListe DokumentTypeId;
    public KodeListe DokumentKategori;
    public KodeListe RelatertYtelseType;
    public KodeListe Begrunnelse;
    public KodeListe Fagsystem;
    public KodeListe VariantFormat;
    public KodeListe AktivitetStatus;
    
}
