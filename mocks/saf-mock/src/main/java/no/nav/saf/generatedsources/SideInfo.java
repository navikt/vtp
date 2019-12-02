package no.nav.saf.generatedsources;

import com.distelli.graphql.ResolveDataFetchingEnvironment;

import graphql.schema.DataFetchingEnvironment;

public interface SideInfo extends ResolveDataFetchingEnvironment<SideInfo> {
    public static class Builder {

        private String _sluttpeker; //NOSONAR

        private Boolean _finnesNesteSide; //NOSONAR

        public Builder() {}
        public Builder(SideInfo src) {

            _sluttpeker = src.getSluttpeker();

            _finnesNesteSide = src.getFinnesNesteSide();

        }


        public Builder withSluttpeker(String _sluttpeker) { //NOSONAR
            this._sluttpeker = _sluttpeker;
            return this;
        }

        public Builder withFinnesNesteSide(Boolean _finnesNesteSide) { //NOSONAR
            this._finnesNesteSide = _finnesNesteSide;
            return this;
        }

        public SideInfo build() {
            return new Impl(this);
        }
    }
    public static class Impl implements SideInfo {

        private String _sluttpeker; //NOSONAR

        private Boolean _finnesNesteSide; //NOSONAR

        protected Impl(Builder builder) {

            this._sluttpeker = builder._sluttpeker;

            this._finnesNesteSide = builder._finnesNesteSide;

        }

        @Override
        public String getSluttpeker() {
            return _sluttpeker;
        }

        @Override
        public Boolean getFinnesNesteSide() {
            return _finnesNesteSide;
        }

        @Override
        public String toString() {
            return "SideInfo{"

                 + "sluttpeker=" + _sluttpeker

                 + ", finnesNesteSide=" + _finnesNesteSide


                 + "}";
        }

    }

    public default SideInfo resolve(DataFetchingEnvironment env) {
        return this;
    }

    public default String getSluttpeker() { return null; }
    public default Boolean getFinnesNesteSide() { return null; } //NOSONAR

}
