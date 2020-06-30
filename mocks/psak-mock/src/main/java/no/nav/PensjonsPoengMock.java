package no.nav;


import io.swagger.annotations.Api;
import no.nav.domain.OpptjeningsGrunnlag;
import no.nav.domain.Pensjonspoeng;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Api(tags = {"PensjonsPoeng"})
@Path("popp/api")
public class PensjonsPoengMock {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/pensjonspoeng/{pid}")
    public Map<String, Set<Pensjonspoeng>> hentPensjonspoengListe(@PathParam("pid") String pid){
        return Map.of("pensjonspoeng", new HashSet<Pensjonspoeng>());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/opptjeningsgrunnlag/{pid}")
    public Map<String, OpptjeningsGrunnlag> hentOpptjeningsGrunnlag(@PathParam("pid") String pid){
        OpptjeningsGrunnlag grunnlag = new OpptjeningsGrunnlag();
        grunnlag.setDagpengerListe(Collections.emptyList());
        grunnlag.setInntektListe(Collections.emptyList());
        grunnlag.setOmsorgListe(Collections.emptyList());
        return Map.of("opptjeningsGrunnlag", grunnlag);
    }
}
