package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import java.time.LocalDate;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Soknad {
    //Generelt
    protected LocalDate mottattDato;
    protected String tilleggsopplysninger;
    protected String begrunnelseForSenInnsending;
    protected String annenPartNavn;
    
    //Fødsel
    protected LocalDate utstedtdato;
    protected LocalDate termindato;
    protected int antallBarn;
    protected Map<Integer, LocalDate> fodselsdatoer;

    //adopsjon
    protected Kode farSokerType;
    protected LocalDate omsorgsovertakelseDato;
    protected Map<Integer, LocalDate> adopsjonFodelsedatoer;
    
    public LocalDate getMottattDato() {
        return mottattDato;
    }

    public LocalDate getOmsorgsovertakelseDato() {
        return omsorgsovertakelseDato;
    }

    public int getAntallBarn() {
        return antallBarn;
    }

    public void setAntallBarn(int antallBarn) {
        this.antallBarn = antallBarn;
    }

    public Map<Integer, LocalDate> getAdopsjonFodelsedatoer() {
        return adopsjonFodelsedatoer;
    }

    public void setAdopsjonFodelsedatoer(Map<Integer, LocalDate> adopsjonFodelsedatoer) {
        this.adopsjonFodelsedatoer = adopsjonFodelsedatoer;
    }

    public Map<Integer, LocalDate> getFodselsdatoer() {
        return fodselsdatoer;
    }

    public void setFodselsdatoer(Map<Integer, LocalDate> fodselsdatoer) {
        this.fodselsdatoer = fodselsdatoer;
    }

}
