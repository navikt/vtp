package no.nav.saf.domain.visningsmodell;

import no.nav.saf.domain.kode.Arkivsakssystem;

import java.time.LocalDateTime;
import java.util.Objects;

public final class Sak {
	/**
	 * @deprecated NB: Arkivsak skal anses som Joark-internt. Fagsystemene skal kun bruke denne etter avtale.
	 * @since 7.3.8
	 */
	@Deprecated(since = "7.3.8")
	private final String arkivsaksnummer;
	/**
	 * @deprecated NB: Arkivsak skal anses som Joark-internt. Fagsystemene skal kun bruke denne etter avtale.
	 * @since 7.3.8
	 */
	@Deprecated(since = "7.3.8")
	private final Arkivsakssystem arkivsaksystem;
	private final String fagsakId;
	private final String fagsaksystem;
	private final LocalDateTime datoOpprettet;

    public Sak(String arkivsaksnummer, Arkivsakssystem arkivsaksystem, String fagsakId, String fagsaksystem, LocalDateTime datoOpprettet) {
        this.arkivsaksnummer = arkivsaksnummer;
        this.arkivsaksystem = arkivsaksystem;
        this.fagsakId = fagsakId;
        this.fagsaksystem = fagsaksystem;
        this.datoOpprettet = datoOpprettet;
    }

    private Sak(Builder builder) {
        arkivsaksnummer = builder.arkivsaksnummer;
        arkivsaksystem = builder.arkivsaksystem;
        fagsakId = builder.fagsakId;
        fagsaksystem = builder.fagsaksystem;
        datoOpprettet = builder.datoOpprettet;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Sak copy) {
        Builder builder = new Builder();
        builder.arkivsaksnummer = copy.getArkivsaksnummer();
        builder.arkivsaksystem = copy.getArkivsaksystem();
        builder.fagsakId = copy.getFagsakId();
        builder.fagsaksystem = copy.getFagsaksystem();
        builder.datoOpprettet = copy.getDatoOpprettet();
        return builder;
    }

    public String getArkivsaksnummer() {
        return arkivsaksnummer;
    }

    public Arkivsakssystem getArkivsaksystem() {
        return arkivsaksystem;
    }

    public String getFagsakId() {
        return fagsakId;
    }

    public String getFagsaksystem() {
        return fagsaksystem;
    }

    public LocalDateTime getDatoOpprettet() {
        return datoOpprettet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sak sak = (Sak) o;
        return Objects.equals(getArkivsaksnummer(), sak.getArkivsaksnummer()) &&
                getArkivsaksystem() == sak.getArkivsaksystem() &&
                Objects.equals(getFagsakId(), sak.getFagsakId()) &&
                Objects.equals(getFagsaksystem(), sak.getFagsaksystem()) &&
                Objects.equals(getDatoOpprettet(), sak.getDatoOpprettet());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getArkivsaksnummer(), getArkivsaksystem(), getFagsakId(), getFagsaksystem(), getDatoOpprettet());
    }

    @Override
    public String toString() {
        return "Sak{" +
                "arkivsaksnummer='" + arkivsaksnummer + '\'' +
                ", arkivsaksystem=" + arkivsaksystem +
                ", fagsakId='" + fagsakId + '\'' +
                ", fagsaksystem='" + fagsaksystem + '\'' +
                ", datoOpprettet=" + datoOpprettet +
                '}';
    }


    public static final class Builder {
        private String arkivsaksnummer;
        private Arkivsakssystem arkivsaksystem;
        private String fagsakId;
        private String fagsaksystem;
        private LocalDateTime datoOpprettet;

        private Builder() {
        }

        public Builder withArkivsaksnummer(String arkivsaksnummer) {
            this.arkivsaksnummer = arkivsaksnummer;
            return this;
        }

        public Builder withArkivsaksystem(Arkivsakssystem arkivsaksystem) {
            this.arkivsaksystem = arkivsaksystem;
            return this;
        }

        public Builder withFagsakId(String fagsakId) {
            this.fagsakId = fagsakId;
            return this;
        }

        public Builder withFagsaksystem(String fagsaksystem) {
            this.fagsaksystem = fagsaksystem;
            return this;
        }

        public Builder withDatoOpprettet(LocalDateTime datoOpprettet) {
            this.datoOpprettet = datoOpprettet;
            return this;
        }

        public Sak build() {
            return new Sak(this);
        }
    }
}
