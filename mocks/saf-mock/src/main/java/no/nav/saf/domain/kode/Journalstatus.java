package no.nav.saf.domain.kode;

import java.util.Arrays;
import java.util.List;

public enum Journalstatus {
	MOTTATT,
	JOURNALFOERT,
	FERDIGSTILT,
	EKSPEDERT,
	UNDER_ARBEID,
	FEILREGISTRERT,
	UTGAAR,
	AVBRUTT,
	UKJENT_BRUKER,
	RESERVERT,
	OPPLASTING_DOKUMENT,
	UKJENT;

	public static List<Journalstatus> asList() {
		return Arrays.asList(values());
	}
}
