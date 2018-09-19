package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BekreftetForelder {
    public String adresse;
    public boolean bekreftetAvTps;
    public Object dodsdato;
    public boolean erMor;
    public String navn;
    public int nummer;
    public Object oversyrtPersonstatus;
    public Kode personstatus;
    public Object utlandsadresse;
}
