package no.nav.oppgave;

import static no.nav.oppgave.Oppgavestatus.FEILREGISTRERT;
import static no.nav.oppgave.Oppgavestatus.FERDIGSTILT;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import no.nav.oppgave.infrastruktur.validering.AtMostOneOf;
import no.nav.oppgave.infrastruktur.validering.AtleastOneOf;
import no.nav.oppgave.infrastruktur.validering.Organisasjonsnummer;

@SuppressWarnings("WeakerAccess")
@AtleastOneOf(fields = {"temagruppe", "tema"})
@AtMostOneOf(fields = {"aktoerId", "orgnr", "bnr", "samhandlernr"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OppgaveJson {
    private Long id;

    @NotBlank(message = "{no.nav.oppgave.tildeltEnhetsnr.NotNull}")
    @Size(message = "{no.nav.oppgave.tildeltEnhetsnr.Size}", min = 4, max = 4)
    private String tildeltEnhetsnr;

    @Size(message = "{no.nav.oppgave.endretAvEnhetsnr.Size}", min = 4, max = 4)
    private String endretAvEnhetsnr;

    @JsonIgnore
    @Size(message = "{no.nav.oppgave.opprettetAvEnhetsnr.Size}", min = 4, max = 4)
    private String opprettetAvEnhetsnr;

    @Size(message = "{no.nav.oppgave.journalpostId.Size}", max = 40)
    private String journalpostId;

    @Size(message = "{no.nav.oppgave.journalpostkilde.Size}", max = 40)
    private String journalpostkilde;

    @Size(message = "{no.nav.oppgave.behandlesAvApplikasjon.Size}", max = 40)
    private String behandlesAvApplikasjon;

    @Size(message = "{no.nav.oppgave.saksreferanse.Size}", max = 40)
    private String saksreferanse;

    @Size(message = "{no.nav.oppgave.bnr.Size}", max = 40)
    private String bnr;

    @Size(message = "{no.nav.oppgave.samhandlernr.Size}", max = 40)
    private String samhandlernr;

    @Size(message = "{no.nav.oppgave.akoerId.Size}", max = 40)
    private String aktoerId;

    @Organisasjonsnummer
    @Size(message = "{no.nav.oppgave.orgnr.Size}", max = 9)
    private String orgnr;

    @Size(message = "{no.nav.oppgave.tilordnetRessurs.Size}", max = 7)
    private String tilordnetRessurs;

    private String beskrivelse;

    @Size(message = "{no.nav.oppgave.temagruppe.Size}", max = 255)
    private String temagruppe;

    @Size(message = "{no.nav.oppgave.tema.Size}", max = 255)
    private String tema;

    @Size(message = "{no.nav.oppgave.behandlingstema.Size}", max = 255)
    private String behandlingstema;

    @NotBlank(message = "{no.nav.oppgave.oppgavetype.NotNull}")
    @Size(message = "{no.nav.oppgave.oppgavetype.Size}", max = 255)
    private String oppgavetype;

    @Size(message = "{no.nav.oppgave.behandlingstype.Size}", max = 255)
    private String behandlingstype;

    @NotNull(message = "{no.nav.oppgave.versjon.NotNull}")
    private Integer versjon;

    private Long mappeId;

    private LocalDate fristFerdigstillelse;

    @NotNull(message = "{no.nav.oppgave.aktivDato.NotNull}")
    private LocalDate aktivDato;

    @JsonIgnore
    private LocalDateTime opprettetTidspunkt;

    @JsonIgnore
    private String opprettetAv;

    @JsonIgnore
    private String endretAv;

    @JsonIgnore
    private LocalDateTime ferdigstiltTidspunkt;

    @JsonIgnore
    private LocalDateTime endretTidspunkt;

    @NotNull(message = "{no.nav.oppgave.prioritet.NotNull}")
    private Prioritet prioritet;

    @NotNull(message = "{no.nav.oppgave.status.NotNull}")
    private Oppgavestatus status;

    private Map<MetadataKey, String> metadata;

    public OppgaveJson() {
        //JaxRS
    }

    OppgaveJson(Oppgave oppgave) {
        this.id = oppgave.getId();
        this.tildeltEnhetsnr = oppgave.getTildeltEnhetsnr();
        this.endretAvEnhetsnr = oppgave.getEndretAvEnhetsnr();
        this.opprettetAvEnhetsnr = oppgave.getOpprettetAvEnhetsnr();
        this.journalpostId = oppgave.getJournalpostId();
        this.journalpostkilde = oppgave.getJournalpostkilde();
        this.behandlesAvApplikasjon = oppgave.getBehandlesAvApplikasjon();
        this.saksreferanse = oppgave.getSaksreferanse();
        this.tilordnetRessurs = oppgave.getTilordnetRessurs();
        this.beskrivelse = oppgave.getBeskrivelse();
        this.temagruppe = oppgave.getTemagruppe();
        this.tema = oppgave.getTema();
        this.behandlingstema = oppgave.getBehandlingstema();
        this.oppgavetype = oppgave.getOppgavetype();
        this.behandlingstype  = oppgave.getBehandlingstype();
        this.versjon = oppgave.getVersjon();
        this.mappeId = oppgave.getMappeId();
        this.fristFerdigstillelse = oppgave.getFristFerdigstillelse();
        this.aktivDato = oppgave.getAktivDato();
        this.opprettetTidspunkt = oppgave.getOpprettetTidspunkt();
        this.ferdigstiltTidspunkt = oppgave.getFerdigstiltTidspunkt();
        this.endretTidspunkt = oppgave.getEndretTidspunkt();
        this.prioritet = oppgave.getPrioritet();
        this.status = oppgave.getStatus();
        this.opprettetAv = oppgave.getOpprettetAv();
        this.endretAv = oppgave.getEndretAv();
        this.metadata = oppgave.getMetadata();
    }

    @JsonProperty("id")
    @ApiModelProperty(value = "Syntetisk id", example = "5436732")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("tildeltEnhetsnr")
    @ApiModelProperty(value = "Enheten oppgaven er tildelt", example = "0100")
    public String getTildeltEnhetsnr() {
        return tildeltEnhetsnr;
    }

    public void setTildeltEnhetsnr(String tildeltEnhetsnr) {
        this.tildeltEnhetsnr = tildeltEnhetsnr;
    }

    @JsonProperty("endretAvEnhetsnr")
    @ApiModelProperty(value = "Enheten som endret oppgaven", example = "0101")
    public String getEndretAvEnhetsnr() {
        return endretAvEnhetsnr;
    }

    public void setEndretAvEnhetsnr(String endretAvEnhetsnr) {
        this.endretAvEnhetsnr = endretAvEnhetsnr;
    }

    @JsonProperty("opprettetAvEnhetsnr")
    @ApiModelProperty(value = "Hvilken enhet som har opprettet oppgaven", example = "0200")
    public String getOpprettetAvEnhetsnr() {
        return opprettetAvEnhetsnr;
    }

    @JsonProperty("journalpostId")
    @ApiModelProperty(value = "Id for en journalpostreferanse", example = "84938201")
    public String getJournalpostId() {
        return journalpostId;
    }

    public void setJournalpostId(String journalpostId) {
        this.journalpostId = journalpostId;
    }

    @JsonProperty("journalpostkilde")
    @ApiModelProperty(value = "Hvilken applikasjon journalposten kommer fra, applikasjonskoder finnes i felles kodeverk", example = "AS36")
    public String getJournalpostkilde() {
        return journalpostkilde;
    }

    public void setJournalpostkilde(String journalpostkilde) {
        this.journalpostkilde = journalpostkilde;
    }

    @JsonProperty("behandlesAvApplikasjon")
    @ApiModelProperty(value = "Hvilken applikasjon oppgaven skal behandles i, applikasjonskoder finnes i felles kodeverk", example = "FS22")
    public String getBehandlesAvApplikasjon() {
        return behandlesAvApplikasjon;
    }

    public void setBehandlesAvApplikasjon(String behandlesAvApplikasjon) {
        this.behandlesAvApplikasjon = behandlesAvApplikasjon;
    }

    @JsonProperty("saksreferanse")
    @ApiModelProperty(value = "Id for en saksreferanse", example = "84942299")
    public String getSaksreferanse() {
        return saksreferanse;
    }

    public void setSaksreferanse(String saksreferanse) {
        this.saksreferanse = saksreferanse;
    }

    @JsonProperty("aktoerId")
    @ApiModelProperty(value = "Syntetisk id for en person, kan hentes fra TPS", example = "123456789")
    public String getAktoerId() {
        return aktoerId;
    }

    @JsonProperty("orgnr")
    @ApiModelProperty(value = "Organisasjonsnummer", example = "979312059")
    public String getOrgnr() {
        return orgnr;
    }

    @JsonProperty("bnr")
    @ApiModelProperty(value = "Bostnummer", example = "11250199559")
    public String getBnr() {
        return bnr;
    }

    @JsonProperty("samhandlernr")
    @ApiModelProperty(value = "Samnhandlernummer", example = "80000999999")
    public String getSamhandlernr() {
        return samhandlernr;
    }

    @JsonProperty("tilordnetRessurs")
    @ApiModelProperty(value = "Ressurssen som er tilordnet oppgaven, gjerne en saksbehandler", example = "Z998323")
    public String getTilordnetRessurs() {
        return tilordnetRessurs;
    }

    public void setTilordnetRessurs(String tilordnetRessurs) {
        this.tilordnetRessurs = tilordnetRessurs;
    }

    @JsonProperty("beskrivelse")
    @ApiModelProperty(value = "Beskrivelse av oppgaven")
    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    @JsonProperty("temagruppe")
    @ApiModelProperty(value = "Temagruppe slik den er definert i felles kodeverk", example = "ANSOS")
    public String getTemagruppe() {
        return temagruppe;
    }

    public void setTemagruppe(String temagruppe) {
        this.temagruppe = temagruppe;
    }

    @JsonProperty("tema")
    @ApiModelProperty(value = "Tema slik den er definert i felles kodeverk", example = "AAP")
    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    @JsonProperty("behandlingstema")
    @ApiModelProperty(value = "Behandlingstema slik den er definert i felles kodeverk", example = "ab0203")
    public String getBehandlingstema() {
        return behandlingstema;
    }

    public void setBehandlingstema(String behandlingstema) {
        this.behandlingstema = behandlingstema;
    }

    @JsonProperty("oppgavetype")
    @ApiModelProperty(value = "Oppgavetype slik den er definert i felles kodeverk", example = "HAST_BANK_OPPLYS")
    public String getOppgavetype() {
        return oppgavetype;
    }

    public void setOppgavetype(String oppgavetype) {
        this.oppgavetype = oppgavetype;
    }

    @JsonProperty("behandlingstype")
    @ApiModelProperty(value = "Behandlingstype slik den er definert i felles kodeverk", example = "ae0001")
    public String getBehandlingstype() {
        return behandlingstype;
    }

    public void setBehandlingstype(String behandlingstype) {
        this.behandlingstype = behandlingstype;
    }

    @JsonProperty("versjon")
    @ApiModelProperty(value = "Brukes for å håndtere optimistisk låsing, hvor to brukere har skrevet på samme oppgave. " +
        "Hver gang det gjøres endring på en oppgave, økes verdien av VERSJON med 1. Når en klient skal lagre endring på " +
        "en oppgave, sendes verdien av VERSJON oppgaven hadde da informasjon om oppgaven som nå er endret ble hentet ut. " +
        "Dersom verdien av VERSJON er endret, har noen andre lagret oppgaven i mellomtiden, og oppgaven kan ikke lagres. " +
        "409 Conflict vil returneres fra tjenesten. Under oppretting av oppgave trenger man ikke å spesifisere noen verdi for dette feltet, " +
        "oppgaven vil starte på versjon 1", example = "1")
    public Integer getVersjon() {
        return versjon;
    }

    public void setVersjon(Integer versjon) {
        this.versjon = versjon;
    }

    @JsonProperty("mappeId")
    @ApiModelProperty(value = "Mapper styres av Oppgave. En oversikt kan hentes fra endepunktet for mapper", example = "848")
    public Long getMappeId() {
        return mappeId;
    }

    public void setMappeId(Long mappeId) {
        this.mappeId = mappeId;
    }

    @JsonProperty("fristFerdigstillelse")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @ApiModelProperty(value = "Oppgavens frist for ferdigstillelse.", example = "2018-03-24")
    public String getFristFerdigstillelse() {
        return fristFerdigstillelse != null ? DateTimeFormatter.ISO_LOCAL_DATE.format(fristFerdigstillelse) : null;
    }

    public void setFristFerdigstillelse(LocalDate fristFerdigstillelse) {
        this.fristFerdigstillelse = fristFerdigstillelse;
    }

    @JsonProperty("aktivDato")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @ApiModelProperty(value = "Brukes av saksbehandlere for å planlegge arbeidsdagene sine, aktivDato forteller noe " +
        "om når saksbehandler selv ønsker å begynne å jobbe på oppgaven", example = "2018-03-10")
    public String getAktivDato() {
        return DateTimeFormatter.ISO_LOCAL_DATE.format(aktivDato);
    }

    public void setAktivDato(LocalDate aktivDato) {
        this.aktivDato = aktivDato;
    }

    @JsonProperty("opprettetTidspunkt")
    @ApiModelProperty("Opprettet tidspunkt iht. ISO-8601")
    public String getOpprettetTidspunkt() {
        return ZonedDateTime.of(opprettetTidspunkt, ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    @JsonProperty("opprettetAv")
    @ApiModelProperty(value = "Hvilken bruker eller system som opprettet oppgaven")
    public String getOpprettetAv() {
        return opprettetAv;
    }

    @JsonProperty("endretAv")
    @ApiModelProperty(value = "Hvilken bruker eller system som endret oppgaven sist")
    public String getEndretAv() {
        return endretAv;
    }


    @JsonProperty("status")
    @ApiModelProperty(value = "Hvilken status oppgaven har", example = "UNDER_BEHANDLING")
    public Oppgavestatus getStatus() {
        return status;
    }

    public void setStatus(Oppgavestatus status) {
        this.status = status;
    }

    @JsonProperty("ferdigstiltTidspunkt")
    @ApiModelProperty(value = "Tidspunktet oppgaven ble ferdigstilt iht. ISO-8601")
    public String getFerdigstiltTidspunkt() {
        return ferdigstiltTidspunkt != null ? ZonedDateTime.of(ferdigstiltTidspunkt, ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) : null;
    }

    @JsonProperty("endretTidspunkt")
    @ApiModelProperty(value = "Endret tidspunkt iht. ISO-8601")
    public String getEndretTidspunkt() {
        return endretTidspunkt != null ? ZonedDateTime.of(endretTidspunkt, ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) : null;
    }

    @JsonProperty("metadata")
    @ApiModelProperty(value = "Metadata for oppgaven. Dette er en JSON-Objekt struktur (key value pair/hash map) som kan brukes for informasjon som ikke passer " +
        "direkte inn i hovedmodellen til oppgave, men som fortsatt er nødvendig for en konsument. På dette feltet vil det komme noen restriksjoner " +
        "så vennligst ta kontakt med oss om deres applikasjon har behov for å ta dette i bruk")
    public Map<MetadataKey, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<MetadataKey, String> metadata) {
        this.metadata = metadata;
    }

    Oppgave toOppgave(String user/*, Ident ident*/) {
        return new Oppgave.Builder()
            .medId(id)
            //.medIdent(domeneIdent)
            .medJournalpostId(journalpostId)
            .medJournalpostkilde(journalpostkilde)
            .medBehandlesAvApplikasjon(behandlesAvApplikasjon)
            .medSaksreferanse(saksreferanse)
            .medTemagruppe(temagruppe)
            .medTema(tema)
            .medBehandlingstema(behandlingstema)
            .medOppgavetype(oppgavetype)
            .medBehandlingsType(behandlingstype)
            .medAktivDato(aktivDato)
            .medPrioritet(prioritet)
            .medTildeltEnhetsnr(tildeltEnhetsnr)
            .medEndretAvEnhetsnr(endretAvEnhetsnr)
            .medTilordnetRessurs(tilordnetRessurs)
            .medBeskrivelse(beskrivelse)
            .medMappeId(mappeId)
            .medVersjon(versjon)
            .medStatus(status)
            .medMetadata(metadata)
            .medFristFerdigstillelse(fristFerdigstillelse)
            .medEndretTidspunkt(LocalDateTime.now())
            .medFerdigstiltTidspunkt((FERDIGSTILT.equals(status) || FEILREGISTRERT.equals(status) ? LocalDateTime.now() : null))
            .medEndretAv(user)
            .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("tildeltEnhetsnr", tildeltEnhetsnr)
            .append("endretAvEnhetsnr", endretAvEnhetsnr)
            .append("journalpostId", journalpostId)
            .append("journalpostkilde", journalpostkilde)
            .append("behandlesAvApplikasjon", behandlesAvApplikasjon)
            .append("saksreferanse", saksreferanse)
            .append("aktoerId", aktoerId)
            .append("orgnr", orgnr)
            .append("bnr", bnr)
            .append("samhandlernr", samhandlernr)
            .append("tilordnetRessurs", tilordnetRessurs)
            .append("temagruppe", temagruppe)
            .append("tema", tema)
            .append("behandlingstema", behandlingstema)
            .append("oppgavetype", oppgavetype)
            .append("behandlingstype", behandlingstype)
            .append("versjon", versjon)
            .append("mappeId", mappeId)
            .append("fristFerdigstillelse", fristFerdigstillelse)
            .append("aktivDato", aktivDato)
            .append("opprettetTidspunkt", opprettetTidspunkt)
            .append("ferdigstiltTidspunkt", ferdigstiltTidspunkt)
            .append("endretTidspunkt", endretTidspunkt)
            .append("prioritet", prioritet)
            .append("status", status)
            .append("opprettetAv", opprettetAv)
            .append("endretAv", endretAv)
            .append("metadata", metadata)
            .append("beskrivelse", "*****")
            .toString();
    }
}
