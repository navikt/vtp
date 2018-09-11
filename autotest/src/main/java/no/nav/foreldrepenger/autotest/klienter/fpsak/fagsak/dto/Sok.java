package no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Sok {
	public String searchString;

	public Sok(String søketekst) {
		super();
		this.searchString = søketekst;
	}
}
