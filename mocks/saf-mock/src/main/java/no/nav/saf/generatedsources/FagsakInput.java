package no.nav.saf.generatedsources;


public interface FagsakInput {
    public static class Builder {
        private String _fagsakId; //NOSONAR
        private String _fagsaksystem; //NOSONAR
        public Builder() {}
        public Builder(FagsakInput src) {
            _fagsakId = src.getFagsakId();
            _fagsaksystem = src.getFagsaksystem();
        }

        public Builder withFagsakId(String _fagsakId) { //NOSONAR
            this._fagsakId = _fagsakId;
            return this;
        }
        public Builder withFagsaksystem(String _fagsaksystem) { //NOSONAR
            this._fagsaksystem = _fagsaksystem;
            return this;
        }
        public FagsakInput build() {
            return new Impl(this);
        }
    }
    public static class Impl implements FagsakInput {
        private String _fagsakId; //NOSONAR
        private String _fagsaksystem; //NOSONAR
        protected Impl(Builder builder) {
            this._fagsakId = builder._fagsakId;
            this._fagsaksystem = builder._fagsaksystem;
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
            return "FagsakInput{"
                 + "fagsakId=" + _fagsakId
                 + ", fagsaksystem=" + _fagsaksystem

                 + "}";
        }

    }
    public default String getFagsakId() { return null; }
    public default String getFagsaksystem() { return null; }
}
