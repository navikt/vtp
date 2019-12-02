package no.nav.saf.generatedsources;

import java.time.LocalDateTime;
import java.util.List;

import com.distelli.graphql.ResolveDataFetchingEnvironment;

import graphql.schema.DataFetchingEnvironment;

public interface DokumentInfo extends ResolveDataFetchingEnvironment<DokumentInfo> {
    public static class Builder {

        private String _dokumentInfoId;

        private String _tittel;

        private String _brevkode;

        private Dokumentstatus _dokumentstatus;

        private LocalDateTime _datoFerdigstilt;

        private String _originalJournalpostId;

        private String _skjerming;

        private List<LogiskVedlegg> _logiskeVedlegg;

        private List<Dokumentvariant> _dokumentvarianter;

        public Builder() {}
        public Builder(DokumentInfo src) {

            _dokumentInfoId = src.getDokumentInfoId();

            _tittel = src.getTittel();

            _brevkode = src.getBrevkode();

            _dokumentstatus = src.getDokumentstatus();

            _datoFerdigstilt = src.getDatoFerdigstilt();

            _originalJournalpostId = src.getOriginalJournalpostId();

            _skjerming = src.getSkjerming();

            _logiskeVedlegg = src.getLogiskeVedlegg();

            _dokumentvarianter = src.getDokumentvarianter();

        }


        public Builder withDokumentInfoId(String _dokumentInfoId) {
            this._dokumentInfoId = _dokumentInfoId;
            return this;
        }

        public Builder withTittel(String _tittel) {
            this._tittel = _tittel;
            return this;
        }

        public Builder withBrevkode(String _brevkode) {
            this._brevkode = _brevkode;
            return this;
        }

        public Builder withDokumentstatus(Dokumentstatus _dokumentstatus) {
            this._dokumentstatus = _dokumentstatus;
            return this;
        }

        public Builder withDatoFerdigstilt(LocalDateTime _datoFerdigstilt) {
            this._datoFerdigstilt = _datoFerdigstilt;
            return this;
        }

        public Builder withOriginalJournalpostId(String _originalJournalpostId) {
            this._originalJournalpostId = _originalJournalpostId;
            return this;
        }

        public Builder withSkjerming(String _skjerming) {
            this._skjerming = _skjerming;
            return this;
        }

        public Builder withLogiskeVedlegg(List<LogiskVedlegg> _logiskeVedlegg) {
            this._logiskeVedlegg = _logiskeVedlegg;
            return this;
        }

        public Builder withDokumentvarianter(List<Dokumentvariant> _dokumentvarianter) {
            this._dokumentvarianter = _dokumentvarianter;
            return this;
        }

        public DokumentInfo build() {
            return new Impl(this);
        }
    }
    public static class Impl implements DokumentInfo {

        private String _dokumentInfoId;

        private String _tittel;

        private String _brevkode;

        private Dokumentstatus _dokumentstatus;

        private LocalDateTime _datoFerdigstilt;

        private String _originalJournalpostId;

        private String _skjerming;

        private List<LogiskVedlegg> _logiskeVedlegg;

        private List<Dokumentvariant> _dokumentvarianter;

        protected Impl(Builder builder) {

            this._dokumentInfoId = builder._dokumentInfoId;

            this._tittel = builder._tittel;

            this._brevkode = builder._brevkode;

            this._dokumentstatus = builder._dokumentstatus;

            this._datoFerdigstilt = builder._datoFerdigstilt;

            this._originalJournalpostId = builder._originalJournalpostId;

            this._skjerming = builder._skjerming;

            this._logiskeVedlegg = builder._logiskeVedlegg;

            this._dokumentvarianter = builder._dokumentvarianter;

        }

        @Override
        public String getDokumentInfoId() {
            return _dokumentInfoId;
        }

        @Override
        public String getTittel() {
            return _tittel;
        }

        @Override
        public String getBrevkode() {
            return _brevkode;
        }

        @Override
        public Dokumentstatus getDokumentstatus() {
            return _dokumentstatus;
        }

        @Override
        public LocalDateTime getDatoFerdigstilt() {
            return _datoFerdigstilt;
        }

        @Override
        public String getOriginalJournalpostId() {
            return _originalJournalpostId;
        }

        @Override
        public String getSkjerming() {
            return _skjerming;
        }

        @Override
        public List<LogiskVedlegg> getLogiskeVedlegg() {
            return _logiskeVedlegg;
        }

        @Override
        public List<Dokumentvariant> getDokumentvarianter() {
            return _dokumentvarianter;
        }

        @Override
        public String toString() {
            return "DokumentInfo{"

                 + "dokumentInfoId=" + _dokumentInfoId

                 + ", tittel=" + _tittel

                 + ", brevkode=" + _brevkode

                 + ", dokumentstatus=" + _dokumentstatus

                 + ", datoFerdigstilt=" + _datoFerdigstilt

                 + ", originalJournalpostId=" + _originalJournalpostId

                 + ", skjerming=" + _skjerming

                 + ", logiskeVedlegg=" + _logiskeVedlegg

                 + ", dokumentvarianter=" + _dokumentvarianter


                 + "}";
        }

    }

    public default DokumentInfo resolve(DataFetchingEnvironment env) {
        return this;
    }

    public default String getDokumentInfoId() { return null; }
    public default String getTittel() { return null; }
    public default String getBrevkode() { return null; }
    public default Dokumentstatus getDokumentstatus() { return null; }
    public default LocalDateTime getDatoFerdigstilt() { return null; }
    public default String getOriginalJournalpostId() { return null; }
    public default String getSkjerming() { return null; }
    public default List<LogiskVedlegg> getLogiskeVedlegg() { return null; }
    public default List<Dokumentvariant> getDokumentvarianter() { return null; }

}
