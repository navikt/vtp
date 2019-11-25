package no.nav.saf.domain.kode;

import java.util.Arrays;
import java.util.List;

public enum Journalposttype {
	I,
	U,
	N;

	public static List<Journalposttype> asList() {
		return Arrays.asList(values());
	}
}
