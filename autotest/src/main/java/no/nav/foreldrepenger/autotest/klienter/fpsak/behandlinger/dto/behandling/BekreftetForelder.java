package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BekreftetForelder {
    protected String adresse;
    protected boolean bekreftetAvTps;
    protected Object dodsdato;
    protected boolean erMor;
    protected String navn;
    protected int nummer;
    protected Object oversyrtPersonstatus;
    protected Kode personstatus;
    protected Object utlandsadresse;
}
