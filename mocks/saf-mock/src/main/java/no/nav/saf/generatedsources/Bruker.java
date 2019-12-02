package no.nav.saf.generatedsources;

import java.util.List;

import com.distelli.graphql.ResolveDataFetchingEnvironment;

import graphql.schema.DataFetchingEnvironment;

public interface Bruker extends ResolveDataFetchingEnvironment<Bruker> {
    public static class Builder {

        private String _id;

        private BrukerIdType _type;

        public Builder() {}
        public Builder(Bruker src) {

            _id = src.getId();

            _type = src.getType();

        }


        public Builder withId(String _id) {
            this._id = _id;
            return this;
        }

        public Builder withType(BrukerIdType _type) {
            this._type = _type;
            return this;
        }

        public Bruker build() {
            return new Impl(this);
        }
    }
    public static class Impl implements Bruker {

        private String _id;

        private BrukerIdType _type;

        protected Impl(Builder builder) {

            this._id = builder._id;

            this._type = builder._type;

        }

        @Override
        public String getId() {
            return _id;
        }

        @Override
        public BrukerIdType getType() {
            return _type;
        }

        @Override
        public String toString() {
            return "Bruker{"

                 + "id=" + _id

                 + ", type=" + _type


                 + "}";
        }

    }

    public default Bruker resolve(DataFetchingEnvironment env) {
        return this;
    }

    public static class Unresolved implements Bruker {
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
            return "Bruker.Unresolved{"
                 + "id=" + _id
                 + "}";
        }
    }
    public static interface Resolver extends com.distelli.graphql.Resolver<Bruker> {
        public List<Bruker> resolve(List<Bruker> list);
    }
    public default String getId() { return null; }
    public default BrukerIdType getType() { return null; }

}
