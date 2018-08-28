package no.nav.foreldrepenger.fpmock2.testmodell.personopplysning;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.fpmock2.testmodell.medlemskap.MedlemskapModell;
import no.nav.foreldrepenger.fpmock2.testmodell.medlemskap.MedlemskapperiodeModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.PersonstatusModell.Personstatuser;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.SivilstandModell.Sivilstander;

public abstract class PersonModell extends BrukerModell {

    /** NAV kodeverk: http://nav.no/kodeverk/Kodeverk/Diskresjonskoder. */
    public enum Diskresjonskoder {
    KLIE, MILI, PEND, SPFO(7), SPSF(6), SVAL, UFB, URIK, UDEF;
        int kode;

        private Diskresjonskoder() {
        }

        private Diskresjonskoder(int kode) {
            this.kode = kode;
        }

    }

    private static final String NB = "NB";

    @JsonProperty("fornavn")
    private String fornavn;

    @JsonProperty("etternavn")
    private String etternavn;

    @JsonProperty("fødselsdato")
    private LocalDate fødselsdato;

    @JsonProperty("dødsdato")
    private LocalDate dødsdato;

    @JsonProperty("diskresjonskode")
    private Diskresjonskoder diskresjonskode;

    @JsonProperty("språk")
    private String språk;

    @JsonProperty("kjønn")
    private Kjønn kjønn;

    @JsonProperty("gjeldendeAdresseType")
    private AdresseType gjeldendeAdresseType;

    @JsonProperty("geografiskTilknytning")
    private GeografiskTilknytningModell geografiskTilknytning = GeografiskTilknytningModell.defaultValue();

    /** Første statsborgerskap ansees som 'gyldig' da TPS returnerer vanligvis kun ett her */
    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("statsborgerskap")
    private List<StatsborgerskapModell> statsborgerskap = new ArrayList<>();

    /** Første sivilstand ansees som 'gyldig' da TPS returnerer vanligvis kun ett her */
    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("sivilstand")
    private List<SivilstandModell> sivilstand = new ArrayList<>();

    /** Første personstatus ansees som 'gyldig' da TPS returnerer vanligvis kun ett her */
    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("personstatus")
    private List<PersonstatusModell> personstatus = new ArrayList<>();

    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("adresser")
    private List<AdresseModell> adresser = new ArrayList<>();

    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("medlemskap")
    private MedlemskapModell medlemskap;

    /** Deler VirksomhetIndeks for et helt scenario for å veksle lokale identer inn i fnr el. */
    @JacksonInject
    private AdresseIndeks adresseIndeks;

    public PersonModell() {
    }

    public PersonModell(String lokalIdent, String navn, LocalDate fødselsdato) {
        super(lokalIdent);

        this.etternavn = navn.contains(" ") ? navn.substring(navn.lastIndexOf(' ')) : navn;
        this.fornavn = navn.contains(" ") ? navn.substring(0, navn.lastIndexOf(' ')) : navn;
        this.fødselsdato = fødselsdato;
    }

    public Optional<AdresseModell> getAdresse(AdresseType adresseType) {
        return getAdresser().stream().filter(a -> a.getAdresseType().equals(adresseType)).findFirst();
    }

    public List<AdresseModell> getAdresser() {
        List<AdresseModell> adresseListe = adresser.stream().map((AdresseModell a) -> {
            if (a instanceof AdresseRefModell) {
                return adresseIndeks.finnFra((AdresseRefModell) a);
            } else {
                return a;
            }
        }).collect(Collectors.toList());

        return Collections.unmodifiableList(adresseListe);
    }

    public List<AdresseModell> getAdresser(AdresseType type) {
        return getAdresser().stream().filter(a -> a.getAdresseType().equals(type)).collect(Collectors.toList());
    }

    public List<PersonstatusModell> getAllePersonstatus() {
        return Collections.unmodifiableList(personstatus);
    }

    public List<StatsborgerskapModell> getAlleStatsborgerskap() {
        return Collections.unmodifiableList(statsborgerskap);
    }

    public String getDiskresjonskode() {
        return diskresjonskode == null ? null : diskresjonskode.name();
    }

    public Diskresjonskoder getDiskresjonskodeType() {
        return diskresjonskode;
    }

    public LocalDate getDødsdato() {
        return dødsdato;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public String getFornavn() {
        return fornavn;
    }

    public LocalDate getFødselsdato() {
        return fødselsdato;
    }

    public GeografiskTilknytningModell getGeografiskTilknytning() {
        return geografiskTilknytning;
    }

    public String getGjeldendeadresseType() {
        if (gjeldendeAdresseType != null && getAdresse(gjeldendeAdresseType).isPresent()) {
            // hvis satt bruk det
            return gjeldendeAdresseType.getTpsKode(getAdresse(gjeldendeAdresseType).get().getLand());
        } else if (!getAdresser().isEmpty()) {
            // plukk første hvis finnes
            AdresseModell adresseModell = getAdresser().get(0);
            return adresseModell.getAdresseType().getTpsKode(adresseModell.getLand());
        } else {
            // ellers ukjent
            return AdresseType.UKJENT_ADRESSE.getTpsKode(null);
        }

    }

    public AdresseType getGjeldendeAdresseType() {
        return gjeldendeAdresseType;
    }

    public Kjønn getKjønn() {
        return kjønn;
    }

    public String getKjønnKode() {
        return getKjønn().name();
    }

    public MedlemskapModell getMedlemskap() {
        return medlemskap;
    }

    public PersonstatusModell getPersonstatus() {
        return personstatus.isEmpty() ? new PersonstatusModell(Personstatuser.BOSA) : personstatus.get(0);
    }

    public SivilstandModell getSivilstand() {
        return sivilstand.isEmpty() ? new SivilstandModell(Sivilstander.UGIF) : sivilstand.get(0);
    }

    public String getSpråk() {
        return språk;
    }

    public String getSpråk2Bokstaver() {
        if (språk == null) {
            return NB;
        } else {
            return språk.substring(0, 2).toUpperCase(Locale.getDefault());
        }
    }

    public StatsborgerskapModell getStatsborgerskap() {
        return statsborgerskap.isEmpty() ? new StatsborgerskapModell(Landkode.NOR) : statsborgerskap.get(0);
    }

    public void leggTil(AdresseModell adresse) {
        this.adresser.add(adresse);
    }

    public void leggTil(PersonstatusModell personstatus) {
        this.personstatus.add(personstatus);
    }

    public void leggTil(SivilstandModell sivilstand) {
        this.sivilstand.add(sivilstand);
    }

    public void leggTil(StatsborgerskapModell statsborgerskap) {
        this.statsborgerskap.add(statsborgerskap);
    }

    public void setMedlemskap(MedlemskapModell medlemskapModell) {
        this.medlemskap = medlemskapModell;
    }

    public void leggTil(MedlemskapperiodeModell medlemskapperiode) {
        if (this.medlemskap == null) {
            setMedlemskap(new MedlemskapModell());
        }
        this.medlemskap.leggTil(medlemskapperiode);
    }

    public void setAdresse(AdresseModell adresse) {
        this.adresser.clear();
        this.adresser.add(adresse);
    }

    public void setDiskresjonskode(Diskresjonskoder diskresjonskode) {
        this.diskresjonskode = diskresjonskode;
    }

    public void setDødsdato(LocalDate dødsdato) {
        this.dødsdato = dødsdato;
    }

    public void setGeografiskTilknytning(GeografiskTilknytningModell geografiskTilknytning) {
        this.geografiskTilknytning = geografiskTilknytning;
    }

    public void setGjeldendeAdresseType(AdresseType gjeldendeAdresseType) {
        this.gjeldendeAdresseType = gjeldendeAdresseType;
    }

    public void setPersonstatus(List<PersonstatusModell> personstatus) {
        this.personstatus = personstatus;
    }

    public void setPersonstatus(PersonstatusModell personstatus) {
        this.personstatus.clear();
        this.personstatus.add(personstatus);
    }

    public void setSivilstand(List<SivilstandModell> sivilstand) {
        this.sivilstand = sivilstand;
    }

    public void setSivilstand(SivilstandModell sivilstand) {
        this.sivilstand.clear();
        this.sivilstand.add(sivilstand);
    }

    public void setSpråk(String språk) {
        this.språk = språk;
    }

    public void setStatsborgerskap(List<StatsborgerskapModell> statsborgerskap) {
        this.statsborgerskap = statsborgerskap;
    }

    public void setStatsborgerskap(StatsborgerskapModell statsborgerskap) {
        this.statsborgerskap.clear();
        this.statsborgerskap.add(statsborgerskap);
    }

    void setEtternavn(String navn) {
        this.etternavn = navn;
    }

    void setFornavn(String fornavn) {
        this.fornavn = fornavn;
    }

    void setFødselsdato(LocalDate fødselsdato) {
        this.fødselsdato = fødselsdato;
    }

    void setKjønn(Kjønn kjønn) {
        this.kjønn = kjønn;
    }
}
