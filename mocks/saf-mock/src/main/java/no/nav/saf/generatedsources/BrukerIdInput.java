package no.nav.saf.generatedsources;

public interface BrukerIdInput {
    public static class Builder {
        private String _id;
        private BrukerIdType _type;
        public Builder() {}
        public Builder(BrukerIdInput src) {
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
        public BrukerIdInput build() {
            return new Impl(this);
        }
    }
    public static class Impl implements BrukerIdInput {
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
            return "BrukerIdInput{"
                 + "id=" + _id
                 + ", type=" + _type

                 + "}";
        }

    }
    public default String getId() { return null; }
    public default BrukerIdType getType() { return null; }
}
