package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EndringBeregningsgrunnlagArbeidsforholdDto {

    protected String arbeidsgiverNavn;
    protected String arbeidsgiverId;
    protected LocalDate startdato;
    protected LocalDate opphoersdato;
    protected String arbeidsforholdId;
    protected Kode arbeidsforholdType;
    protected AktørId aktørId;
    protected List<GraderingEllerRefusjonDto> perioderMedGraderingEllerRefusjon;
}
