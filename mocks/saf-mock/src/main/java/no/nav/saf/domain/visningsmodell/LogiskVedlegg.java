package no.nav.saf.domain.visningsmodell;

import java.util.Objects;

public final class LogiskVedlegg {
	private final String logiskVedleggId;
	private final String tittel;

	public LogiskVedlegg(String logiskVedleggId, String tittel) {
		this.logiskVedleggId = logiskVedleggId;
		this.tittel = tittel;
	}

	public String getLogiskVedleggId() {
		return logiskVedleggId;
	}

	public String getTittel() {
		return tittel;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LogiskVedlegg that = (LogiskVedlegg) o;
		return Objects.equals(getLogiskVedleggId(), that.getLogiskVedleggId()) &&
				Objects.equals(getTittel(), that.getTittel());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getLogiskVedleggId(), getTittel());
	}

	@Override
	public String toString() {
		return "LogiskVedlegg{" +
				"logiskVedleggId='" + logiskVedleggId + '\'' +
				", tittel='" + tittel + '\'' +
				'}';
	}
}
