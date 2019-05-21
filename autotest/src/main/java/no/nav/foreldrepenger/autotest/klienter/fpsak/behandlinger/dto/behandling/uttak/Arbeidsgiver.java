package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Arbeidsgiver {

    protected String identifikator;
    protected String navn;
    protected String aktørId;
    protected Boolean virksomhet;

    public String getIdentifikator() {
        return identifikator;
    }

    public String getNavn() {
        return navn;
    }

    public String getAktørId() {
        return aktørId;
    }

    public Boolean getVirksomhet() {
        return virksomhet;
    }
}
