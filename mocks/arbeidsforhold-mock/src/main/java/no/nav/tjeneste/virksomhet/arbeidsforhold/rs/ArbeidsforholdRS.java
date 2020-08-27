package no.nav.tjeneste.virksomhet.arbeidsforhold.rs;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsforhold;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ArbeidsforholdRS {

    @JsonProperty("arbeidsforholdId")
    private String arbeidsforholdId;
    @JsonProperty("navArbeidsforholdId")
    private Long navArbeidsforholdId;
    @JsonProperty("arbeidsgiver")
    private OpplysningspliktigArbeidsgiverRS arbeidsgiver;
    @JsonProperty("ansettelsesperiode")
    private AnsettelsesperiodeRS ansettelsesperiode;
    @JsonProperty("arbeidsavtaler")
    private List<ArbeidsavtaleRS> arbeidsavtaler;
    @JsonProperty("permisjonPermitteringer")
    private List<PermisjonPermitteringRS> permisjonPermitteringer;
    @JsonProperty("registrert")
    private LocalDate registrert;
    @JsonProperty("type")
    private String type; // (kodeverk: Arbeidsforholdtyper)

    public ArbeidsforholdRS(Arbeidsforhold arbeidsforhold) {
        this.arbeidsforholdId = arbeidsforhold.getArbeidsforholdId();
        this.navArbeidsforholdId = arbeidsforhold.getArbeidsforholdIdnav();
        this.registrert = arbeidsforhold.getAnsettelsesperiodeFom();
        this.type = arbeidsforhold.getArbeidsforholdstype().getKode();
        this.ansettelsesperiode = new AnsettelsesperiodeRS(new PeriodeRS(arbeidsforhold.getAnsettelsesperiodeFom(), arbeidsforhold.getAnsettelsesperiodeTom()));
        this.arbeidsgiver = new OpplysningspliktigArbeidsgiverRS(arbeidsforhold.getArbeidsgiverOrgnr(), arbeidsforhold.getArbeidsgiverAktorId());
        this.permisjonPermitteringer = arbeidsforhold.getPermisjoner().stream()
                .map(PermisjonPermitteringRS::new)
                .collect(Collectors.toList());
        this.arbeidsavtaler = arbeidsforhold.getArbeidsavtaler().stream()
                .map(ArbeidsavtaleRS::new)
                .collect(Collectors.toList());
    }

    public String getArbeidsforholdId() {
        return arbeidsforholdId;
    }

    public Long getNavArbeidsforholdId() {
        return navArbeidsforholdId;
    }

    public OpplysningspliktigArbeidsgiverRS getArbeidsgiver() {
        return arbeidsgiver;
    }

    public AnsettelsesperiodeRS getAnsettelsesperiode() {
        return ansettelsesperiode;
    }

    public List<ArbeidsavtaleRS> getArbeidsavtaler() {
        return arbeidsavtaler;
    }

    public List<PermisjonPermitteringRS> getPermisjonPermitteringer() {
        return permisjonPermitteringer;
    }

    public LocalDate getRegistrert() {
        return registrert;
    }

    public String getType() {
        return type;
    }

}
