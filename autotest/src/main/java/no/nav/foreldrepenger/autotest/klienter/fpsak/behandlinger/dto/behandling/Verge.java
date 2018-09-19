package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import java.time.LocalDate;

public class Verge {
	public Long aktoerId;
	public String fodselsnr;
	public LocalDate gyldigFom;
	public LocalDate gyldigTom;
	public String mandatTekst;
	public String navn;
	public Boolean sokerErKontaktPerson;
	public Boolean sokerErUnderTvungenForvaltning;
	public LocalDate vedtaksDato;
	public Boolean vergeErKontaktPerson;
}
