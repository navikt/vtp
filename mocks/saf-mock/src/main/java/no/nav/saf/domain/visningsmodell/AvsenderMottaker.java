package no.nav.saf.domain.visningsmodell;

import java.util.Objects;

public final class AvsenderMottaker {
	private final String id;
	private final AvsenderMottakerIdType type;
	private final String navn;
	private final String land;
	private final boolean erLikBruker;

    public AvsenderMottaker(String id, AvsenderMottakerIdType type, String navn, String land, boolean erLikBruker) {
        this.id = id;
        this.type = type;
        this.navn = navn;
        this.land = land;
        this.erLikBruker = erLikBruker;
    }

    public String getId() {
        return id;
    }

    public AvsenderMottakerIdType getType() {
        return type;
    }

    public String getNavn() {
        return navn;
    }

    public String getLand() {
        return land;
    }

    public boolean isErLikBruker() {
        return erLikBruker;
    }

    @Override
    public String toString() {
        return "AvsenderMottaker{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", navn='" + navn + '\'' +
                ", land='" + land + '\'' +
                ", erLikBruker=" + erLikBruker +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvsenderMottaker that = (AvsenderMottaker) o;
        return isErLikBruker() == that.isErLikBruker() &&
                Objects.equals(getId(), that.getId()) &&
                getType() == that.getType() &&
                Objects.equals(getNavn(), that.getNavn()) &&
                Objects.equals(getLand(), that.getLand());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getType(), getNavn(), getLand(), isErLikBruker());
    }
}
