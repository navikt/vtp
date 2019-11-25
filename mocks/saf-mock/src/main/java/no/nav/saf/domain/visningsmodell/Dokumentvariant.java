package no.nav.saf.domain.visningsmodell;

import java.util.Objects;

import no.nav.saf.domain.kode.Skjerming;
import no.nav.saf.domain.kode.Variantformat;

public final class Dokumentvariant {
	private final Variantformat variantformat;
	private final String filnavn;
	private final String filuuid;
	private final String filtype;
	private final boolean saksbehandlerHarTilgang;
	private final Skjerming skjerming;

	public Dokumentvariant(Variantformat variantformat, String filnavn, String filuuid, String filtype, boolean saksbehandlerHarTilgang, Skjerming skjerming) {
		this.variantformat = variantformat;
		this.filnavn = filnavn;
		this.filuuid = filuuid;
		this.filtype = filtype;
		this.saksbehandlerHarTilgang = saksbehandlerHarTilgang;
		this.skjerming = skjerming;
	}

	private Dokumentvariant(Builder builder) {
		variantformat = builder.variantformat;
		filnavn = builder.filnavn;
		filuuid = builder.filuuid;
		filtype = builder.filtype;
		saksbehandlerHarTilgang = builder.saksbehandlerHarTilgang;
		skjerming = builder.skjerming;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static Builder builder(Dokumentvariant copy) {
		Builder builder = new Builder();
		builder.variantformat = copy.getVariantformat();
		builder.filnavn = copy.getFilnavn();
		builder.filuuid = copy.getFiluuid();
		builder.filtype = copy.getFiltype();
		builder.saksbehandlerHarTilgang = copy.isSaksbehandlerHarTilgang();
		builder.skjerming = copy.getSkjerming();
		return builder;
	}

	public Variantformat getVariantformat() {
		return variantformat;
	}

	public String getFilnavn() {
		return filnavn;
	}

	public String getFiluuid() {
		return filuuid;
	}

	public String getFiltype() {
		return filtype;
	}

	public boolean isSaksbehandlerHarTilgang() {
		return saksbehandlerHarTilgang;
	}

	public Skjerming getSkjerming() {
		return skjerming;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Dokumentvariant that = (Dokumentvariant) o;
		return isSaksbehandlerHarTilgang() == that.isSaksbehandlerHarTilgang() &&
				getVariantformat() == that.getVariantformat() &&
				Objects.equals(getFilnavn(), that.getFilnavn()) &&
				Objects.equals(getFiluuid(), that.getFiluuid()) &&
				Objects.equals(getFiltype(), that.getFiltype()) &&
				getSkjerming() == that.getSkjerming();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getVariantformat(), getFilnavn(), getFiluuid(), getFiltype(), isSaksbehandlerHarTilgang(), getSkjerming());
	}

	@Override
	public String toString() {
		return "Dokumentvariant{" +
				"variantformat=" + variantformat +
				", filnavn='" + filnavn + '\'' +
				", filuuid='" + filuuid + '\'' +
				", filtype='" + filtype + '\'' +
				", saksbehandlerHarTilgang=" + saksbehandlerHarTilgang +
				", skjerming=" + skjerming +
				'}';
	}


	public static final class Builder {
		private Variantformat variantformat;
		private String filnavn;
		private String filuuid;
		private String filtype;
		private boolean saksbehandlerHarTilgang;
		private Skjerming skjerming;

		private Builder() {
		}

		public Builder withVariantformat(Variantformat variantformat) {
			this.variantformat = variantformat;
			return this;
		}

		public Builder withFilnavn(String filnavn) {
			this.filnavn = filnavn;
			return this;
		}

		public Builder withFiluuid(String filuuid) {
			this.filuuid = filuuid;
			return this;
		}

		public Builder withFiltype(String filtype) {
			this.filtype = filtype;
			return this;
		}

		public Builder withSaksbehandlerHarTilgang(boolean saksbehandlerHarTilgang) {
			this.saksbehandlerHarTilgang = saksbehandlerHarTilgang;
			return this;
		}

		public Builder withSkjerming(Skjerming skjerming) {
			this.skjerming = skjerming;
			return this;
		}

		public Dokumentvariant build() {
			return new Dokumentvariant(this);
		}
	}
}
