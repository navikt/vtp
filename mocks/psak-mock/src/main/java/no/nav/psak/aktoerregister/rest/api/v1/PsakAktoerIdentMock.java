package no.nav.psak.aktoerregister.rest.api.v1;

import io.swagger.annotations.Api;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Api(tags = {"aktoerregister"})
@Path("/psak/aktoerregister/api/v1/identer")
public class PsakAktoerIdentMock {

    private static final String NAV_IDENTER_HEADER_KEY = "Nav-Personidenter";
    private static final int NAV_IDENTER_MAX_SIZE = 1000;
    private static final String IDENTGRUPPE = "identgruppe";
    private static final String AKTOERID_IDENTGRUPPE = "AktoerId";
    private static final String PERSONIDENT_IDENTGRUPPE = "NorskIdent";
    private static final String GJELDENDE = "gjeldende";

    //TODO (TEAM FAMILIE) Lag mock-responser fra scenario NOSONAR
    private String personIdentMock = "12345678910";
    private String aktørIdMock = "1234567891011";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, IdentinfoForAktoer> getIdenter(@HeaderParam(NAV_IDENTER_HEADER_KEY) Set<String> requestIdenter,
                                                      @NotNull  @QueryParam(IDENTGRUPPE) String identgruppe,
                                                      @NotNull  @QueryParam(GJELDENDE) boolean gjeldende) {
        validateRequest(requestIdenter);
        Identinfo identinfo;
        if (AKTOERID_IDENTGRUPPE.equals(identgruppe)) {
            identinfo = new Identinfo(personIdentMock, PERSONIDENT_IDENTGRUPPE, true);
        } else {
            identinfo = new Identinfo(aktørIdMock, AKTOERID_IDENTGRUPPE, true);
        }
        Map<String, IdentinfoForAktoer> resultMap = new HashMap<>();
        resultMap.put(requestIdenter.stream().findFirst().orElseThrow(IllegalArgumentException::new),
                new IdentinfoForAktoer(List.of(identinfo), null));
        return resultMap;
    }

    private void validateRequest(Set<String> identer) {
        if (identer.isEmpty()) {
            throw new IllegalArgumentException("Ville kastet \"MissingIdenterException\"");
        } else if (identer.size() > NAV_IDENTER_MAX_SIZE) {
            throw new IllegalArgumentException("Ville kastet \"RequestIdenterMaxSizeException\"");
        }
    }

    public static class Identinfo {

        private String ident;
        private String identgruppe;
        private Boolean gjeldende;

        public Identinfo() {

        }

        public Identinfo(String ident, String identgruppe, Boolean gjeldende) {
            this.ident = ident;
            this.identgruppe = identgruppe;
            this.gjeldende = gjeldende;
        }

        public String getIdent() {
            return ident;
        }

        public void setIdent(String ident) {
            this.ident = ident;
        }

        public String getIdentgruppe() {
            return identgruppe;
        }

        public void setIdentgruppe(String identgruppe) {
            this.identgruppe = identgruppe;
        }

        public Boolean getGjeldende() {
            return gjeldende;
        }

        public void setGjeldende(Boolean gjeldende) {
            this.gjeldende = gjeldende;
        }

        @Override
        public String toString() {
            return "Identinfo{" + "ident=" + ident +
                    ", identgruppe=" + identgruppe +
                    ", gjeldende=" + gjeldende +
                    '}';
        }
    }

    public static class IdentinfoForAktoer {

        private List<Identinfo> identer;
        private String feilmelding;

        public IdentinfoForAktoer() {
        }

        public IdentinfoForAktoer(List<Identinfo> identer, String feilmelding) {
            this.identer = identer;
            this.feilmelding = feilmelding;
        }

        public List<Identinfo> getIdenter() {
            return identer;
        }

        public void setIdenter(List<Identinfo> identer) {
            this.identer = identer;
        }

        public String getFeilmelding() {
            return feilmelding;
        }

        public void setFeilmelding(String feilmelding) {
            this.feilmelding = feilmelding;
        }

        @Override
        public String toString() {
            return "IdentinfoForAktoer{" + "identer=" + identer +
                    ", feilmelding=" + feilmelding +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IdentinfoForAktoer that = (IdentinfoForAktoer) o;
            return Objects.equals(getIdenter(), that.getIdenter()) &&
                    Objects.equals(getFeilmelding(), that.getFeilmelding());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getIdenter(), getFeilmelding());
        }
    }
}
