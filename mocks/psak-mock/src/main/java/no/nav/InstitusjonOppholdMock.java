package no.nav;

import io.swagger.annotations.Api;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(tags = {"InstitusjonOpphold"})
@Path("/v1/person/institusjonsopphold")
public class InstitusjonOppholdMock {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Institusjonsopphold> getIdenter(@HeaderParam("Nav-Personident") String requestIdent) {
        return new ArrayList<Institusjonsopphold>();
    }


    public static class Institusjonsopphold {
        private int oppholdId;
        private String tssEksternId;
        private String organisasjonsnummer;
        private String institusjonstype;
        private String varighet;
        private String kategori;
        private Date startdato;
        private Date faktiskSluttdato;
        private Date forventetSluttdato;
        private String kilde;
        private String overfoert;
        private String endretAv;
        private Date endringstidspunkt;

        public String getVarighet() {
            return varighet;
        }

        public String getKategori() {
            return kategori;
        }

        public int getOppholdId() {
            return oppholdId;
        }

        public String getTssEksternId() {
            return tssEksternId;
        }

        public String getOrganisasjonsnummer() {
            return organisasjonsnummer;
        }

        public String getInstitusjonstype() {
            return institusjonstype;
        }

        public Date getStartdato() {
            return startdato;
        }

        public Date getFaktiskSluttdato() {
            return faktiskSluttdato;
        }

        public Date getForventetSluttdato() {
            return forventetSluttdato;
        }

        public String getKilde() {
            return kilde;
        }

        public String getOverfoert() {
            return overfoert;
        }

        public String getEndretAv() {
            return endretAv;
        }

        public Date getEndringstidspunkt() {
            return endringstidspunkt;
        }

        public void setOppholdId(int oppholdId) {
            this.oppholdId = oppholdId;
        }

        public void setTssEksternId(String tssEksternId) {
            this.tssEksternId = tssEksternId;
        }

        public void setOrganisasjonsnummer(String organisasjonsnummer) {
            this.organisasjonsnummer = organisasjonsnummer;
        }

        public void setInstitusjonstype(String institusjonstype) {
            this.institusjonstype = institusjonstype;
        }

        public void setVarighet(String varighet) {
            this.varighet = varighet;
        }

        public void setKategori(String kategori) {
            this.kategori = kategori;
        }

        public void setStartdato(Date startdato) {
            this.startdato = startdato;
        }

        public void setFaktiskSluttdato(Date faktiskSluttdato) {
            this.faktiskSluttdato = faktiskSluttdato;
        }

        public void setForventetSluttdato(Date forventetSluttdato) {
            this.forventetSluttdato = forventetSluttdato;
        }

        public void setKilde(String kilde) {
            this.kilde = kilde;
        }

        public void setOverfoert(String overfoert) {
            this.overfoert = overfoert;
        }

        public void setEndretAv(String endretAv) {
            this.endretAv = endretAv;
        }

        public void setEndringstidspunkt(Date endringstidspunkt) {
            this.endringstidspunkt = endringstidspunkt;
        }
    }
}
