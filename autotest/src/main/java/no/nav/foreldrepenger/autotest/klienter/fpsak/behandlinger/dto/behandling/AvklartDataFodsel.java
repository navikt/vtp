package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AvklartDataFodsel {

    protected List<AvklartBarn> avklartBarn;
    protected Boolean brukAntallBarnFraTps;
    protected Boolean erOverstyrt;
    protected LocalDate termindato;
    protected Integer antallBarnTermin;
    protected LocalDate utstedtdato;
    protected Boolean morForSykVedFodsel;
    protected Long vedtaksDatoSomSvangerskapsuke;
}
