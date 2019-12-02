package no.nav.saf.generatedsources;

import com.distelli.graphql.ResolveDataFetchingEnvironment;

import graphql.schema.DataFetchingEnvironment;

public interface LogiskVedlegg extends ResolveDataFetchingEnvironment<LogiskVedlegg> {
    public static class Builder {

        private String _logiskVedleggId; //NOSONAR

        private String _tittel; //NOSONAR

        public Builder() {}
        public Builder(LogiskVedlegg src) {

            _logiskVedleggId = src.getLogiskVedleggId();

            _tittel = src.getTittel();

        }

        public Builder withLogiskVedleggId(String _logiskVedleggId) { //NOSONAR
            this._logiskVedleggId = _logiskVedleggId;
            return this;
        }

        public Builder withTittel(String _tittel) { //NOSONAR
            this._tittel = _tittel;
            return this;
        }

        public LogiskVedlegg build() {
            return new Impl(this);
        }
    }
    public static class Impl implements LogiskVedlegg {

        private String _logiskVedleggId; //NOSONAR

        private String _tittel; //NOSONAR

        protected Impl(Builder builder) {

            this._logiskVedleggId = builder._logiskVedleggId;

            this._tittel = builder._tittel;

        }

        @Override
        public String getLogiskVedleggId() {
            return _logiskVedleggId;
        }

        @Override
        public String getTittel() {
            return _tittel;
        }

        @Override
        public String toString() {
            return "LogiskVedlegg{"

                 + "logiskVedleggId=" + _logiskVedleggId

                 + ", tittel=" + _tittel


                 + "}";
        }

    }

    public default LogiskVedlegg resolve(DataFetchingEnvironment env) {
        return this;
    }

    public default String getLogiskVedleggId() { return null; }
    public default String getTittel() { return null; }

}
