package no.nav.saf.domain.visningsmodell;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.saf.domain.kode.Datotype;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

public class RelevantDato {
	// Fallback for datoer som er påkrevd men av ukjente årsaker ikke finnes.
	public static final LocalDateTime INVALID_DATE = LocalDateTime.of(LocalDate.of(1, 1, 1), LocalTime.of(0, 0));

	private final LocalDateTime dato;
	private final Datotype datotype;

	@JsonCreator
	public RelevantDato(@JsonProperty("dato") Date dato, @JsonProperty("datotype") Datotype datotype) {
		this.dato = toLocalDateTime(dato);
		this.datotype = datotype;
	}

	public LocalDateTime getDato() {
		return dato;
	}

	public Datotype getDatotype() {
		return datotype;
	}

	private static LocalDateTime toLocalDateTime(Date date) {
		if (date == null) {
			return INVALID_DATE;
		}
		return LocalDateTime.from(date.toInstant().atZone(ZoneId.systemDefault()));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RelevantDato that = (RelevantDato) o;
		return Objects.equals(getDato(), that.getDato()) &&
				getDatotype() == that.getDatotype();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getDato(), getDatotype());
	}

	@Override
	public String toString() {
		return "RelevantDato{" +
				"dato=" + dato +
				", datotype=" + datotype +
				'}';
	}
}
