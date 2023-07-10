package no.nav.mock.pesys;

import java.time.LocalDate;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.VoksenModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.mock.pesys.dto.HarUføreGrad;

@Path("/api/pesys/ufo")
public class UføreMock {

    private static final String HEADER_FNR = "fnr";
    private static final LocalDate FEM_ÅR_SIDEN = LocalDate.now().minusYears(5);

    @Context
    private TestscenarioRepository testscenarioRepository;

    public UføreMock() {
    }

    public UføreMock(TestscenarioRepository testscenarioRepository) {
        this.testscenarioRepository = testscenarioRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public HarUføreGrad harUføreGrad(@HeaderParam(HEADER_FNR) String fnr) {
        var personIndeks = testscenarioRepository.getPersonIndeks();
        VoksenModell voksenModell = personIndeks.finnByIdent(fnr);

        var innektytelsemodell = testscenarioRepository.getInntektYtelseModell(fnr);
        if (voksenModell.harUføretrygd() || (innektytelsemodell.isPresent() && innektytelsemodell.get().pesysmodell().harUføretrygd())) {
            return new HarUføreGrad(true, FEM_ÅR_SIDEN, FEM_ÅR_SIDEN);
        } else {
            return new HarUføreGrad(false, null, null);
        }
    }
}
