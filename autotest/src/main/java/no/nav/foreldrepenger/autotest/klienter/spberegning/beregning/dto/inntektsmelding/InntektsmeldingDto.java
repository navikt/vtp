package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.inntektsmelding;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.spberegning.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InntektsmeldingDto {

    protected Integer id;
    protected Integer mottattDokumentId;
    protected String orgNr;
    protected String orgNavn;
    protected String arbeidsforholdId;
    protected LocalDate startDatoPermisjon;
    protected LocalDate startDatoArbeidsgiverPerioden;
    protected LocalDate sluttDatoArbeidsgiverPerioden;
    protected Kode begrunnelse;
    protected Boolean relasjonTilBruker;
    protected String refusjonOpphører;
    protected Double refusjonPerMnd;
    protected Double inntektPerMnd;
    protected Double bruttoSykepengerArbedsgiverperioden;
    protected List<UtsettelsePeriodeDto> utsettelsePeriodeDtoer;
    protected List<NaturalYtelseDto> naturalYtelseDtoer;
    protected List<GraderingDto> graderingDtoer;
    
    public InntektsmeldingDto() {
    }
}
