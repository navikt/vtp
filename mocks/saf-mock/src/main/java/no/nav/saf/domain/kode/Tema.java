package no.nav.saf.domain.kode;

import java.util.Arrays;
import java.util.List;

public enum Tema {
	AAP("Arbeidsavklaringspenger"),
	AAR("Aa-registeret"),
	AGR("Ajourhold - Grunnopplysninger"),
	BAR("Barnetrygd"),
	BID("Bidrag"),
	BIL("Bil"),
	DAG("Dagpenger"),
	ENF("Enslig forsørger"),
	ERS("Erstatning"),
	FAR("Farskap"),
	FEI("Feilutbetaling"),
	FOR("Foreldre- og svangerskapspenger"),
	FOS("Forsikring"),
	FUL("Fullmakt"),
	GEN("Generell"),
	GRA("Gravferdsstønad"),
	GRU("Grunn- og hjelpestønad"),
	HEL("Helsetjenester og ortopediske hjelpemidler"),
	HJE("Hjelpemidler"),
	IAR("Inkluderende arbeidsliv"),
	IND("Tiltakspenger"),
	KON("Kontantstøtte"),
	KTR("Kontroll"),
	MED("Medlemskap"),
	MOB("Mobilitetsfremmende stønad"),
	OMS("Omsorgspenger, pleiepenger og opplæringspenger"),
	OPA("Oppfølging - Arbeidsgiver"),
	OPP("Oppfølging"),
	PEN("Pensjon"),
	PER("Permittering og masseoppsigelser"),
	REH("Rehabilitering"),
	REK("Rekruttering og stilling"),
	RPO("Retting av personopplysninger"),
	RVE("Rettferdsvederlag"),
	SAA("Sanksjon - Arbeidsgiver"),
	SAK("Saksomkostninger"),
	SAP("Sanksjon - Person"),
	SER("Serviceklager"),
	SIK("Sikkerhetstiltak"),
	STO("Regnskap/utbetaling"),
	SUP("Supplerende stønad"),
	SYK("Sykepenger"),
	SYM("Sykmeldinger"),
	TIL("Tiltak"),
	TRK("Trekkhåndtering"),
	TRY("Trygdeavgift"),
	TSO("Tilleggsstønad"),
	TSR("Tilleggsstønad arbeidssøkere"),
	UFM("Unntak fra medlemskap"),
	UFO("Uføretrygd"),
	UKJ("Ukjent"),
	VEN("Ventelønn"),
	YRA("Yrkesrettet attføring"),
	YRK("Yrkesskade");

	private final String temanavn;

	Tema(final String temanavn) {
		this.temanavn = temanavn;
	}

	public String getTemanavn() {
		return temanavn;
	}

	public static List<Tema> asList() {
		return Arrays.asList(values());
	}
}
