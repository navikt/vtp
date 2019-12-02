package no.nav.saf.generatedsources;

import java.util.List;

import com.distelli.graphql.ResolveDataFetchingEnvironment;

import graphql.schema.DataFetchingEnvironment;

public interface AvsenderMottaker extends ResolveDataFetchingEnvironment<AvsenderMottaker> {
    public static class Builder {

        private String _id;

        private AvsenderMottakerIdType _type;

        private String _navn;

        private String _land;

        private Boolean _erLikBruker;

        public Builder() {}
        public Builder(AvsenderMottaker src) {

            _id = src.getId();

            _type = src.getType();

            _navn = src.getNavn();

            _land = src.getLand();

            _erLikBruker = src.getErLikBruker();

        }


        public Builder withId(String _id) {
            this._id = _id;
            return this;
        }

        public Builder withType(AvsenderMottakerIdType _type) {
            this._type = _type;
            return this;
        }

        public Builder withNavn(String _navn) {
            this._navn = _navn;
            return this;
        }

        public Builder withLand(String _land) {
            this._land = _land;
            return this;
        }

        public Builder withErLikBruker(Boolean _erLikBruker) {
            this._erLikBruker = _erLikBruker;
            return this;
        }

        public AvsenderMottaker build() {
            return new Impl(this);
        }
    }
    public static class Impl implements AvsenderMottaker {

        private String _id;

        private AvsenderMottakerIdType _type;

        private String _navn;

        private String _land;

        private Boolean _erLikBruker;

        protected Impl(Builder builder) {

            this._id = builder._id;

            this._type = builder._type;

            this._navn = builder._navn;

            this._land = builder._land;

            this._erLikBruker = builder._erLikBruker;

        }

        @Override
        public String getId() {
            return _id;
        }

        @Override
        public AvsenderMottakerIdType getType() {
            return _type;
        }

        @Override
        public String getNavn() {
            return _navn;
        }

        @Override
        public String getLand() {
            return _land;
        }

        @Override
        public Boolean getErLikBruker() {
            return _erLikBruker;
        }

        @Override
        public String toString() {
            return "AvsenderMottaker{"

                 + "id=" + _id

                 + ", type=" + _type

                 + ", navn=" + _navn

                 + ", land=" + _land

                 + ", erLikBruker=" + _erLikBruker


                 + "}";
        }

    }

    public default AvsenderMottaker resolve(DataFetchingEnvironment env) {
        return this;
    }

    public static class Unresolved implements AvsenderMottaker {
        private String _id;
        public Unresolved(String id) {
            this._id = id;
        }
        @Override
        public String getId() {
            return _id;
        }
        @Override
        public String toString() {
            return "AvsenderMottaker.Unresolved{"
                 + "id=" + _id
                 + "}";
        }
    }
    public static interface Resolver extends com.distelli.graphql.Resolver<AvsenderMottaker> {
        public List<AvsenderMottaker> resolve(List<AvsenderMottaker> list);
    }
    public default String getId() { return null; }
    public default AvsenderMottakerIdType getType() { return null; }
    public default String getNavn() { return null; }
    public default String getLand() { return null; }
    public default Boolean getErLikBruker() { return null; }

}
