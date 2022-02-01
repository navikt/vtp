package no.nav.mock.pesys;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.VoksenModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.mock.pesys.dto.HentUforehistorikkResponseDto;
import no.nav.mock.pesys.dto.UforeTypeCtiDto;
import no.nav.mock.pesys.dto.UforehistorikkDto;
import no.nav.mock.pesys.dto.UforeperiodeDto;
import no.nav.mock.pesys.dto.UføreTypeCode;

@Path("/api/pesys")
public class PesysMock {

    private static final String HEADER_FNR = "fnr";
    private static final Date FEM_ÅR_SIDEN = convertToDateViaInstant(LocalDate.now().minusYears(5));

    @Context
    private TestscenarioRepository testscenarioRepository;

    public PesysMock() {
    }

    public PesysMock(TestscenarioRepository testscenarioRepository) {
        this.testscenarioRepository = testscenarioRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public HentUforehistorikkResponseDto hentUføreHistorikk(@HeaderParam(HEADER_FNR) String fnr) {
        var personIndeks = testscenarioRepository.getPersonIndeks();
        VoksenModell voksenModell = personIndeks.finnByIdent(fnr);
        if (voksenModell.harUføretrygd()) {
            return lagUføreResponseMedEffektFom5ÅrSiden();
        } else {
            return new HentUforehistorikkResponseDto(new UforehistorikkDto(List.of()));
        }
    }

    protected HentUforehistorikkResponseDto lagUføreResponseMedEffektFom5ÅrSiden() {
        return new HentUforehistorikkResponseDto(
                new UforehistorikkDto(List.of(
                        new UforeperiodeDto(100, FEM_ÅR_SIDEN, FEM_ÅR_SIDEN,
                                new UforeTypeCtiDto(UføreTypeCode.UFORE), FEM_ÅR_SIDEN, FEM_ÅR_SIDEN)
                ))
        );
    }

    private static Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}
