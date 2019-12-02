package no.nav.saf.generatedsources;

import java.time.LocalDateTime;

import com.distelli.graphql.ResolveDataFetchingEnvironment;

import graphql.schema.DataFetchingEnvironment;

public interface RelevantDato extends ResolveDataFetchingEnvironment<RelevantDato> {
    public static class Builder {

        private LocalDateTime  _dato;

        private Datotype _datotype;

        public Builder() {}
        public Builder(RelevantDato src) {

            _dato = src.getDato();

            _datotype = src.getDatotype();

        }


        public Builder withDato(LocalDateTime _dato) {
            this._dato = _dato;
            return this;
        }

        public Builder withDatotype(Datotype _datotype) {
            this._datotype = _datotype;
            return this;
        }

        public RelevantDato build() {
            return new Impl(this);
        }
    }
    public static class Impl implements RelevantDato {

        private LocalDateTime _dato;

        private Datotype _datotype;

        protected Impl(Builder builder) {

            this._dato = builder._dato;

            this._datotype = builder._datotype;

        }

        @Override
        public LocalDateTime getDato() {
            return _dato;
        }

        @Override
        public Datotype getDatotype() {
            return _datotype;
        }

        @Override
        public String toString() {
            return "RelevantDato{"

                 + "dato=" + _dato

                 + ", datotype=" + _datotype


                 + "}";
        }

    }

    public default RelevantDato resolve(DataFetchingEnvironment env) {
        return this;
    }

    public default LocalDateTime getDato() { return null; }
    public default Datotype getDatotype() { return null; }

}
