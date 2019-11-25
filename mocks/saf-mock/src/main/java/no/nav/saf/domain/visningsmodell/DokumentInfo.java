package no.nav.saf.domain.visningsmodell;

import no.nav.saf.domain.kode.Dokumentstatus;
import no.nav.saf.domain.kode.Skjerming;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class DokumentInfo {
	private final Journalpost parent;

	private final String dokumentInfoId;
	private final String tittel;
	private final String brevkode;
	private final Dokumentstatus dokumentstatus;
	private final LocalDateTime datoFerdigstilt;
	private final String originalJournalpostId;
	private final Skjerming skjerming;
	private final List<LogiskVedlegg> logiskeVedlegg = new ArrayList<>();
	private final List<Dokumentvariant> dokumentvarianter = new ArrayList<>();

	public DokumentInfo(Journalpost parent, String dokumentInfoId, String tittel, String brevkode, Dokumentstatus dokumentstatus, LocalDateTime datoFerdigstilt, String originalJournalpostId, Skjerming skjerming) {
		this.parent = parent;
		this.dokumentInfoId = dokumentInfoId;
		this.tittel = tittel;
		this.brevkode = brevkode;
		this.dokumentstatus = dokumentstatus;
		this.datoFerdigstilt = datoFerdigstilt;
		this.originalJournalpostId = originalJournalpostId;
		this.skjerming = skjerming;
	}

	private DokumentInfo(Builder builder) {
		parent = builder.parent;
		dokumentInfoId = builder.dokumentInfoId;
		tittel = builder.tittel;
		brevkode = builder.brevkode;
		dokumentstatus = builder.dokumentstatus;
		datoFerdigstilt = builder.datoFerdigstilt;
		originalJournalpostId = builder.originalJournalpostId;
		skjerming = builder.skjerming;
	}

	public Journalpost getParent() {
		return parent;
	}

	public String getDokumentInfoId() {
		return dokumentInfoId;
	}

	public String getTittel() {
		return tittel;
	}

	public String getBrevkode() {
		return brevkode;
	}

	public Dokumentstatus getDokumentstatus() {
		return dokumentstatus;
	}

	public LocalDateTime getDatoFerdigstilt() {
		return datoFerdigstilt;
	}

	public String getOriginalJournalpostId() {
		return originalJournalpostId;
	}

	public Skjerming getSkjerming() {
		return skjerming;
	}

	public List<LogiskVedlegg> getLogiskeVedlegg() {
		return logiskeVedlegg;
	}

	public List<Dokumentvariant> getDokumentvarianter() {
		return dokumentvarianter;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static Builder builder(DokumentInfo copy) {
		Builder builder = new Builder();
		builder.parent = copy.getParent();
		builder.dokumentInfoId = copy.getDokumentInfoId();
		builder.tittel = copy.getTittel();
		builder.brevkode = copy.getBrevkode();
		builder.dokumentstatus = copy.getDokumentstatus();
		builder.datoFerdigstilt = copy.getDatoFerdigstilt();
		builder.originalJournalpostId = copy.getOriginalJournalpostId();
		builder.skjerming = copy.getSkjerming();
		return builder;
	}

	@Override
	public String toString() {
		return "DokumentInfo{" +
				"dokumentInfoId='" + dokumentInfoId + '\'' +
				", tittel='" + tittel + '\'' +
				", brevkode='" + brevkode + '\'' +
				", dokumentstatus=" + dokumentstatus +
				", datoFerdigstilt=" + datoFerdigstilt +
				", originalJournalpostId='" + originalJournalpostId + '\'' +
				", skjerming=" + skjerming +
				", logiskeVedlegg=" + logiskeVedlegg +
				", dokumentvarianter=" + dokumentvarianter +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DokumentInfo that = (DokumentInfo) o;
		return Objects.equals(getParent(), that.getParent()) &&
				Objects.equals(getDokumentInfoId(), that.getDokumentInfoId()) &&
				Objects.equals(getTittel(), that.getTittel()) &&
				Objects.equals(getBrevkode(), that.getBrevkode()) &&
				getDokumentstatus() == that.getDokumentstatus() &&
				Objects.equals(getDatoFerdigstilt(), that.getDatoFerdigstilt()) &&
				Objects.equals(getOriginalJournalpostId(), that.getOriginalJournalpostId()) &&
				getSkjerming() == that.getSkjerming() &&
				Objects.equals(getLogiskeVedlegg(), that.getLogiskeVedlegg()) &&
				Objects.equals(getDokumentvarianter(), that.getDokumentvarianter());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getParent(), getDokumentInfoId(), getTittel(), getBrevkode(), getDokumentstatus(), getDatoFerdigstilt(), getOriginalJournalpostId(), getSkjerming(), getLogiskeVedlegg(), getDokumentvarianter());
	}

	public static final class Builder {
		private Journalpost parent;
		private String dokumentInfoId;
		private String tittel;
		private String brevkode;
		private Dokumentstatus dokumentstatus;
		private LocalDateTime datoFerdigstilt;
		private String originalJournalpostId;
		private Skjerming skjerming;

		private Builder() {
		}

		public Builder withParent(Journalpost parent) {
			this.parent = parent;
			return this;
		}

		public Builder withDokumentInfoId(String dokumentInfoId) {
			this.dokumentInfoId = dokumentInfoId;
			return this;
		}

		public Builder withTittel(String tittel) {
			this.tittel = tittel;
			return this;
		}

		public Builder withBrevkode(String brevkode) {
			this.brevkode = brevkode;
			return this;
		}

		public Builder withDokumentstatus(Dokumentstatus dokumentstatus) {
			this.dokumentstatus = dokumentstatus;
			return this;
		}

		public Builder withDatoFerdigstilt(LocalDateTime datoFerdigstilt) {
			this.datoFerdigstilt = datoFerdigstilt;
			return this;
		}

		public Builder withOriginalJournalpostId(String originalJournalpostId) {
			this.originalJournalpostId = originalJournalpostId;
			return this;
		}

		public Builder withSkjerming(Skjerming skjerming) {
			this.skjerming = skjerming;
			return this;
		}

		public DokumentInfo build() {
			return new DokumentInfo(this);
		}
	}
}
