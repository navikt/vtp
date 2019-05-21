package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Personopplysning {
    private int id;
    protected List<Adresse> adresser;
    protected long aktoerId;
    protected Object annenPart;
    protected List<Personopplysning> barn;
    protected boolean bekreftetAvTps;
    protected Kode diskresjonskode;
    protected LocalDate doedsdato;
    protected LocalDate foedselsdato;
    protected String foedselsnr;
    protected Kode navBrukerKjonn;
    protected String navn;
    protected Object nummer;
    protected Kode personstatus;
    protected Kode region;
    protected Kode siviltilstand;
    protected Object valgtOpplysning;
    
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public LocalDate getDoedsdato() {
        return doedsdato;
    }
    public void setDoedsdato(LocalDate doedsdato) {
        this.doedsdato = doedsdato;
    }
    public Kode getNavBrukerKjonn() {
        return navBrukerKjonn;
    }
    public void setNavBrukerKjonn(Kode navBrukerKjonn) {
        this.navBrukerKjonn = navBrukerKjonn;
    }
    public String getNavn() {
        return navn;
    }
    public void setNavn(String navn) {
        this.navn = navn;
    }
    public long getAktoerId() {
        return aktoerId;
    }
    public void setAktoerId(long aktoerId) {
        this.aktoerId = aktoerId;
    }
}
