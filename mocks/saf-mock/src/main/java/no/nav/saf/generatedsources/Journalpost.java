package no.nav.saf.generatedsources;

import java.time.LocalDateTime;
import java.util.List;

import com.distelli.graphql.ResolveDataFetchingEnvironment;

import graphql.schema.DataFetchingEnvironment;

public interface Journalpost extends ResolveDataFetchingEnvironment<Journalpost> {
    public static class Builder {

        private String _journalpostId;

        private String _tittel;

        private Journalposttype _journalposttype;

        private Journalstatus _journalstatus;

        private Tema _tema;

        private String _temanavn;

        private String _behandlingstema;

        private String _behandlingstemanavn;

        private Sak _sak;

        private Bruker _bruker;

        private AvsenderMottaker _avsenderMottaker;

        private String _avsenderMottakerId;

        private String _avsenderMottakerNavn;

        private String _avsenderMottakerLand;

        private String _journalforendeEnhet;

        private String _journalfoerendeEnhet;

        private String _journalfortAvNavn;

        private String _opprettetAvNavn;

        private Kanal _kanal;

        private String _kanalnavn;

        private String _skjerming;

        private LocalDateTime _datoOpprettet;

        private List<RelevantDato> _relevanteDatoer;

        private String _antallRetur;

        private List<Tilleggsopplysning> _tilleggsopplysninger;

        private List<DokumentInfo> _dokumenter;

        public Builder() {}
        public Builder(Journalpost src) {

            _journalpostId = src.getJournalpostId();

            _tittel = src.getTittel();

            _journalposttype = src.getJournalposttype();

            _journalstatus = src.getJournalstatus();

            _tema = src.getTema();

            _temanavn = src.getTemanavn();

            _behandlingstema = src.getBehandlingstema();

            _behandlingstemanavn = src.getBehandlingstemanavn();

            _sak = src.getSak();

            _bruker = src.getBruker();

            _avsenderMottaker = src.getAvsenderMottaker();

            _avsenderMottakerId = src.getAvsenderMottakerId();

            _avsenderMottakerNavn = src.getAvsenderMottakerNavn();

            _avsenderMottakerLand = src.getAvsenderMottakerLand();

            _journalforendeEnhet = src.getJournalforendeEnhet();

            _journalfoerendeEnhet = src.getJournalfoerendeEnhet();

            _journalfortAvNavn = src.getJournalfortAvNavn();

            _opprettetAvNavn = src.getOpprettetAvNavn();

            _kanal = src.getKanal();

            _kanalnavn = src.getKanalnavn();

            _skjerming = src.getSkjerming();

            _datoOpprettet = src.getDatoOpprettet();

            _relevanteDatoer = src.getRelevanteDatoer();

            _antallRetur = src.getAntallRetur();

            _tilleggsopplysninger = src.getTilleggsopplysninger();

            _dokumenter = src.getDokumenter();

        }


        public Builder withJournalpostId(String _journalpostId) {
            this._journalpostId = _journalpostId;
            return this;
        }

        public Builder withTittel(String _tittel) {
            this._tittel = _tittel;
            return this;
        }

        public Builder withJournalposttype(Journalposttype _journalposttype) {
            this._journalposttype = _journalposttype;
            return this;
        }

        public Builder withJournalstatus(Journalstatus _journalstatus) {
            this._journalstatus = _journalstatus;
            return this;
        }

        public Builder withTema(Tema _tema) {
            this._tema = _tema;
            return this;
        }

        public Builder withTemanavn(String _temanavn) {
            this._temanavn = _temanavn;
            return this;
        }

        public Builder withBehandlingstema(String _behandlingstema) {
            this._behandlingstema = _behandlingstema;
            return this;
        }

        public Builder withBehandlingstemanavn(String _behandlingstemanavn) {
            this._behandlingstemanavn = _behandlingstemanavn;
            return this;
        }

        public Builder withSak(Sak _sak) {
            this._sak = _sak;
            return this;
        }

        public Builder withBruker(Bruker _bruker) {
            this._bruker = _bruker;
            return this;
        }

        public Builder withAvsenderMottaker(AvsenderMottaker _avsenderMottaker) {
            this._avsenderMottaker = _avsenderMottaker;
            return this;
        }

        public Builder withAvsenderMottakerId(String _avsenderMottakerId) {
            this._avsenderMottakerId = _avsenderMottakerId;
            return this;
        }

        public Builder withAvsenderMottakerNavn(String _avsenderMottakerNavn) {
            this._avsenderMottakerNavn = _avsenderMottakerNavn;
            return this;
        }

        public Builder withAvsenderMottakerLand(String _avsenderMottakerLand) {
            this._avsenderMottakerLand = _avsenderMottakerLand;
            return this;
        }

        public Builder withJournalforendeEnhet(String _journalforendeEnhet) {
            this._journalforendeEnhet = _journalforendeEnhet;
            return this;
        }

        public Builder withJournalfoerendeEnhet(String _journalfoerendeEnhet) {
            this._journalfoerendeEnhet = _journalfoerendeEnhet;
            return this;
        }

        public Builder withJournalfortAvNavn(String _journalfortAvNavn) {
            this._journalfortAvNavn = _journalfortAvNavn;
            return this;
        }

        public Builder withOpprettetAvNavn(String _opprettetAvNavn) {
            this._opprettetAvNavn = _opprettetAvNavn;
            return this;
        }

        public Builder withKanal(Kanal _kanal) {
            this._kanal = _kanal;
            return this;
        }

        public Builder withKanalnavn(String _kanalnavn) {
            this._kanalnavn = _kanalnavn;
            return this;
        }

        public Builder withSkjerming(String _skjerming) {
            this._skjerming = _skjerming;
            return this;
        }

        public Builder withDatoOpprettet(LocalDateTime _datoOpprettet) {
            this._datoOpprettet = _datoOpprettet;
            return this;
        }

        public Builder withRelevanteDatoer(List<RelevantDato> _relevanteDatoer) {
            this._relevanteDatoer = _relevanteDatoer;
            return this;
        }

        public Builder withAntallRetur(String _antallRetur) {
            this._antallRetur = _antallRetur;
            return this;
        }

        public Builder withTilleggsopplysninger(List<Tilleggsopplysning> _tilleggsopplysninger) {
            this._tilleggsopplysninger = _tilleggsopplysninger;
            return this;
        }

        public Builder withDokumenter(List<DokumentInfo> _dokumenter) {
            this._dokumenter = _dokumenter;
            return this;
        }

        public Journalpost build() {
            return new Impl(this);
        }
    }
    public static class Impl implements Journalpost {

        private String _journalpostId;

        private String _tittel;

        private Journalposttype _journalposttype;

        private Journalstatus _journalstatus;

        private Tema _tema;

        private String _temanavn;

        private String _behandlingstema;

        private String _behandlingstemanavn;

        private Sak _sak;

        private Bruker _bruker;

        private AvsenderMottaker _avsenderMottaker;

        private String _avsenderMottakerId;

        private String _avsenderMottakerNavn;

        private String _avsenderMottakerLand;

        private String _journalforendeEnhet;

        private String _journalfoerendeEnhet;

        private String _journalfortAvNavn;

        private String _opprettetAvNavn;

        private Kanal _kanal;

        private String _kanalnavn;

        private String _skjerming;

        private LocalDateTime _datoOpprettet;

        private List<RelevantDato> _relevanteDatoer;

        private String _antallRetur;

        private List<Tilleggsopplysning> _tilleggsopplysninger;

        private List<DokumentInfo> _dokumenter;

        protected Impl(Builder builder) {

            this._journalpostId = builder._journalpostId;

            this._tittel = builder._tittel;

            this._journalposttype = builder._journalposttype;

            this._journalstatus = builder._journalstatus;

            this._tema = builder._tema;

            this._temanavn = builder._temanavn;

            this._behandlingstema = builder._behandlingstema;

            this._behandlingstemanavn = builder._behandlingstemanavn;

            this._sak = builder._sak;

            this._bruker = builder._bruker;

            this._avsenderMottaker = builder._avsenderMottaker;

            this._avsenderMottakerId = builder._avsenderMottakerId;

            this._avsenderMottakerNavn = builder._avsenderMottakerNavn;

            this._avsenderMottakerLand = builder._avsenderMottakerLand;

            this._journalforendeEnhet = builder._journalforendeEnhet;

            this._journalfoerendeEnhet = builder._journalfoerendeEnhet;

            this._journalfortAvNavn = builder._journalfortAvNavn;

            this._opprettetAvNavn = builder._opprettetAvNavn;

            this._kanal = builder._kanal;

            this._kanalnavn = builder._kanalnavn;

            this._skjerming = builder._skjerming;

            this._datoOpprettet = builder._datoOpprettet;

            this._relevanteDatoer = builder._relevanteDatoer;

            this._antallRetur = builder._antallRetur;

            this._tilleggsopplysninger = builder._tilleggsopplysninger;

            this._dokumenter = builder._dokumenter;

        }

        @Override
        public String getJournalpostId() {
            return _journalpostId;
        }

        @Override
        public String getTittel() {
            return _tittel;
        }

        @Override
        public Journalposttype getJournalposttype() {
            return _journalposttype;
        }

        @Override
        public Journalstatus getJournalstatus() {
            return _journalstatus;
        }

        @Override
        public Tema getTema() {
            return _tema;
        }

        @Override
        public String getTemanavn() {
            return _temanavn;
        }

        @Override
        public String getBehandlingstema() {
            return _behandlingstema;
        }

        @Override
        public String getBehandlingstemanavn() {
            return _behandlingstemanavn;
        }

        @Override
        public Sak getSak() {
            return _sak;
        }

        @Override
        public Bruker getBruker() {
            return _bruker;
        }

        @Override
        public AvsenderMottaker getAvsenderMottaker() {
            return _avsenderMottaker;
        }

        @Override
        public String getAvsenderMottakerId() {
            return _avsenderMottakerId;
        }

        @Override
        public String getAvsenderMottakerNavn() {
            return _avsenderMottakerNavn;
        }

        @Override
        public String getAvsenderMottakerLand() {
            return _avsenderMottakerLand;
        }

        @Override
        public String getJournalforendeEnhet() {
            return _journalforendeEnhet;
        }

        @Override
        public String getJournalfoerendeEnhet() {
            return _journalfoerendeEnhet;
        }

        @Override
        public String getJournalfortAvNavn() {
            return _journalfortAvNavn;
        }

        @Override
        public String getOpprettetAvNavn() {
            return _opprettetAvNavn;
        }

        @Override
        public Kanal getKanal() {
            return _kanal;
        }

        @Override
        public String getKanalnavn() {
            return _kanalnavn;
        }

        @Override
        public String getSkjerming() {
            return _skjerming;
        }

        @Override
        public LocalDateTime getDatoOpprettet() {
            return _datoOpprettet;
        }

        @Override
        public List<RelevantDato> getRelevanteDatoer() {
            return _relevanteDatoer;
        }

        @Override
        public String getAntallRetur() {
            return _antallRetur;
        }

        @Override
        public List<Tilleggsopplysning> getTilleggsopplysninger() {
            return _tilleggsopplysninger;
        }

        @Override
        public List<DokumentInfo> getDokumenter() {
            return _dokumenter;
        }

        @Override
        public String toString() {
            return "Journalpost{"

                 + "journalpostId=" + _journalpostId

                 + ", tittel=" + _tittel

                 + ", journalposttype=" + _journalposttype

                 + ", journalstatus=" + _journalstatus

                 + ", tema=" + _tema

                 + ", temanavn=" + _temanavn

                 + ", behandlingstema=" + _behandlingstema

                 + ", behandlingstemanavn=" + _behandlingstemanavn

                 + ", sak=" + _sak

                 + ", bruker=" + _bruker

                 + ", avsenderMottaker=" + _avsenderMottaker

                 + ", avsenderMottakerId=" + _avsenderMottakerId

                 + ", avsenderMottakerNavn=" + _avsenderMottakerNavn

                 + ", avsenderMottakerLand=" + _avsenderMottakerLand

                 + ", journalforendeEnhet=" + _journalforendeEnhet

                 + ", journalfoerendeEnhet=" + _journalfoerendeEnhet

                 + ", journalfortAvNavn=" + _journalfortAvNavn

                 + ", opprettetAvNavn=" + _opprettetAvNavn

                 + ", kanal=" + _kanal

                 + ", kanalnavn=" + _kanalnavn

                 + ", skjerming=" + _skjerming

                 + ", datoOpprettet=" + _datoOpprettet

                 + ", relevanteDatoer=" + _relevanteDatoer

                 + ", antallRetur=" + _antallRetur

                 + ", tilleggsopplysninger=" + _tilleggsopplysninger

                 + ", dokumenter=" + _dokumenter


                 + "}";
        }

    }

    public default Journalpost resolve(DataFetchingEnvironment env) {
        return this;
    }

    public default String getJournalpostId() { return null; }
    public default String getTittel() { return null; }
    public default Journalposttype getJournalposttype() { return null; }
    public default Journalstatus getJournalstatus() { return null; }
    public default Tema getTema() { return null; }
    public default String getTemanavn() { return null; }
    public default String getBehandlingstema() { return null; }
    public default String getBehandlingstemanavn() { return null; }
    public default Sak getSak() { return null; }
    public default Bruker getBruker() { return null; }
    public default AvsenderMottaker getAvsenderMottaker() { return null; }
    public default String getAvsenderMottakerId() { return null; }
    public default String getAvsenderMottakerNavn() { return null; }
    public default String getAvsenderMottakerLand() { return null; }
    public default String getJournalforendeEnhet() { return null; }
    public default String getJournalfoerendeEnhet() { return null; }
    public default String getJournalfortAvNavn() { return null; }
    public default String getOpprettetAvNavn() { return null; }
    public default Kanal getKanal() { return null; }
    public default String getKanalnavn() { return null; }
    public default String getSkjerming() { return null; }
    public default LocalDateTime getDatoOpprettet() { return null; }
    public default List<RelevantDato> getRelevanteDatoer() { return null; }
    public default String getAntallRetur() { return null; }
    public default List<Tilleggsopplysning> getTilleggsopplysninger() { return null; }
    public default List<DokumentInfo> getDokumenter() { return null; }

}
