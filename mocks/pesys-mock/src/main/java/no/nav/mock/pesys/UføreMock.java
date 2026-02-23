package no.nav.mock.pesys;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import no.nav.mock.pesys.dto.HarUføreGrad;
import no.nav.vtp.person.PersonRepository;
import no.nav.vtp.person.ytelse.YtelseType;

@Path("/api/pesys/ufo")
public class UføreMock {

    private static final String HEADER_FNR = "fnr";

    @Context
    private PersonRepository personRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public HarUføreGrad harUføreGrad(@HeaderParam(HEADER_FNR) String fnr) {
        var person = personRepository.hentPerson(fnr);
        var erUføreOpt = person.ytelser().stream().filter(y -> YtelseType.UFØREPENSJON.equals(y.ytelse())).findFirst();
        if (erUføreOpt.isPresent()) {
            var uføre = erUføreOpt.get();
            return new HarUføreGrad(true, uføre.fom(), uføre.tom());
        }
        return new HarUføreGrad(false, null, null);
    }
}
