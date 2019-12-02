package no.nav.saf.generatedsources;

import com.distelli.graphql.ResolveDataFetchingEnvironment;

import graphql.schema.DataFetchingEnvironment;

public interface Dokumentvariant extends ResolveDataFetchingEnvironment<Dokumentvariant> {
    public static class Builder {

        private Variantformat _variantformat;

        private String _filnavn;

        private String _filuuid;

        private String _filtype;

        private Boolean _saksbehandlerHarTilgang;

        private SkjermingType _skjerming;

        public Builder() {}
        public Builder(Dokumentvariant src) {

            _variantformat = src.getVariantformat();

            _filnavn = src.getFilnavn();

            _filuuid = src.getFiluuid();

            _filtype = src.getFiltype();

            _saksbehandlerHarTilgang = src.getSaksbehandlerHarTilgang();

            _skjerming = src.getSkjerming();

        }


        public Builder withVariantformat(Variantformat _variantformat) {
            this._variantformat = _variantformat;
            return this;
        }

        public Builder withFilnavn(String _filnavn) {
            this._filnavn = _filnavn;
            return this;
        }

        public Builder withFiluuid(String _filuuid) {
            this._filuuid = _filuuid;
            return this;
        }

        public Builder withFiltype(String _filtype) {
            this._filtype = _filtype;
            return this;
        }

        public Builder withSaksbehandlerHarTilgang(Boolean _saksbehandlerHarTilgang) {
            this._saksbehandlerHarTilgang = _saksbehandlerHarTilgang;
            return this;
        }

        public Builder withSkjerming(SkjermingType _skjerming) {
            this._skjerming = _skjerming;
            return this;
        }

        public Dokumentvariant build() {
            return new Impl(this);
        }
    }
    public static class Impl implements Dokumentvariant {

        private Variantformat _variantformat;

        private String _filnavn;

        private String _filuuid;

        private String _filtype;

        private Boolean _saksbehandlerHarTilgang;

        private SkjermingType _skjerming;

        protected Impl(Builder builder) {

            this._variantformat = builder._variantformat;

            this._filnavn = builder._filnavn;

            this._filuuid = builder._filuuid;

            this._filtype = builder._filtype;

            this._saksbehandlerHarTilgang = builder._saksbehandlerHarTilgang;

            this._skjerming = builder._skjerming;

        }

        @Override
        public Variantformat getVariantformat() {
            return _variantformat;
        }

        @Override
        public String getFilnavn() {
            return _filnavn;
        }

        @Override
        public String getFiluuid() {
            return _filuuid;
        }

        @Override
        public String getFiltype() {
            return _filtype;
        }

        @Override
        public Boolean getSaksbehandlerHarTilgang() {
            return _saksbehandlerHarTilgang;
        }

        @Override
        public SkjermingType getSkjerming() {
            return _skjerming;
        }

        @Override
        public String toString() {
            return "Dokumentvariant{"

                 + "variantformat=" + _variantformat

                 + ", filnavn=" + _filnavn

                 + ", filuuid=" + _filuuid

                 + ", filtype=" + _filtype

                 + ", saksbehandlerHarTilgang=" + _saksbehandlerHarTilgang

                 + ", skjerming=" + _skjerming


                 + "}";
        }

    }

    public default Dokumentvariant resolve(DataFetchingEnvironment env) {
        return this;
    }

    public default Variantformat getVariantformat() { return null; }
    public default String getFilnavn() { return null; }
    public default String getFiluuid() { return null; }
    public default String getFiltype() { return null; }
    public default Boolean getSaksbehandlerHarTilgang() { return null; }
    public default SkjermingType getSkjerming() { return null; }

}
