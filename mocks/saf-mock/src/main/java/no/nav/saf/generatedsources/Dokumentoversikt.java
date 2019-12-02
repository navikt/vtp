package no.nav.saf.generatedsources;

import java.util.List;

import com.distelli.graphql.ResolveDataFetchingEnvironment;

import graphql.schema.DataFetchingEnvironment;

public interface Dokumentoversikt extends ResolveDataFetchingEnvironment<Dokumentoversikt> {
    public static class Builder {

        private List<Journalpost> _journalposter;

        private SideInfo _sideInfo;

        public Builder() {}
        public Builder(Dokumentoversikt src) {

            _journalposter = src.getJournalposter();

            _sideInfo = src.getSideInfo();

        }


        public Builder withJournalposter(List<Journalpost> _journalposter) {
            this._journalposter = _journalposter;
            return this;
        }

        public Builder withSideInfo(SideInfo _sideInfo) {
            this._sideInfo = _sideInfo;
            return this;
        }

        public Dokumentoversikt build() {
            return new Impl(this);
        }
    }
    public static class Impl implements Dokumentoversikt {

        private List<Journalpost> _journalposter;

        private SideInfo _sideInfo;

        protected Impl(Builder builder) {

            this._journalposter = builder._journalposter;

            this._sideInfo = builder._sideInfo;

        }

        @Override
        public List<Journalpost> getJournalposter() {
            return _journalposter;
        }

        @Override
        public SideInfo getSideInfo() {
            return _sideInfo;
        }

        @Override
        public String toString() {
            return "Dokumentoversikt{"

                 + "journalposter=" + _journalposter

                 + ", sideInfo=" + _sideInfo


                 + "}";
        }

    }

    public default Dokumentoversikt resolve(DataFetchingEnvironment env) {
        return this;
    }

    public default List<Journalpost> getJournalposter() { return null; }
    public default SideInfo getSideInfo() { return null; }

}
