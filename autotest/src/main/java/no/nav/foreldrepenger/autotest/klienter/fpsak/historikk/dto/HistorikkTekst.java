package no.nav.foreldrepenger.autotest.klienter.fpsak.historikk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HistorikkTekst {
    protected String begrunnelse;
    protected String hendelse;
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "<begrunnelse="+begrunnelse + ", hendelse"+hendelse+">";
    }
}
