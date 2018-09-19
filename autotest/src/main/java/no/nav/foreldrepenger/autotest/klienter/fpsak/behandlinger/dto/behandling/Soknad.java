package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import java.time.LocalDate;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Soknad {
    //Generelt
    public LocalDate mottattDato;
    public String tilleggsopplysninger;
    public String begrunnelseForSenInnsending;
    public String annenPartNavn;
    
    //Fødsel
    public LocalDate utstedtdato;
    public LocalDate termindato;
    public int antallBarn;
    public Map<Integer, LocalDate> fodselsdatoer;

    //adopsjon
    public Kode farSokerType;
    public LocalDate omsorgsovertakelseDato;
    public Map<Integer, LocalDate> adopsjonFodelsedatoer;

}
