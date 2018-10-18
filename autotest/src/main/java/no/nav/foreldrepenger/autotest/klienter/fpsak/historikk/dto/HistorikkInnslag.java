package no.nav.foreldrepenger.autotest.klienter.fpsak.historikk.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HistorikkInnslag {

    protected HistorikkTekst tekst;
    protected List<HistorikkinnslagDel> historikkinnslagDeler;
    
    protected int behandlingsid;
    protected Kode type;
    protected Kode aktoer;
    protected Kode kjoenn;
    
    public String getTekst() {
        return type.navn;
    }
}
