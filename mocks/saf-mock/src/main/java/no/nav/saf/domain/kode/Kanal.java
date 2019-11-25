package no.nav.saf.domain.kode;

public enum Kanal {
	ALTINN("Altinn"),
	EESSI("EESSI"),
	EIA("EIA"),
	EKST_OPPS("Eksternt oppslag"),
	LOKAL_UTSKRIFT("Lokal utskrift"),
	NAV_NO("Ditt NAV"),
	SENTRAL_UTSKRIFT("Sentral utskrift"),
	SDP("Digital postkasse til innbyggere"),
	SKAN_NETS("Skanning Nets"),
	SKAN_PEN("Skanning Pensjon"),
	TRYGDERETTEN("Trygderetten"),
	HELSENETTET("Helsenettet"),
	INGEN_DISTRIBUSJON("Ingen distribusjon"),
	UKJENT("Ukjent"),
	NAV_NO_UINNLOGGET("Ditt NAV uten ID-porten-pålogging");

	private final String kanalnavn;

	Kanal(String kanalnavn) {
		this.kanalnavn = kanalnavn;
	}

	public String getKanalnavn() {
		return kanalnavn;
	}
}
