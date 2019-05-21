package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Verge {
    protected Long aktoerId;
    protected String fodselsnr;
    protected LocalDate gyldigFom;
    protected LocalDate gyldigTom;
    protected String mandatTekst;
    protected String navn;
    protected Boolean sokerErKontaktPerson;
    protected Boolean sokerErUnderTvungenForvaltning;
    protected LocalDate vedtaksDato;
    protected Boolean vergeErKontaktPerson;
}
