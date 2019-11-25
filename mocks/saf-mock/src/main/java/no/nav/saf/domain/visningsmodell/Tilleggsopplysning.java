package no.nav.saf.domain.visningsmodell;

import java.util.Objects;

public final class Tilleggsopplysning {
	private final String nokkel;
	private final String verdi;

	public Tilleggsopplysning(String nokkel, String verdi) {
		this.nokkel = nokkel;
		this.verdi = verdi;
	}

	public String getNokkel() {
		return nokkel;
	}

	public String getVerdi() {
		return verdi;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Tilleggsopplysning that = (Tilleggsopplysning) o;
		return Objects.equals(getNokkel(), that.getNokkel()) &&
				Objects.equals(getVerdi(), that.getVerdi());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getNokkel(), getVerdi());
	}

	@Override
	public String toString() {
		return "Tilleggsopplysning{" +
				"nokkel='" + nokkel + '\'' +
				", verdi='" + verdi + '\'' +
				'}';
	}
}
