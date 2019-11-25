package no.nav.saf.domain.visningsmodell;

import java.util.Objects;

public final class Bruker {
	private final String id;
	private final BrukerIdType type;

	public Bruker(String id, BrukerIdType type) {
		this.id = id;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public BrukerIdType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Bruker{" +
				"id='" + id + '\'' +
				", type=" + type +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Bruker bruker = (Bruker) o;
		return Objects.equals(getId(), bruker.getId()) &&
				getType() == bruker.getType();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getType());
	}
}
