package no.nav.foreldrepenger.autotest.klienter.fpsak.historikk.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HistorikkInnslag {

    public HistorikkTekst tekst;
    public List<HistorikkinnslagDel> historikkinnslagDeler;
    
    public int behandlingsid;
    public Kode type;
    public Kode aktoer;
    public Kode kjoenn;
}
