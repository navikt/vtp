package no.nav.oppgave;

import io.swagger.annotations.ApiParam;
import no.nav.oppgave.infrastruktur.validering.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.Max;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

@AtleastOneOf(fields = {"statuskategori", "statuser", "aktoerId", "orgnr", "bnr", "samhandlernr"})
public class OppgaveSearchRequest {

    @QueryParam("erUtenMappe")
    @ApiParam(value = "erUtenMappe", example = "true")
    private Boolean erUtenMappe;

    @QueryParam("opprettetAv")
    @ApiParam(value = "Hvem som har opprettet oppgaven", example = "Z990695")
    private String opprettetAv;

    @QueryParam("tildeltRessurs")
    @ApiParam(value = "Om oppgaven er tildelt en ressurs eller ikke", example = "false")
    private Boolean tildeltRessurs;

    @QueryParam("ikkeTidligereTilordnetRessurs")
    @ApiParam(value = "Oppgaven kan ikke tidligere ha vært tilordnet denne ressursen", example = "Z990695")
    private String ikkeTidligereTilordnetRessurs;

    @QueryParam("tilordnetRessurs")
    @ApiParam(value = "Hvilken ressurs oppgaven er tilordnet", example = "Z990695")
    private String tilordnetRessurs;

    @QueryParam("behandlesAvApplikasjon")
    @ApiParam(value = "Hvilken applikasjon oppgaven behandles i, applikasjonskoder finnes i felles kodeverk", example = "AS36")
    private String behandlesAvApplikasjon;

    @IsoDateTime
    @QueryParam("ferdigstiltFom")
    @ApiParam(value = "Nedre grense for filtrering på ferdigstilt tidspunkt", example = "2018-05-24T13:49:00")
    private String ferdigstiltFom;

    @IsoDateTime
    @QueryParam("ferdigstiltTom")
    @ApiParam(value = "Øvre grense for filtrering på ferdigstilt tidspunkt", example = "2018-05-24T13:49:00")
    private String ferdigstiltTom;

    @IsoDateTime
    @QueryParam("opprettetFom")
    @ApiParam(value = "Nedre grense for filtrering på opprettetTidspunkt", example = "2018-05-24T13:49:00")
    private String opprettetFom;

    @IsoDateTime
    @QueryParam("opprettetTom")
    @ApiParam(value = "Øvre grense for filtrering på opprettetTidspunkt", example = "2018-05-25T15:00:00")
    private String opprettetTom;

    @IsoDate
    @QueryParam("fristFom")
    @ApiParam(value = "Nedre grense for filtrering på fristFerdigstillelse", example = "2018-12-10")
    private String fristFom;

    @IsoDate
    @QueryParam("fristTom")
    @ApiParam(value = "Øvre grense for filtrering på fristFerdigstillelse", example = "2018-12-21")
    private String fristTom;

    @IsoDate
    @QueryParam("aktivDatoFom")
    @ApiParam(value = "Nedre grense for filtrering på aktivDato", example = "2018-12-30")
    private String aktivDatoFom;

    @IsoDate
    @QueryParam("aktivDatoTom")
    @ApiParam(value = "Øvre grense for filtrering på aktivDato", example = "2018-11-25")
    private String aktivDatoTom;

    @QueryParam("aktoerId")
    @ApiParam(value = "Filtrering på oppgaver opprettet for en aktør (person)", example = "583957652")
    private String aktoerId;

    @QueryParam("orgnr")
    @ApiParam(value = "Filtrering på oppgaver opprettet for en organisasjon", example = "917755736")
    private String orgnr;

    @QueryParam("bnr")
    @ApiParam(value = "Filtrering på oppgaver opprettet for et bostnummer", example = "11250199559")
    private String bnr;

    @QueryParam("samhandlernr")
    @ApiParam(value = "Filtrering på oppgaver opprettet for en samhandler", example = "80000999999")
    private String samhandlernr;

    @QueryParam("tema")
    @ApiParam(value = "Filtrering på tema (iht felles kodeverk), her kan flere temaer spesifiseres i samme søk", example = "FOR")
    private List<String> temaer;

    @QueryParam("oppgavetype")
    @ApiParam(value = "Filtrering på oppgavetype (iht felles kodeverk), her kan flere oppgavetyper spesifiseres i samme søk", example = "JFR")
    private List<String> oppgavetyper;

    @QueryParam("behandlingstema")
    @ApiParam(value = "Filtrering på behandlingstema (iht felles kodeverk)", example = "ab0317")
    private String behandlingstema;

    @QueryParam("behandlingstype")
    @ApiParam(value = "Filtrering på behandlingstype (iht felles kodeverk)", example = "ae0106")
    private String behandlingstype;

    @QueryParam("tildeltEnhetsnr")
    @ApiParam(value = "Filtrering på tildelt enhet", example = "0100")
    private String tildeltEnhetsnr;

    @QueryParam("opprettetAvEnhetsnr")
    @ApiParam(value = "Filtrering på enhet som opprettet oppgaven", example = "0100")
    private String opprettetAvEnhetsnr;

    @QueryParam("metadatanokkel")
    @ApiParam(value = "Filtrering på metadatanøkkel", example = "REVURDERINGSTYPE")
    private MetadataKey metadatanokkel;

    @QueryParam("metadataverdi")
    @ApiParam(value = "Filtrering på metadataverdi", example = "TILST_DOD")
    private List<String> metadataverdier;

    @QueryParam("journalpostId")
    @ApiParam(value = "Filtrering på journalpost", example = "8953456")
    private List<String> journalpostIds;

    @QueryParam("saksreferanse")
    @ApiParam(value = "Filtrering på saksreferanse", example = "6764567")
    private List<String> saksreferanser;

    @QueryParam("mappeId")
    @ApiParam(value = "Filtrering på mappe", example = "5435")
    private List<Long> mappeIds;

    @OneOrMoreOf(legalValues = {"OPPRETTET", "AAPNET", "UNDER_BEHANDLING", "FERDIGSTILT", "FEILREGISTRERT"}, name = "status")
    @QueryParam("status")
    @ApiParam(value = "Status på oppgaven, her kan flere statuser spesifiseres i samme søk", example = "FERDIGSTILT")
    private List<String> statuser;

    @OneOf(legalValues = {"AAPEN", "AVSLUTTET"}, name = "statuskategori")
    @QueryParam("statuskategori")
    @ApiParam(value = "Statuskategori er en kategorisering av statuser internt i oppgave, dvs at det kan søkes på enten AAPEN eller AVSLUTTET " +
            "og de relevante oppgave vil returneres uten at konsument trenger å spesifisere alle statuser som representerer " +
            "åpne oppgaver eller motsatt (avsluttede oppgaver)", example = "AAPEN")
    private String statuskategori;

    @OneOf(legalValues = {"ASC", "DESC"}, name = "sorteringsrekkefølge")
    @QueryParam("sorteringsrekkefolge")
    @ApiParam(value = "Rekkefølgen på de returnerte oppgavene", example = "ASC")
    @DefaultValue("ASC")
    private String sorteringsrekkefolge;

    @OneOf(legalValues = {"OPPRETTET_TIDSPUNKT", "AKTIV_DATO", "FRIST", "ENDRET_TIDSPUNKT"}, name = "sorteringsfelt")
    @QueryParam("sorteringsfelt")
    @ApiParam(value = "Hvilket felt oppgavene sorteres etter", example = "ENDRET_TIDSPUNKT")
    @DefaultValue("FRIST")
    private String sorteringsfelt;

    @QueryParam("offset")
    @ApiParam(value = "Offset for paginering i søk", example = "10")
    private Long offset;

    @QueryParam("limit")
    @ApiParam(value = "Begrensning i antall returnerte oppgaver", example = "10")
    @DefaultValue("10")
    @Max(message = "{no.nav.oppgave.limit.Size}", value = 1000)
    private Long limit;

    public OppgaveSearchRequest() {
        //JaxRS
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
                .append("aktoerId", aktoerId)
                .append("orgnr", orgnr)
                .append("bnr", bnr)
                .append("tildeltEnhetsnr", tildeltEnhetsnr)
                .append("aktivDatoFom", aktivDatoFom)
                .append("aktivDatoTom", aktivDatoTom)
                .append("fristFom", fristFom)
                .append("fristTom", fristTom)
                .append("opprettetFom", opprettetFom)
                .append("opprettetTom", opprettetTom)
                .append("ferdigstiltFom", ferdigstiltFom)
                .append("ferdigstiltTom", ferdigstiltTom)
                .append("tema", temaer)
                //.append("metadatanokkel", metadatanokkel)
                .append("metadataverdier", metadataverdier)
                .append("oppgavetype", oppgavetyper)
                .append("behandlingstema", behandlingstema)
                .append("behandlingstype", behandlingstype)
                .append("sorteringsfelt", sorteringsfelt)
                .append("sorteringsrekkefolge", sorteringsrekkefolge)
                .append("statuser", statuser)
                .append("statuskategori", statuskategori)
                .append("opprettetAv", opprettetAv)
                .append("tildeltRessurs", tildeltRessurs)
                .append("erUtenMappe", erUtenMappe)
                .append("tilordnetRessurs", tilordnetRessurs)
                .append("ikkeTidligereTilordnetRessurs", ikkeTidligereTilordnetRessurs)
                .append("journalpostIds", journalpostIds)
                .append("saksreferanser", saksreferanser)
                .append("mappeIds", mappeIds)
                .append("behandlesAvApplikasjon", behandlesAvApplikasjon)
                .append("opprettetAvEnhetsnr", opprettetAvEnhetsnr)
                .append("offset", offset)
                .append("limit", limit)
                .toString();
    }

    public String getIkkeTidligereTilordnetRessurs() {
        return ikkeTidligereTilordnetRessurs;
    }

    public void setIkkeTidligereTilordnetRessurs(String ikkeTidligereTilordnetRessurs) {
        this.ikkeTidligereTilordnetRessurs = ikkeTidligereTilordnetRessurs;
    }

    public List<String> getSaksreferanser() {
        return saksreferanser;
    }

    public void setSaksreferanser(List<String> saksreferanser) {
        this.saksreferanser = saksreferanser;
    }

    public String getOpprettetAv() {
        return opprettetAv;
    }

    public void setOpprettetAv(String opprettetAv) {
        this.opprettetAv = opprettetAv;
    }

    public Boolean getTildeltRessurs() {
        return tildeltRessurs;
    }

    public void setTildeltRessurs(Boolean tildeltRessurs) {
        this.tildeltRessurs = tildeltRessurs;
    }

    public List<String> getJournalpostIds() {
        return journalpostIds;
    }

    public void setJournalpostIds(List<String> journalpostIds) {
        this.journalpostIds = journalpostIds;
    }

    public List<Long> getMappeIds() {
        return mappeIds;
    }

    public void setMappeIds(List<Long> mappeIds) {
        this.mappeIds = mappeIds;
    }

    public String getTilordnetRessurs() {
        return tilordnetRessurs;
    }

    public void setTilordnetRessurs(String tilordnetRessurs) {
        this.tilordnetRessurs = tilordnetRessurs;
    }

    public MetadataKey getMetadatanokkel() {
        return metadatanokkel;
    }

    public void setMetadatanokkel(MetadataKey metadatanokkel) {
        this.metadatanokkel = metadatanokkel;
    }

    public List<String> getMetadataverdier() {
        return metadataverdier;
    }

    public void setMetadataverdier(List<String> metadataverdier) {
        this.metadataverdier = metadataverdier;
    }

    public String getFerdigstiltFom() {
        return ferdigstiltFom;
    }

    public void setFerdigstiltFom(String ferdigstiltFom) {
        this.ferdigstiltFom = ferdigstiltFom;
    }

    public String getFerdigstiltTom() {
        return ferdigstiltTom;
    }

    public void setFerdigstiltTom(String ferdigstiltTom) {
        this.ferdigstiltTom = ferdigstiltTom;
    }

    public Boolean getErUtenMappe() {
        return erUtenMappe;
    }

    public void setErUtenMappe(Boolean erUtenMappe) {
        this.erUtenMappe = erUtenMappe;
    }

    public String getOpprettetFom() {
        return opprettetFom;
    }

    public void setOpprettetFom(String opprettetFom) {
        this.opprettetFom = opprettetFom;
    }

    public String getOpprettetTom() {
        return opprettetTom;
    }

    public void setOpprettetTom(String opprettetTom) {
        this.opprettetTom = opprettetTom;
    }

    public String getFristFom() {
        return fristFom;
    }

    public void setFristFom(String fristFom) {
        this.fristFom = fristFom;
    }

    public String getFristTom() {
        return fristTom;
    }

    public void setFristTom(String fristTom) {
        this.fristTom = fristTom;
    }

    public String getAktivDatoFom() {
        return aktivDatoFom;
    }

    public void setAktivDatoFom(String aktivDatoFom) {
        this.aktivDatoFom = aktivDatoFom;
    }

    public String getAktivDatoTom() {
        return aktivDatoTom;
    }

    public void setAktivDatoTom(String aktivDatoTom) {
        this.aktivDatoTom = aktivDatoTom;
    }

    public String getAktoerId() {
        return aktoerId;
    }

    public void setAktoerId(String aktoerId) {
        this.aktoerId = aktoerId;
    }

    public String getOrgnr() {
        return orgnr;
    }

    public void setOrgnr(String orgnr) {
        this.orgnr = orgnr;
    }

    public String getBnr() {
        return bnr;
    }

    public void setBnr(String bnr) {
        this.bnr = bnr;
    }

    public String getSamhandlernr() {
        return samhandlernr;
    }

    public void setSamhandlernr(String samhandlernr) {
        this.samhandlernr = samhandlernr;
    }

    public List<String> getTemaer() {
        return temaer;
    }

    public void setTemaer(List<String> temaer) {
        this.temaer = temaer;
    }

    public List<String> getOppgavetyper() {
        return oppgavetyper;
    }

    public void setOppgavetyper(List<String> oppgavetyper) {
        this.oppgavetyper = oppgavetyper;
    }

    public String getBehandlingstema() {
        return behandlingstema;
    }

    public void setBehandlingstema(String behandlingstema) {
        this.behandlingstema = behandlingstema;
    }

    public String getBehandlingstype() {
        return behandlingstype;
    }

    public void setBehandlingstype(String behandlingstype) {
        this.behandlingstype = behandlingstype;
    }

    public String getTildeltEnhetsnr() {
        return tildeltEnhetsnr;
    }

    public void setTildeltEnhetsnr(String tildeltEnhetsnr) {
        this.tildeltEnhetsnr = tildeltEnhetsnr;
    }

    public List<String> getStatuser() {
        return statuser;
    }

    public void setStatuser(List<String> statuser) {
        this.statuser = statuser;
    }

    public String getStatuskategori() {
        return statuskategori;
    }

    public void setStatuskategori(String statuskategori) {
        this.statuskategori = statuskategori;
    }

    public String getBehandlesAvApplikasjon() {
        return behandlesAvApplikasjon;
    }

    public void setBehandlesAvApplikasjon(String behandlesAvApplikasjon) {
        this.behandlesAvApplikasjon = behandlesAvApplikasjon;
    }

    public String getSorteringsrekkefolge() {
        return sorteringsrekkefolge;
    }

    public void setSorteringsrekkefolge(String sorteringsrekkefolge) {
        this.sorteringsrekkefolge = sorteringsrekkefolge;
    }

    public String getSorteringsfelt() {
        return sorteringsfelt;
    }

    public void setSorteringsfelt(String sorteringsfelt) {
        this.sorteringsfelt = sorteringsfelt;
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public String getOpprettetAvEnhetsnr() {
        return opprettetAvEnhetsnr;
    }

    public void setOpprettetAvEnhetsnr(String opprettetAvEnhetsnr) {
        this.opprettetAvEnhetsnr = opprettetAvEnhetsnr;
    }
}
