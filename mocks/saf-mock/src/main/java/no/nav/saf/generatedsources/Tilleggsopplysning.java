package no.nav.saf.generatedsources;

import com.distelli.graphql.ResolveDataFetchingEnvironment;

import graphql.schema.DataFetchingEnvironment;

public interface Tilleggsopplysning extends ResolveDataFetchingEnvironment<Tilleggsopplysning> {
    public static class Builder {

        private String _nokkel; //NOSONAR

        private String _verdi; //NOSONAR

        public Builder() {}
        public Builder(Tilleggsopplysning src) {

            _nokkel = src.getNokkel();

            _verdi = src.getVerdi();

        }


        public Builder withNokkel(String _nokkel) { //NOSONAR
            this._nokkel = _nokkel;
            return this;
        }

        public Builder withVerdi(String _verdi) { //NOSONAR
            this._verdi = _verdi;
            return this;
        }

        public Tilleggsopplysning build() {
            return new Impl(this);
        }
    }
    public static class Impl implements Tilleggsopplysning {

        private String _nokkel; //NOSONAR

        private String _verdi; //NOSONAR

        protected Impl(Builder builder) {

            this._nokkel = builder._nokkel;

            this._verdi = builder._verdi;

        }

        @Override
        public String getNokkel() {
            return _nokkel;
        }

        @Override
        public String getVerdi() {
            return _verdi;
        }

        @Override
        public String toString() {
            return "Tilleggsopplysning{"

                 + "nokkel=" + _nokkel

                 + ", verdi=" + _verdi


                 + "}";
        }

    }

    public default Tilleggsopplysning resolve(DataFetchingEnvironment env) {
        return this;
    }

    public default String getNokkel() { return null; }
    public default String getVerdi() { return null; }

}
