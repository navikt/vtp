package no.nav.foreldrepenger.fpmock2.testmodell.medlemskap;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Landkode;

/**
 * Medlemskapperiode med defaults, de kan overstyrs av json struktur hvis satt
 */
@JsonTypeName("medlemskapperiode")
public class MedlemskapperiodeModell {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(100000000);

    @JsonProperty("id")
    private Long id = ID_GENERATOR.getAndIncrement();

    @JsonProperty("fom")
    private LocalDate fom = LocalDate.now().minusYears(1);

    @JsonProperty("tom")
    private LocalDate tom = LocalDate.now().plusYears(3);

    @JsonProperty("besluttetDato")
    private LocalDate besluttetDato = LocalDate.now().minusYears(1);

    @JsonProperty("land")
    private Landkode landkode = Landkode.DEU; // EØS land

    @JsonProperty("trygdedekning")
    private DekningType dekningType = DekningType.IHT_AVTALE; // setter til en uavklart kode default.

    @JsonProperty("kilde")
    private MedlemskapKildeType kilde = MedlemskapKildeType.TPS;

    @JsonProperty("lovvalgType")
    private LovvalgType lovvalgType = LovvalgType.ENDL;

    @JsonProperty("status")
    private PeriodeStatus status = PeriodeStatus.UAVK;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFom() {
        return fom;
    }

    public void setFom(LocalDate fom) {
        this.fom = fom;
    }

    public LocalDate getTom() {
        return tom;
    }

    public void setTom(LocalDate tom) {
        this.tom = tom;
    }

    public LocalDate getBesluttetDato() {
        return besluttetDato;
    }

    public void setBesluttetDato(LocalDate besluttetDato) {
        this.besluttetDato = besluttetDato;
    }

    public Landkode getLandkode() {
        return landkode;
    }

    public void setLandkode(Landkode landkode) {
        this.landkode = landkode;
    }

    public DekningType getDekningType() {
        return dekningType;
    }

    public void setDekningType(DekningType dekningType) {
        this.dekningType = dekningType;
    }

    public MedlemskapKildeType getKilde() {
        return kilde;
    }

    public void setKilde(MedlemskapKildeType kilde) {
        this.kilde = kilde;
    }

    public LovvalgType getLovvalgType() {
        return lovvalgType;
    }

    public void setLovvalgType(LovvalgType lovvalgType) {
        this.lovvalgType = lovvalgType;
    }

    public PeriodeStatus getStatus() {
        return status;
    }

    public void setStatus(PeriodeStatus status) {
        this.status = status;
    }

}
