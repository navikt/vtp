package no.nav.saf.generatedsources;

import java.time.LocalDateTime;

import com.distelli.graphql.ResolveDataFetchingEnvironment;

import graphql.schema.DataFetchingEnvironment;

public interface Sak extends ResolveDataFetchingEnvironment<Sak> {
    public static class Builder {

        private String _arkivsaksnummer;

        private Arkivsaksystem _arkivsaksystem;

        private LocalDateTime _datoOpprettet;

        private String _fagsakId;

        private String _fagsaksystem;

        public Builder() {}
        public Builder(Sak src) {

            _arkivsaksnummer = src.getArkivsaksnummer();

            _arkivsaksystem = src.getArkivsaksystem();

            _datoOpprettet = src.getDatoOpprettet();

            _fagsakId = src.getFagsakId();

            _fagsaksystem = src.getFagsaksystem();

        }


        public Builder withArkivsaksnummer(String _arkivsaksnummer) {
            this._arkivsaksnummer = _arkivsaksnummer;
            return this;
        }

        public Builder withArkivsaksystem(Arkivsaksystem _arkivsaksystem) {
            this._arkivsaksystem = _arkivsaksystem;
            return this;
        }

        public Builder withDatoOpprettet(LocalDateTime _datoOpprettet) {
            this._datoOpprettet = _datoOpprettet;
            return this;
        }

        public Builder withFagsakId(String _fagsakId) {
            this._fagsakId = _fagsakId;
            return this;
        }

        public Builder withFagsaksystem(String _fagsaksystem) {
            this._fagsaksystem = _fagsaksystem;
            return this;
        }

        public Sak build() {
            return new Impl(this);
        }
    }
    public static class Impl implements Sak {

        private String _arkivsaksnummer;

        private Arkivsaksystem _arkivsaksystem;

        private LocalDateTime _datoOpprettet;

        private String _fagsakId;

        private String _fagsaksystem;

        protected Impl(Builder builder) {

            this._arkivsaksnummer = builder._arkivsaksnummer;

            this._arkivsaksystem = builder._arkivsaksystem;

            this._datoOpprettet = builder._datoOpprettet;

            this._fagsakId = builder._fagsakId;

            this._fagsaksystem = builder._fagsaksystem;

        }

        @Override
        public String getArkivsaksnummer() {
            return _arkivsaksnummer;
        }

        @Override
        public Arkivsaksystem getArkivsaksystem() {
            return _arkivsaksystem;
        }

        @Override
        public LocalDateTime getDatoOpprettet() {
            return _datoOpprettet;
        }

        @Override
        public String getFagsakId() {
            return _fagsakId;
        }

        @Override
        public String getFagsaksystem() {
            return _fagsaksystem;
        }

        @Override
        public String toString() {
            return "Sak{"

                 + "arkivsaksnummer=" + _arkivsaksnummer

                 + ", arkivsaksystem=" + _arkivsaksystem

                 + ", datoOpprettet=" + _datoOpprettet

                 + ", fagsakId=" + _fagsakId

                 + ", fagsaksystem=" + _fagsaksystem


                 + "}";
        }

    }

    public default Sak resolve(DataFetchingEnvironment env) {
        return this;
    }

    public default String getArkivsaksnummer() { return null; }
    public default Arkivsaksystem getArkivsaksystem() { return null; }
    public default LocalDateTime getDatoOpprettet() { return null; }
    public default String getFagsakId() { return null; }
    public default String getFagsaksystem() { return null; }

}
